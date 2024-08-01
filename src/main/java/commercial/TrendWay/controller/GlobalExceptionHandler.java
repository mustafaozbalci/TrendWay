package commercial.TrendWay.controller;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModel> handleBadRequestException(BadRequestException ex) {
        ResponseModel responseModel = new ResponseModel(ex.getErrorCode().getCode(), ex.getMessage(), null);
        return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
    }
}
