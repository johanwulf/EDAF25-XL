package model;

import expr.Environment;
import expr.ExprResult;

public class CircularCell implements Cell {
    String expr;

    public CircularCell(String expression) {
        expr = expression;
    }

    public String toString() {
        return expr;
    }

    @Override public ExprResult evaluate(Environment env) throws CircularError {
        throw new CircularError("circular value");
    }
}