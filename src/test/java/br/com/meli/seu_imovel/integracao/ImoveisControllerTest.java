package br.com.meli.seu_imovel.integracao;

import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.exceptions.StandardException;
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
/*
@author Todos do grupo
 */
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
// __________________________________________________________________
    /*
    @author Gabriel
     */
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
    /*
   @author Gabriel
    */
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
    // __________________________________________________________________
/*
@author Nayara
 */
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("O comprimento não pode estar vazio. (Comprimento do quarto)"));

    }
    /*
    @author Nayara
     */
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
                        .value("O comprimento máximo permitido por cômodo é de 33 metros."));
    }

    // __________________________________________________________________

    /*
     * @author Weverton Bruno
     * */
    @Test
    @DisplayName("Deve retornar uma exceção quando a largura não for informada")
    public void itShouldReturnAValidationExceptionOfNotNull() throws Exception{
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.getRooms().get(0).setRoomWidth(null);

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("A largura não pode estar vazia. (Largura do Quarto)"));
    }

    /*
     * @author Weverton Bruno
     * */
    @Test
    @DisplayName("Deve retornar uma exceção quando a largura for superior a 25")
    public void itShouldReturnAValidationExceptionWhenWidthIsGratherThan25() throws Exception{
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.getRooms().get(0).setRoomWidth(26D);

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("A largura máxima permitida por cômodo é de 25 metros."));
    }

    /*
     * @author Weverton Bruno
     * */
    @Test
    @DisplayName("Deve retornar uma exceção o bairro não for encontrado no repositório")
    public void itShouldReturnAValidationExceptionWhenDistrictNotExists() throws Exception{
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setPropDistrict("Centro");

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("Distrito não válido!"));
    }
    // __________________________________________________________________

/*
@author Ederson
 */

    @Test
    @DisplayName("Testando propName com primeira letra minuscula.")
    public void testandoPropNomeDoImovelComLetraMinuscula() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setPropName("imovel");

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("O nome da propriedade deve começar com uma letra maiúscula."));
    }
    /*
    @author Ederson
     */
    @Test
    @DisplayName("Testando propname com null.")
    public void testandoPropNomeDoImovelComNull() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setPropName(null);

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("O nome da propriedade não pode estar vazio. (Nome)"));
    }
    /*
    @author Ederson
     */
    @Test
    @DisplayName("Testando propname com mais de 30 caracteres.")
    public void testandoPropNomeDoImovelCom31Caracteres() throws Exception {
        ImovelDTO imovelDTO = geraImovelTeste();
        imovelDTO.setPropName("Iiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

        String payload = objectMapper.writeValueAsString(imovelDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/relatorio-de-imovel")
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("O comprimento do nome não pode exceder 30 caracteres."));
    }

    // __________________________________________________________________

/*
@author Jefferson
 */
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
    /*
    @author Jefferson
     */
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

    // __________________________________________________________________

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
