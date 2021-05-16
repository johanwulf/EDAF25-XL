package model;

import expr.*;

public class ErrorCell implements Cell {

    private String error;

    public ErrorCell(String error) {
        this.error = error;
    }

    public String toString() {  // redigera
        return error;
    }

    @Override
    public ExprResult evaluate(Environment env) throws CircularError {
        return new ErrorResult(error);
    }
}
