package br.com.meli.seu_imovel.controller;

import br.com.meli.seu_imovel.dto.ImovelDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ImoveisController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/relatorio-de-imovel")
    public ResponseEntity<ImovelDTO> getImmobileReport (@Valid @RequestBody  ImovelDTO imovel) {
        return ResponseEntity.ok(new ImovelDTO());
    }
}
