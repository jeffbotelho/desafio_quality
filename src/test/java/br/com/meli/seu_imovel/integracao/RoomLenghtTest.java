package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
@Author Nayara Coca
 */

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RoomLenghtTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar uma exceção quando o comprimento não for informado")
    public void lengthNotNull() throws Exception{
    ImovelDTO imovelDTO = geraImovelTeste();
    imovelDTO.getRooms().get(0).setRoomLength(null);
    String payload = objectMapper.writeValueAsString(imovelDTO);
    mockMvc.perform(MockMvcRequestBuilders.post("/relatorio-de-imovel")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("O comprimento não pode estar vazio."));

    }

    @Test
    @DisplayName("Deve retornar uma exceção quando o comprimento for maior que 33 metro")
public void lengthNotGraterThan33() throws Exception{
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.getRooms().get(0).setRoomLength(34D);

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                .value("O comprimento máximo permitidio por cômodo é de 33 metros."));
    }

    private ImovelDTO geraImovelTeste() {
        ImovelDTO imovelDTO = new ImovelDTO();
        imovelDTO.setPropName("Imovel1");
        imovelDTO.setPropDistrict("Jaçanã");
        imovelDTO.setValueDistrictM2(BigDecimal.valueOf(10000));
        List<ComodoDTO> comodos = new ArrayList<>(Arrays.asList(
                new ComodoDTO("Cômodo1", 10D, 15D, null),
                new ComodoDTO("Comodo2", 12D, 15D, null),
                new ComodoDTO("Comodo3", 10D, 20D, null)
        ));

        imovelDTO.setRooms(comodos);
        return imovelDTO;
    }
}
