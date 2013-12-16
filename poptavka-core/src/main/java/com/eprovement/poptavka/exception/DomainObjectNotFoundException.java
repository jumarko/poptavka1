package com.eprovement.poptavka.exception;

public class DomainObjectNotFoundException extends RuntimeException {

    private final long id;
    private final Class clazz;

    public DomainObjectNotFoundException(long id, Class clazz) {
        super("No object of class " + clazz + " and id " + id + "found!");
        this.id = id;
        this.clazz = clazz;
    }

    public long getId() {
        return id;
    }

    public Class getDomainObjectClass() {
        return clazz;
    }
}
