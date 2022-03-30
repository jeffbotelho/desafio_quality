package br.com.meli.seu_imovel.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/*
 * trata as exception relacionadas às validações, devolvendo uma mensagem mais inteligível
 * @Author Rogério Lambert
 * */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class StandardException {
    private Integer status;
    private String msg;
    private Instant timestamp;
    private String path;

    public static StandardException badRequest(String msg, String path){
        return new StandardException(HttpStatus.BAD_REQUEST.value(), msg, Instant.now(), path);
    }
}
