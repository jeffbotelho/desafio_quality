package br.com.meli.seu_imovel.service;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.repositories.BairroRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImoveisServiceTest {

    @Mock
    BairroRepository bairroRepository;

    @InjectMocks
    ImoveisService imoveisService;

    private ImovelDTO geraImovelTeste() {
        ImovelDTO imovelDTO = new ImovelDTO();
        imovelDTO.setPropName("Imovel1");
        imovelDTO.setPropDistrict("centro");
        imovelDTO.setValueDistrictM2(BigDecimal.valueOf(10000));
        List<ComodoDTO> comodos = new ArrayList<>(Arrays.asList(
                new ComodoDTO("comodo1", 10D, 15D, null),
                new ComodoDTO("comodo2", 12D, 15D, null),
                new ComodoDTO("comodo3", 10D, 20D, null)
        ));

        imovelDTO.setRooms(comodos);
        return imovelDTO;
    }

    @Test
    public void calculaAreaTotalTest() {

    }

    @Test
    public void calculaValorTotalTest() {

    }

    @Test
    public void calculaMaiorComodoTest() {

    }

    @Test
    @DisplayName("Testar se calculo da area esta correto")
    public void calculaSeAreaDoComodoEstaCorretaTest() {

        // setaup
        ImovelDTO imovelDTO = geraImovelTeste();

        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("encontrado"));

        ImovelDTO response = imoveisService.gerarRelatorio(imovelDTO);

        assertEquals(150D, response.getRooms().get(0).getArea());
        assertEquals(180D, response.getRooms().get(1).getArea());
        assertEquals(200D, response.getRooms().get(2).getArea());

    }

    @Test
    @DisplayName("Testar se calculo da area  nao esta correto")
    public void calculaSeAreaDoComodoNaoEstaCorretaTest() {
        // setaup
        ImovelDTO imovelDTO = geraImovelTeste();

        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));

        ImovelDTO response = imoveisService.gerarRelatorio(imovelDTO);

        assertNotEquals(170D, response.getRooms().get(0).getArea());
        assertNotEquals(190D, response.getRooms().get(1).getArea());
        assertNotEquals(210D, response.getRooms().get(2).getArea());

    }


}
