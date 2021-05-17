package model;

public abstract class CellFactory {
    public Cell createCell(String text) {
        if(text.startsWith("#")) {
            return new CommentCell(text);
        } else {
            return new ExprCell(text);
        }
    }
}
