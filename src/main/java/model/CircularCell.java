package model;

import expr.Environment;
import expr.ExprResult;

public class CircularCell implements Cell {

    public CircularCell() {

    }

    public String toString() {  // redigera
        return "circularcell";
    }

    @Override public ExprResult evaluate(Environment env) throws CircularError {
        throw new CircularError("circular value");
    }

}