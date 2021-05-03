package model;

import util.XLBufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import expr.*;

public class XLModel implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;

  Map<String, Cell> cells;

  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   */
  public void update(CellAddress address, String text) {
    // alt 1) text är tom
    // alt 2) text börjar med # -> kommentar
    // alt 3) text är ett uttryck

    ExprCell newCell = new ExprCell(text);
    cells.put(address.toString(), new CircularCell());

    try {
      newCell.evaluate(this);
      cells.put(address.toString(), newCell);
    } catch (Error e) {
      // Hantera cirkulärt fel
      cells.put(address.toString(), new ErrorCell());
    }
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
  }

  public void saveFile(File file) {
  }

  @Override
  public ExprResult value(String name) {
    Cell cell = cells.get(name.toLowerCase());
    return cell.evaluate(this);
  }
}
