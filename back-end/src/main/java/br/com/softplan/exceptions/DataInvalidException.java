package br.com.softplan.exceptions;

public class DataInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataInvalidException(String msg) {
        super(msg);
    }
}