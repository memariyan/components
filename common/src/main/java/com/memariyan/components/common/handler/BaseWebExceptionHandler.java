package com.memariyan.components.common.handler;

import com.memariyan.components.common.exception.*;
import com.memariyan.components.common.model.ErrorCodes;
import com.memariyan.components.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseWebExceptionHandler {

    private final MessageSource messageSource;

    private static final String DEFAULT_LOCALE = "en";

    //----------------------------------- Validation Exceptions

    @ExceptionHandler(InvalidParameterException.class)
    public final ResponseEntity<ErrorResponse> handle(InvalidParameterException ex, HandlerMethod handlerMethod) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCodes.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        var errorMessages = getMessages(exception.getAllErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCodes.BAD_REQUEST, errorMessages));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handle(HandlerMethodValidationException exception) {
        var errorMessages = getErrorMessages(exception.getAllErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCodes.BAD_REQUEST, errorMessages));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handle(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCodes.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCodes.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public final ResponseEntity<ErrorResponse> handle(WebExchangeBindException ex, HandlerMethod handlerMethod) {
        var errorMessages = getMessages(ex.getBindingResult().getAllErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(null, errorMessages));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handle(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCodes.BAD_REQUEST, getMessage(exception)));
    }

    //----------------------------------- Logical Exceptions

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException exception) {
        log.error("not found exception : ", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCodes.NOT_FOUND, getMessage(exception)));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handle(DuplicateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCodes.CONFLICT, getMessage(exception)));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ErrorCodes.FORBIDDEN, getMessage(exception)));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handle(ClientException exception) {
        log.error("unhandled client exception : ", exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(ErrorCodes.UNKNOWN, getMessage(exception)));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessException exception) {
        log.debug("business exception : ", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(exception.getErrorCode(), getMessage(exception)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        log.error("unhandled exception : ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorCodes.UNKNOWN, exception.getMessage()));
    }

    private String getMessage(BaseException exception) {
        return getMessage(exception.getMessage());
    }

    private String getMessage(ObjectError error) {
        if (StringUtils.isBlank(error.getDefaultMessage())) {
            return "Error";
        }
        return getMessage(error.getDefaultMessage());
    }

    private String getMessage(String errorMsg) {
        try {
            String message = messageSource.getMessage(errorMsg, null, getLocale());
            return StringUtils.isBlank(message) ? errorMsg : message;
        } catch (NoSuchMessageException ex) {
            return errorMsg;
        }
    }

    private String getMessage(MessageSourceResolvable errorMsg) {
        try {
            String message = messageSource.getMessage(errorMsg.getDefaultMessage(), null, getLocale());
            return StringUtils.isBlank(message) ? errorMsg.getDefaultMessage() : message;
        } catch (NoSuchMessageException ex) {
            return errorMsg.getDefaultMessage();
        }
    }

    private List<String> getMessages(List<ObjectError> errors) {
        return errors.stream()
                .filter(e -> StringUtils.isNotBlank(e.getDefaultMessage()))
                .map(this::getMessage)
                .collect(Collectors.toList());
    }

    private List<String> getErrorMessages(List<? extends MessageSourceResolvable> errors) {
        return errors.stream()
                .filter(e -> StringUtils.isNotBlank(e.getDefaultMessage()))
                .map(this::getMessage)
                .collect(Collectors.toList());
    }

    private Locale getLocale() {
        return Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.of(DEFAULT_LOCALE));
    }

}
