package Compiler.Error;

public class ParserError extends Error {
    public static Throwable error(String message) {
        Error.error(Error.lineno, Error.column, message);
        return new RuntimeException();
    }
}

