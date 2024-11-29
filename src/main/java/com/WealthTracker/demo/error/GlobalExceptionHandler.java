package com.WealthTracker.demo.error;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import io.jsonwebtoken.JwtException;
import org.hibernate.Internal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler { //** 예외 처리에 관한 Handler

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 필요한 다른 예외 처리 추가
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex){
        return ResponseEntity
                .status(HttpStatus.valueOf(ex.getErrorCode().getStatus()))
                .body(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleServerException(Exception e){
        return new ResponseEntity(new ReturnCodeDTO(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),ErrorCode.INTERNAL_SERVER_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> dtoValidation(final MethodArgumentNotValidException e){
        Map<String ,String> errors=new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError) error).getField();
            String errorMessage=error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
