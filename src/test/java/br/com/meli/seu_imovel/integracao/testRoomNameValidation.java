package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.exceptions.StandardException;
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
public class testRoomNameValidation {


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
        ));

        imovelDTO.setRooms(comodos);
        return imovelDTO;
    }

    /*
     * testa validacao de roomName inexistente
     * @Author Rogério Lambert
     * */
    @Test
    @DisplayName("Testa se um erro eh retornado quando um campo de roomName eh inexistente")
    public void testaSeExcecaoParaRoomNameInexistente () throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        ComodoDTO roomWithNameEmpyt = new ComodoDTO();
        roomWithNameEmpyt.setRoomWidth(13D);
        roomWithNameEmpyt.setRoomLength(13D);
        List<ComodoDTO> rooms = imovelDTO.getRooms();
        rooms.add(roomWithNameEmpyt);
        imovelDTO.setRooms(rooms);

        String payloadImovel = objectMapper.writeValueAsString(imovelDTO);

        // faz requisição
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payloadImovel))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        // transforma o resultado da requisição em string
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // desserializar, transforma a string em um objeto para manipulação.
        StandardException response = objectMapper.readValue(contentAsString, StandardException.class);

        Assertions.assertEquals(response.getMsg(), "O campo roomName (nome do comodo) nao pode estar vazio.");
        Assertions.assertEquals(response.getStatus(), 400);
    }
    /*
     * testa validacao de roomName deve comecar com letra maiouscula
     * @Author Rogério Lambert
     * */
    @Test
    @DisplayName("Testa se um erro eh retornado quando um campo de roomName comecar com letra minuscula")
    public void testaSeExcecaoParaRoomNameComecaComMinuscula () throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        ComodoDTO roomWithNameEmpyt = new ComodoDTO();
        roomWithNameEmpyt.setRoomWidth(13D);
        roomWithNameEmpyt.setRoomLength(13D);
        roomWithNameEmpyt.setRoomName("sala");

        List<ComodoDTO> rooms = imovelDTO.getRooms();
        rooms.add(roomWithNameEmpyt);
        imovelDTO.setRooms(rooms);

        String payloadImovel = objectMapper.writeValueAsString(imovelDTO);

        // faz requisição
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payloadImovel))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        // transforma o resultado da requisição em string
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // desserializar, transforma a string em um objeto para manipulação.
        StandardException response = objectMapper.readValue(contentAsString, StandardException.class);

        Assertions.assertEquals(response.getMsg(), "O campo roomName (nome do comodo) deve comecar com uma letra maiuscula.");
        Assertions.assertEquals(response.getStatus(), 400);
    }
    /*
     * testa validacao de roomName deve comecar com letra maiouscula
     * @Author Rogério Lambert
     * */
    @Test
    @DisplayName("Testa se um erro eh retornado quando um campo de roomName comecar com letra minuscula")
    public void testaSeExcecaoParaRoomNameMaiorQue30Caracteres () throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        ComodoDTO roomWithNameEmpyt = new ComodoDTO();
        roomWithNameEmpyt.setRoomWidth(13D);
        roomWithNameEmpyt.setRoomLength(13D);
        roomWithNameEmpyt.setRoomName("S234567890123456789012345678901");

        List<ComodoDTO> rooms = imovelDTO.getRooms();
        rooms.add(roomWithNameEmpyt);
        imovelDTO.setRooms(rooms);

        String payloadImovel = objectMapper.writeValueAsString(imovelDTO);

        // faz requisição
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payloadImovel))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        // transforma o resultado da requisição em string
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // desserializar, transforma a string em um objeto para manipulação.
        StandardException response = objectMapper.readValue(contentAsString, StandardException.class);

        Assertions.assertEquals(response.getMsg(), "O campo roomName (nome do comodo) nao pode exceder 30 caracteres.");
        Assertions.assertEquals(response.getStatus(), 400);
    }
}
