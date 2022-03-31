package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ValueDistrictTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void verificaSeValorDoDistristroPorMetroQuadradoMaiorQueTrezeDigitos() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setValueDistrictM2(BigDecimal.valueOf(10000000000000D));

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("O valor do metro quadrado nao pode ser maior que 13 digitos. (Valor Distrito)"));

    }

    @Test
    public void verificaSeValorDoDistristroPorMetroQuadradoNaoNulo() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setValueDistrictM2(null);

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("O valor do metro quadrado não pode estar vazio. (Valor do Distrito)"));

    }

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
}
