package com.sumauto.exception;


public class LibraryInitException extends RuntimeException {
    public LibraryInitException(String detailMessage) {
        super(detailMessage);
    }

    public LibraryInitException(Class cls) {
        super("please call " + cls.getName() + " init() method first!");
    }
}
