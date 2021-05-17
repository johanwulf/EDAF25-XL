package model;

import util.XLBufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import expr.*;

public class XLModel implements Environment {
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
    if (text.equals("")) {        // alt 1) text är tom
      cells.put(address.toString(), new EmptyCell());
      this.notifyObservers(address.toString(), "");
    } else if (text.startsWith("#")) {        // alt 2) text börjar med # -> kommentar
      String comment = text.substring(1,text.length());
      cells.put(address.toString(), new CommentCell(comment));
      this.notifyObservers(address.toString(), cells.get(address.toString()).toString());
    } else {    // alt 3) text är ett uttryck
      ExprCell newCell = new ExprCell(text);
      cells.put(address.toString(), new CircularCell());
      try {       // kolla om uttrycket är cirkulärt
        newCell.evaluate(this);
        cells.put(address.toString(), newCell);
      } catch (Error e) {        // Hantera cirkulärt fel
        cells.put(address.toString(), new ErrorCell(e.getMessage()));
      }
      ExprResult result = this.value(address.toString());
      if(result.isError()) {
        this.notifyObservers(address.toString(), result.toString());
      } else {
        this.notifyObservers(address.toString(), String.valueOf(result.value()));
      }
    }
    ExprResult result = cells.get(address.toString()).evaluate(this);
  }

  public void updateAllCells() {
    cells.entrySet().forEach(entry -> {
      String address = entry.getKey();
      Cell cell = entry.getValue();
      String value = cell.toString();

      if (cell instanceof ExprCell) {
        value = String.valueOf(cell.evaluate(this).value());
      }

      notifyObservers(address, value);
    });
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
  }

  public void saveFile(File file) {
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
      this.notifyObservers(address, "");
    }
  }

  public void clearCell(CellAddress address) {
    cells.put(address.toString(), new EmptyCell());
    this.notifyObservers(address.toString(), "");
  }
}
