package expr;

import model.*;

public interface Environment {
  ExprResult value(String name) throws CircularError;
}
