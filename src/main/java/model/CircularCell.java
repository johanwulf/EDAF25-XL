package model;

import expr.Environment;
import expr.ExprResult;

public class CircularCell implements Cell {
    @Override public ExprResult evaluate(Environment env) throws CircularError {
        throw new CircularError("Circular Value");
    }

}