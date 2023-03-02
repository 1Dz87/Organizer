package by.itstep.organizaer.aspect;

import by.itstep.organizaer.exceptions.*;
import by.itstep.organizaer.model.dto.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "by.itstep.organizaer.web")
public class ExceptionHandlingAdvice {

    @ExceptionHandler(value = {UserNotFoundException.class, AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonException handleNotFound(Exception ex) {
        return CommonException
                .builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {AccountAlreadyExistsException.class, UserAlreadyExistsException.class,
            TransactionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonException handleBadRequest(Throwable ex) {
        return CommonException
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonException handleValidationFail(MethodArgumentNotValidException ex) {
        FieldError err = ex.getBindingResult().getFieldError();
        return CommonException
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Запрос не прошел валидацию. Поле %s имеет не валидное значение %s", err.getField(), err.getRejectedValue()))
                .build();
    }

    @ExceptionHandler(value = InteractionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonException handleInteractionException(InteractionException ex) {
        return CommonException
                .builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
    }
}
