package br.com.b2w.swapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.b2w.swapi.model.Planeta;

public interface PlanetaRepository extends MongoRepository<Planeta, String> {

    Planeta findByNome(String nome);

    Planeta findById(long id);

}
