package model;

import util.XLException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class XLBufferedReader extends BufferedReader {
  public XLBufferedReader(File file) throws FileNotFoundException {
    super(new FileReader(file));
  }

  // TODO Change Object to something appropriate
  public void load(Map<String, Cell> map) throws IOException {
    try {
      while (ready()) {
        String string = readLine();
        String[] values = string.split("=", 2);

        String address = values[0];
        String value = values[1];

        if (value.startsWith("#")) {
          map.put(address, new CommentCell(value));
        } else if (value.equals("")) {
          map.put(address, new EmptyCell());
        } else {
          map.put(address, new ExprCell(value));
        }
      }
    } catch (Exception e) {
      throw new XLException(e.getMessage());
    }
  }
}
