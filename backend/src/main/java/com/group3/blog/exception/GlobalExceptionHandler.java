package com.group3.blog.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Map<String,String>> authentication(AuthenticationException e){return ResponseEntity.status(401).body(Map.of("message","用户名或密码错误"));}
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<Map<String,String>> business(BusinessException e){return ResponseEntity.status(e.getStatus()).body(Map.of("message",e.getMessage()));}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){FieldError error=e.getBindingResult().getFieldErrors().get(0);return ResponseEntity.badRequest().body(Map.of("message",error.getField()+"："+error.getDefaultMessage()));}
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ResponseEntity<Map<String,String>> conflict(ObjectOptimisticLockingFailureException e){return ResponseEntity.status(409).body(Map.of("message","数据已被其他操作修改，请刷新后重试"));}
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String,String>> duplicate(DataIntegrityViolationException e){return ResponseEntity.status(409).body(Map.of("message","数据重复或仍被其他记录引用"));}
    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String,String>> unknown(Exception e){return ResponseEntity.internalServerError().body(Map.of("message","服务器内部错误"));}
}
