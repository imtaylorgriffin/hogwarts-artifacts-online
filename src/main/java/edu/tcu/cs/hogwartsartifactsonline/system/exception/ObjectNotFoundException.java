package edu.tcu.cs.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find "+ objectName +" with ID: "+id+" :(");
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find "+ objectName +" with ID: "+id+" :(");
    }

    // code wouldnt work unless i added these ones too
    public ObjectNotFoundException(String message) {
        super("Could not find artifact with ID: "+message+" :(");
    }

    public ObjectNotFoundException(Integer id) {
        super("Could not find wizard with ID: "+id+" :(");
    }
}
