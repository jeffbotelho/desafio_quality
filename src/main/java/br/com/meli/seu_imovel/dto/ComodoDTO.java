package br.com.meli.seu_imovel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/* Formata os dados de um comodo que compõem a lista de comodos do ImovelDTO
 *@Author Rogério Lambert
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComodoDTO {
    @NotBlank(message = "O campo não pode estar vazio. (Nome do quarto)")
    @Pattern(regexp=
            "[A-ZÀ-Ú][A-z À-ú0-9]*",
            message = "O campo deve começar com uma letra maiúscula. (Nome do quarto)"
    )
    @Size(max = 30,message = "O campo não pode exceder 30 caracteres. (Nome do quarto)")
    private String roomName;
    @NotNull(message = "A largura não pode estar vazia. (Largura do Quarto)")
    @Max(value = 25,message = "A largura máxima permitida por cômodo é de 25 metros.")
    private Double roomWidth;
    @NotNull(message = "O comprimento não pode estar vázio. (Comprimento do quarto)")
    @Max(value = 33,message = "O comprimento máximo permitido por cômodo é de 33 metros.")
    private Double roomLength;
    private Double area;
}
