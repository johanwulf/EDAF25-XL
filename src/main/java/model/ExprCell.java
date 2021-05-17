package model;

import expr.*;

import java.io.IOException;

public class ExprCell implements Cell {
    Expr expr;
    String rawexpr;

    public ExprCell(String expression) {
        rawexpr = expression;

        try {
            expr = new ExprParser().build(expression);
        } catch (IOException e) {
            expr = new ErrorExpr(e.getMessage());
        }
    }



    public String toString() {  // redigera
        return rawexpr;
    }

    @Override
    public ExprResult evaluate(Environment env) {
        return expr.value(env);
    }
}
