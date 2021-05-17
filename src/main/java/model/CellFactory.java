package model;

public abstract class CellFactory {
    public Cell createCell(String text) {
        if(text.startsWith("#")) {
            return new CommentCell(text);
        } else if (text.equals("")) {
            return new EmptyCell();
        } else {
            return new ExprCell(text);
        }
    }
}
