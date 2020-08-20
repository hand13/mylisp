package com.hand13.exception;

public class LispException extends RuntimeException {
    private String fileName;
    private int lineNumber;
    private int x;

    public LispException(String msg) {
        super(msg);
    }

    public LispException(String msg, int lineNumber, int x, String fileName) {
        super(msg);
        this.lineNumber = lineNumber;
        this.x = x;
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getX() {
        return x;
    }

    public String getFileName() {
        return fileName;
    }
}
