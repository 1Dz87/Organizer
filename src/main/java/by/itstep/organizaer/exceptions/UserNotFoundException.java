package by.itstep.organizaer.exceptions;

import by.itstep.organizaer.constants.StringConstants;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super(String.format(StringConstants.USER_NOT_FOUND_ERR_MSG_TEMPLATE, id));
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
