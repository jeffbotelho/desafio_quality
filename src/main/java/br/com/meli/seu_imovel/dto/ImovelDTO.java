package br.com.meli.seu_imovel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/*
 * formata os dados do imovel, sendo utilizado para receber a requisição como para resposta
 * @Author Rogério Lambert
 * */

@Data
@NoArgsConstructor
public class ImovelDTO {
    @NotBlank(message = "O nome da propriedade não pode estar vazio. (Nome)")
    @Pattern(regexp=
            "[A-ZÀ-Ú][A-z À-ú0-9]*",
            message = "O nome da propriedade deve começar com uma letra maiúscula."
    )
    @Size(max = 30,message = "O comprimento do nome não pode exceder 30 caracteres.")
    private String propName;
    @NotBlank(message = "O bairro não pode estar vazio. (Distrito)")
    @Size(max = 45,message = "O comprimento do bairro não pode exceder 45 caracteres.")
    private String propDistrict;
    @NotNull(message = "O valor do metro quadrado não pode estar vazio. (Valor do Distrito)")
    @Digits(integer = 11, fraction = 2, message = "O valor do metro quadrado nao pode ser maior que 13 digitos. (Valor Distrito)")
    private BigDecimal valueDistrictM2;
    @NotEmpty
    @Valid
    private List<ComodoDTO> rooms;

    private ComodoDTO bigestRoom;
    private Double totalArea;
    private BigDecimal totalPrice;

}
