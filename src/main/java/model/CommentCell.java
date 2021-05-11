package model;

import expr.*;

public class CommentCell implements Cell {

    private String comment;

    public CommentCell(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return comment;
    }

    @Override
    public ExprResult evaluate(Environment env) {
        return new ErrorResult("cannot use comment");
    }
}
