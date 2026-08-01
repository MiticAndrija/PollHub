package com.pollhub.exception;

public class MissingRoleException extends RuntimeException {

    public MissingRoleException(String roleName) {
        super("Required role is missing: " + roleName);
    }
}
