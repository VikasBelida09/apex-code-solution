package com.apex.eqp.inventory.Exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg){
        super(msg);
    }
    public ResourceNotFoundException(){
        super("Resource with the given id is not present in the server");
    }
}
