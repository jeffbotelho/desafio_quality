package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ImoveisControllerTest {

    @Autowired
    private MockMvc mockMvc; // faz uma request

    @Autowired
    private ObjectMapper objectMapper; // transforma objeto em json

    // cria um objeto do imóvel para testes
    private ImovelDTO geraImovelTeste() {
        ImovelDTO imovelDTO = new ImovelDTO();
        imovelDTO.setPropName("Imovel1");
        imovelDTO.setPropDistrict("Jaçanã");
        imovelDTO.setValueDistrictM2(BigDecimal.valueOf(10000));
        List<ComodoDTO> comodos = new ArrayList<>(Arrays.asList(
                new ComodoDTO("Comòdo1", 10D, 15D, null),
                new ComodoDTO("Comodo2", 12D, 15D, null),
                new ComodoDTO("Comodo3", 10D, 20D, null)
                // option + shift + clic para editar vario de uma vez
        ));

        imovelDTO.setRooms(comodos);
        return imovelDTO;
    }

    // executa o caminho com tudo correto
    // decidimos fazer em grupo o caminho feliz
    @Test
    @DisplayName("Testando o caminho feliz.")
    public void testandoCaminhoFeliz () throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();

        String payloadImovel = objectMapper.writeValueAsString(imovelDTO);

        // faz requisição
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payloadImovel))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // transforma o resultado da requisição em string
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // desserializar, transforma a string em um objeto para manipulação.
        ImovelDTO imovelObjeto = objectMapper.readValue(contentAsString, ImovelDTO.class);

        Assertions.assertEquals(BigDecimal.valueOf(5300000D), imovelObjeto.getTotalPrice());
        Assertions.assertEquals("Comodo3", imovelObjeto.getBigestRoom().getRoomName());
        Assertions.assertEquals(200D, imovelObjeto.getBigestRoom().getArea());
        Assertions.assertEquals(530D, imovelObjeto.getTotalArea());
    }

}
