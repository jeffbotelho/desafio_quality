package br.com.meli.seu_imovel.repositories;

import java.util.Optional;

public interface BairroRepository {
    Optional<String> findByName(String name);
}
