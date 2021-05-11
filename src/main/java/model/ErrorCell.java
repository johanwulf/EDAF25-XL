package model;

import expr.*;

public class ErrorCell implements Cell {

    private String error;

    public ErrorCell(String error) {
        this.error = error;
    }

    @Override
    public ExprResult evaluate(Environment env) throws CircularError {
        return new ErrorResult(error);
    }
}
