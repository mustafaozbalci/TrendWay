package commercial.TrendWay.controller;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModel> handleBadRequestException(BadRequestException ex) {
        logger.error("Handling BadRequestException: {}", ex.getMessage());
        ResponseModel responseModel = new ResponseModel(ex.getErrorCode().getCode(), ex.getMessage(), null);
        return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
    }
}
