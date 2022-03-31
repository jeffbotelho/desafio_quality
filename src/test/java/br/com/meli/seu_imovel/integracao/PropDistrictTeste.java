package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
/*
* @author Gabriel Essenio
*/
public class PropDistrictTeste {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    // Teste se lança a exceçao quando o nome do bairro for maior que 45 caracteres
    @Test
    @DisplayName("Deve Retornar um excecao quando o tamanho do nome do bairro for maior do que 45")
    public void itShouldReturnAValidationExceptionWhenDistrictNameIsBiggerThan() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        String nomeDistrict = "São Miguel Paulista da Silva Souza Soares dos Santos e Santigo Carmargos Dias";
        imovelDTO.setPropDistrict(nomeDistrict);
        String payload = objectMapper.writeValueAsString(imovelDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.msg")
                        .value("O comprimento do bairro não pode exceder 45 caracteres."));
    }

    // Testa se lança a exceção quando o nome do bairro estiver vazio
    @Test
    @DisplayName("Deve Retornar um excecao quando o nome do bairro estiver vazio")
    public void itShouldReturnAValidationExceptionWhenDistrictIsEmpty() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setPropDistrict("");
        String payload = objectMapper.writeValueAsString(imovelDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.msg")
                        .value("O bairro não pode estar vazio. (Distrito)"));
    }

    // metodo que gera um ImovelDTO para realizar teste
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
