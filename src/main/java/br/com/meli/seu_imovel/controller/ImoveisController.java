package br.com.meli.seu_imovel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImoveisController {

    @GetMapping("/ping")
    public String ping() {
        System.out.println("pong");
    }
}
