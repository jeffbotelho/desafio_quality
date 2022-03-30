package br.com.meli.seu_imovel.service;


import br.com.meli.seu_imovel.dto.ComodoDTO;
import br.com.meli.seu_imovel.dto.ImovelDTO;
import br.com.meli.seu_imovel.exceptions.DistrictNotExistExepection;
import br.com.meli.seu_imovel.repositories.BairroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ImoveisService {

    private BairroRepository bairroRepository;

    public ImoveisService(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    public ImovelDTO gerarRelatorio(ImovelDTO imovelDTO){

        Optional<String> district = bairroRepository.findByName(imovelDTO.getPropDistrict());

        if (district.isEmpty()) {
            throw new DistrictNotExistExepection("Distrito não válido!");
        }

        //Req04
        calcularArea(imovelDTO);

        //Req01
        Double calculaTamanho = calculaTamanhoPropriedade(imovelDTO.getRooms());
        imovelDTO.setTotalArea(calculaTamanho);

        //Req02
        BigDecimal calculaValorVizinhaca = calculaValorDeAcordoComVizinhaca(imovelDTO.getValueDistrictM2(), calculaTamanho);
        imovelDTO.setTotalPrice(calculaValorVizinhaca);

        //Req03
        ComodoDTO maiorComodo = determinaMaiorComodo(imovelDTO);
        imovelDTO.setBigestRoom(maiorComodo);

        return imovelDTO;
    }
    // Requisito 04 , determina a quantidade de metros em cada comodo
    private void calcularArea(ImovelDTO imovelDTO) {

        List<ComodoDTO> comodos = imovelDTO.getRooms();
        for (ComodoDTO c: comodos){
            c.setArea(c.getRoomWidth() * c.getRoomLength());
        }
    }
    // Requisito 01 , Calcula o total de metro de uma propriedade
    private Double calculaTamanhoPropriedade(List<ComodoDTO> listComodoDTO){
        return listComodoDTO.stream().reduce(0D,(acumulador, item) -> acumulador + item.getArea(), Double::sum);
    }

    // Requisito 02, Determina valor da propriedade de acordo com a vizinhaça
    private BigDecimal calculaValorDeAcordoComVizinhaca(BigDecimal valueDistrictM2, Double tamanhoTotal){
        return valueDistrictM2.multiply(BigDecimal.valueOf(tamanhoTotal));
    }
    // Requisito 03, Determina qual maior comodo
    private ComodoDTO determinaMaiorComodo(ImovelDTO imovelDTO){
       return imovelDTO.getRooms().stream().max((x,y)-> x.getArea().compareTo(y.getArea())).get();
    }
}
