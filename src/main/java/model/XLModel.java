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
    if (text.equals("")) {        // alt 1) text är tom
      cells.put(address.toString(), new EmptyCell());
    } else if (text.startsWith("#")) {        // alt 2) text börjar med # -> kommentar
      String comment = text.substring(1,text.length());
      cells.put(address.toString(), new CommentCell(comment));
    } else {    // alt 3) text är ett uttryck
      ExprCell newCell = new ExprCell(text);
      cells.put(address.toString(), new CircularCell());
      try {       // kolla om uttrycket är cirkulärt
        newCell.evaluate(this);
        cells.put(address.toString(), newCell);
      } catch (CircularError e) {        // Hantera cirkulärt fel
        cells.put(address.toString(), new ErrorCell(e.getMessage()));
      }
    }
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
  }

  public void saveFile(File file) {
  }

  @Override
  public ExprResult value(String name) throws CircularError {
    Cell cell = cells.get(name.toLowerCase());
    return cell.evaluate(this);
  }
}
