package com.library.fullstackbackend.exception;
/* Created by Arjun Gautam */

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID id){
        super("Could not found the user with id "+ id);
    }
}
