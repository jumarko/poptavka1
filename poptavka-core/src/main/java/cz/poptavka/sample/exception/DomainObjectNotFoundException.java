
package cz.poptavka.sample.exception;

public class DomainObjectNotFoundException extends RuntimeException {

    public DomainObjectNotFoundException(long id, Class t) {
        super("No object of class " + t + " and id " + id + "found!");
    }
}
