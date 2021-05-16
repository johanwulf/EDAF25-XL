package model;

import expr.*;

import java.io.IOException;

public class ExprCell implements Cell {
    Expr expr;

    public ExprCell(String expression) {
        try {
            expr = new ExprParser().build(expression);
        } catch (IOException e) {
            expr = new ErrorExpr(e.getMessage());
        }
    }

    public String toString() {  // redigera
        return expr.toString();
    }

    @Override
    public ExprResult evaluate(Environment env) {
        return expr.value(env);
    }
}
