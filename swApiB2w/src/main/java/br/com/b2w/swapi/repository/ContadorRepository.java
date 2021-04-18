package br.com.b2w.swapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.b2w.swapi.model.Contador;

public interface ContadorRepository extends MongoRepository<Contador, String> {

}
