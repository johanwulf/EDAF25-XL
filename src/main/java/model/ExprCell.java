package model;

import expr.*;

import java.io.IOException;

public class ExprCell implements Cell {
    Expr expr;
    String string;

    public ExprCell(String expression) {
        try {
            expr = new ExprParser().build(expression);
        } catch (IOException e) {
            expr = new ErrorExpr(e.getMessage());
        }
    }

    @Override
    public ExprResult evaluate(Environment env) {
        return expr.value(env);
    }
}
