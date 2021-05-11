package model;

import expr.*;

public class EmptyCell implements Cell {

    public String toString() {
        return "";
    }

    @Override
    public ExprResult evaluate(Environment env) throws CircularError {
        return new ErrorResult("empty cell");
    }
}