package br.com.b2w.swapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.model.SwApi;
import br.com.b2w.swapi.repository.PlanetaRepository;
import br.com.b2w.swapi.validation.SwapiValidationException;

@Service
public class PlanetaService {

    @Autowired
    private PlanetaRepository repository;

    @Autowired
    private SwApiService swApiService;

    @Autowired
    private ContadorService contadorService;

    /**
     * Retorna uma lista com todos os planetas cadastrados
     * @return List<Planet> planetas
     */
    public List<Planeta> findAll() {
        return repository.findAll();
    }

    /**
     * Retorna um planeta, buscando pelo ID
     * @param id
     * @return Optional<Planet> planeta
     */
    public Planeta findById(Long id) {
    	Planeta planeta = repository.findById(id);
    	
        return planeta;
    }

    /**
     * Retorna um planeta, buscando pelo Nome
     * @param nome
     * @return
     */
    public Planeta findByNome(String nome) {
    	Planeta planeta = repository.findByNome(nome);
    	
    	if (planeta == null)
    		planeta = new Planeta("error");
    	
        return planeta;
    }

    /**
     * Adicionar um novo planeta (com nome, clima e terreno)
     * @param planetRequest
     * @return Planet planeta
     */
    public Planeta create(Planeta planeta) throws SwapiValidationException {
        planeta.setFilmes(this.aparicoesEmFilmesTotal(planeta.getNome()));
        planeta.setId(contadorService.getNextSequence("Contador"));

        return repository.save(planeta);
    }

    /**
     * Verifica se existe algum Planeta criado com o nome passado no parametro
     * @param planetaNome
     * @return boolean
     */
    public boolean validarPlaneta(String planetaNome) {
        Planeta planeta = this.findByNome(planetaNome);
        
        if (planeta.getNome() == null)
        	return false;
        else
        	return true;
    }

    /**
     * Deleta um o Planeta
     * @param planet
     */
    public void delete(Planeta planet) {
    	repository.delete(planet);
    }

    /**
     * Consulta a API SWApi e retorna o Total de aparições do planeta nos filmes
     * @param nome
     * @return Integer
     * @throws SwapiValidationException
     */
    public Integer aparicoesEmFilmesTotal(String nome) throws SwapiValidationException {
        SwApi swapiPlanet = swApiService.getPlanetaInSwapiByName(nome);

        return swapiPlanet.getFilms().size();
    }

    /**
     * Deleta todos os planetas
     */
    public void deleteAll() {
    	repository.deleteAll();
    }
}
