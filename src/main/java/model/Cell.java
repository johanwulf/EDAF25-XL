package model;
import expr.*;

public interface Cell {
    ExprResult evaluate(Environment env) throws CircularError;
    String toString();
}