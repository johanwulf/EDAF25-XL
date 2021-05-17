package model;

import util.XLBufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import expr.*;
import util.XLPrintStream;

public class XLModel extends CellFactory implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;

  Map<String, Cell> cells = new HashMap<>();
  ArrayList<Observer> observers = new ArrayList<>();

  public XLModel() {
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        cells.put(new CellAddress(row, col).toString(), new EmptyCell());
      }
    }
  }

  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   */
  public void update(CellAddress address, String text) {
    String rawAddress = address.toString();
    Cell cell = createCell(text);
    cells.put(rawAddress, cell);

    updateAllCells();
  }

  public void updateAllCells() {
    cells.entrySet().forEach(entry -> {
      String address = entry.getKey();
      String text = entry.getValue().toString();

      String value = calculateCell(text, address);
      this.notifyObservers(address, value);
    });
  }

  private String calculateCell(String text, String address) {
    if (text == null || text.equals("")) {
      return "";
    }

    if (text.startsWith("#")) {
      return text.substring(1);
    }

    ExprCell cell = new ExprCell(text);
    cells.put(address, new CircularCell(text));

    try {
      cells.put(address, cell);
      return cell.evaluate(this).toString();
    } catch (Error e) {
      return new ErrorExpr("circular value").toString();
    }
  }

  public void loadFile(File file) throws IOException {
    XLBufferedReader reader = new XLBufferedReader(file);
    clearAll();
    reader.load(cells);
    updateAllCells();
  }

  public void saveFile(File file) throws FileNotFoundException {
    System.out.println("in");
    XLPrintStream writer = new XLPrintStream(file.getName());
    writer.save(cells.entrySet());
  }

  @Override
  public ExprResult value(String name) {
    Cell cell = cells.get(name);
    return cell.evaluate(this);
  }

  public String readCell(CellAddress address) {
    return cells.get(address.toString()).toString();
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  private void notifyObservers(String address, String value) {
    for(Observer observer : observers) {
      observer.onUpdate(address, value);
    }
  }

  public void clearAll() {
    for (String address : cells.keySet()) {
      cells.put(address, new EmptyCell());
    }
    updateAllCells();
  }

  public void clearCell(CellAddress address) {
    cells.put(address.toString(), new EmptyCell());
    updateAllCells();
  }
}
