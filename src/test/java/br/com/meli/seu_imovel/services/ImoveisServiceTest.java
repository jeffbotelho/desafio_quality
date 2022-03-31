package br.com.meli.seu_imovel.services;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.exceptions.DistrictNotExistExepection;
import br.com.meli.seu_imovel.repositories.BairroRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    //funçao para mokar o resultado esperado
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

  //CALCULA VALOR TOTAL
    //------------------------------
    //Weverton e Rogerio

    @Test
    @DisplayName("Teste se cálculo do valor total da propriedade é realizado corretamente:")
    public void testeCalculoValorTotal() {
        // Setup do teste: gera um objeto de request e mocka repository
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));

        // Act: Execucao do service
        ImovelDTO returnedImovelDTO = imoveisService.gerarRelatorio(imovelDTO);

        // Assert: verifica se o valor do imovel esta conforme o esperado
        Assertions.assertEquals(returnedImovelDTO.getTotalPrice(), BigDecimal.valueOf(5300000D));
    }

    @Test
    @DisplayName("Teste se uma exceção é lançada quando o bairro não consta no repositório")
    public void testeSeLancaExcecaoBairroNaoEncontrado() {
        // Setup do teste: gera um objeto de request e mocka repository
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.empty());

        // Act & Assertion: Verifica se ao executar o service uma excecao eh lancada por nao encontrar o bairro
        Throwable exceptionReturned = Assertions.assertThrows(DistrictNotExistExepection.class, () -> imoveisService.gerarRelatorio(imovelDTO));
        Assertions.assertEquals(exceptionReturned.getMessage(), "Distrito não válido!");
    }

    @Test
    @DisplayName("Testar se o valor do nome do bairro consultado no repositório é o mesmo passado na requisição")
    public void testeSeValorConsultaBairroIgualAoEnviadoNaRequisicao() {
        // Setup do teste: Gera um objeto de request e mocka repositorio
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName(Mockito.anyString())).thenReturn(Optional.of("centro"));

        // Act: Execucao do service
        imoveisService.gerarRelatorio(imovelDTO);

        // Assert: VErifica se o metodo findByName do repository é chamado 1 vez e
        // verifica se esse metodo é chamado passando como argumento o valor "centro"
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(bairroRepository, Mockito.times(1)).findByName(captor.capture());
        Assertions.assertEquals("centro", captor.getValue());
    }

    //MAIOR COMODO
    //Nayara e Gabriel
    //  --------------------------------
    @Test
    @DisplayName("Testa se o maior cômodo retorna com nome correto")
    public void testeMaiorQuarto(){
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO imovelRetorno = imoveisService.gerarRelatorio(imovelDTO);
        ComodoDTO bigestRoom = imovelRetorno.getBigestRoom();
        Assertions.assertEquals("comodo3",bigestRoom.getRoomName());
    }
    @Test
    @DisplayName("Testa se, ao inserir 2 cômodos do mesmo tamanho, retorna o primeiro cômodo inserido")
    public void testeQuartosIguais(){
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.getRooms().get(0).setRoomLength(5D);
        imovelDTO.getRooms().get(0).setRoomWidth(40D);
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO imovelRetorno = imoveisService.gerarRelatorio(imovelDTO);
        ComodoDTO bigestRoom = imovelRetorno.getBigestRoom();
        Assertions.assertEquals("comodo1",bigestRoom.getRoomName());
    }
    @Test
    @DisplayName("Testar se o calculo total da propriedade é determinado corretamente para um dado de entrada conhecido")
    public void testeAreaImovel(){
        ImovelDTO imovelDTO = geraImovelTeste();
        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));
        ImovelDTO imovelRetorno = imoveisService.gerarRelatorio(imovelDTO);
        double totalArea  = imovelRetorno.getTotalArea();
        Assertions.assertEquals(530D,totalArea);
    }

    //  -------------------------------------------------
    // VERIFICA SE CALCULO ESTA CERTO
    // Ederson e Jeffeson
    @Test
    @DisplayName("Testar se calculo da area esta correto")
    public void calculaSeAreaDoComodoEstaCorretaTest() {

        // setaup
        ImovelDTO imovelDTO = geraImovelTeste();

        Mockito.when(bairroRepository.findByName("centro")).thenReturn(Optional.of("centro"));

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
