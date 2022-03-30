package br.com.meli.seu_imovel.service;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.repositories.BairroRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BiggerRoomTest {
    @Mock
    private BairroRepository bairroRepository;
    @InjectMocks
    private ImoveisService imoveisService;

    @Test
    @DisplayName("Testa se o maior cômodo retorna com nome correto")
    public void testeMaiorQuarto(){
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO maiorComodo = imoveisService.gerarRelatorio(imovelDTO);
        ComodoDTO bigestRoom = maiorComodo.getBigestRoom();
        Assertions.assertEquals("comodo3",bigestRoom.getRoomName());
    }
    @Test
    @DisplayName("Testa se, ao inserir 2 cômodos do mesmo tamanho, retorna o primeiro cômodo inserido")
    public void testeQuartosIguais(){
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.getRooms().get(0).setRoomLength(5D);
        imovelDTO.getRooms().get(0).setRoomWidth(40D);
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO maiorComodo = imoveisService.gerarRelatorio(imovelDTO);
        ComodoDTO bigestRoom = maiorComodo.getBigestRoom();
        Assertions.assertEquals("comodo1",bigestRoom.getRoomName());
    }
    @Test
    @DisplayName("Testar se o calculo total da propriedade é determinado corretamente para um dado de entrada conhecido")
    public void testeAreaImovel(){
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO maiorComodo = imoveisService.gerarRelatorio(imovelDTO);
        ComodoDTO bigestRoom = maiorComodo.getBigestRoom();
        Assertions.assertEquals(200D,bigestRoom.getArea());
    }

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


}
