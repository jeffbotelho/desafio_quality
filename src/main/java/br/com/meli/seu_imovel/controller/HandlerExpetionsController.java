package br.com.meli.seu_imovel.controller;

import br.com.meli.seu_imovel.exceptions.StandardException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandlerExpetionsController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardException> argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
        StandardException response = StandardException.badRequest(e.getFieldError().getDefaultMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(response);
    }
}
