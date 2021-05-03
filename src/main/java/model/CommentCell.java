package model;

import expr.*;

public class CommentCell implements Cell {

    @Override
    public ExprResult evaluate(Environment env) {
        return new ErrorResult("cannot use comment");
    }
}
