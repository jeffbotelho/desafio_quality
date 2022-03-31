package br.com.meli.seu_imovel.controller;

import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.unitario.ImoveisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*
 * trata as exception relacionadas às validações, devolvendo uma mensagem mais inteligível
 * @Author Rogério Lambert
 * */

@RestController
public class ImoveisController {

    @Autowired
    private ImoveisService imoveisService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/relatorio-de-imovel")
    public ResponseEntity<ImovelDTO> getImmobileReport (@Valid @RequestBody  ImovelDTO imovel) {
        return ResponseEntity.ok(imoveisService.gerarRelatorio(imovel));
    }
}
