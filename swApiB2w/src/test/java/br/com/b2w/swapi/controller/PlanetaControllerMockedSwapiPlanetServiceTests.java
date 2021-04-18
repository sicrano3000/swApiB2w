package br.com.b2w.swapi.controller;

import br.com.b2w.swapi.SwApiB2w;
import br.com.b2w.swapi.commons.Utility;
import br.com.b2w.swapi.dto.PlanetaResponseDTO;
import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.model.SwApi;
import br.com.b2w.swapi.repository.PlanetaRepository;
import br.com.b2w.swapi.service.SwApiService;
import br.com.b2w.swapi.validation.SwapiValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SwApiB2w.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PlanetaControllerMockedSwapiPlanetServiceTests {

    @Autowired
    PlanetaRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SwApiService swApiServiceMocked;
    
    private List<Planeta> planetas;

    @Before
    public void init() {
    	repository.deleteAll();
    	
    	planetas = new ArrayList<>();

        Planeta p1 = new Planeta();
        p1.setNome("Tatooine");
        p1.setClima("arid");
        p1.setTerreno("desert");

        Planeta p2 = new Planeta();
        p2.setNome("Alderaan");
        p2.setClima("temperate");
        p2.setTerreno("grasslands, mountains");

        Planeta p3 = new Planeta();
        p3.setNome("Yavin IV");
        p3.setClima("temperate, tropical");
        p3.setTerreno("jungle, rainforests");

        planetas.add(p1);
        planetas.add(p2);
        planetas.add(p3);
    }

    @Test
    public void cadastrarPlanetaComSucesso() throws URISyntaxException, SwapiValidationException {
        Planeta planeta = planetas.get(0);
        SwApi swapiPlaneta = Utility.getSwapiPlanetDeUmPlaneta(planeta);

        Mockito.when(
        		swApiServiceMocked.getPlanetaInSwapiByName(Mockito.any())
        ).thenReturn(swapiPlaneta);

        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertEquals(planeta.getNome(), exchange.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchange.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchange.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                java.util.Optional.ofNullable(swapiPlaneta.getFilms().size()),
                java.util.Optional.ofNullable(exchange.getBody().getPlaneta().getFilmes())
        );
    }

    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {
        Planeta planeta = planetas.get(0);
        this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        RequestEntity<String> entity2 = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Utility.getJsonCadastroPlaneta(planeta));

        ResponseEntity<PlanetaResponseDTO> exchange2 = restTemplate.exchange(entity2, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange2);
        Assert.assertEquals(HttpStatus.CONFLICT, exchange2.getStatusCode());
        Assert.assertEquals("Planeta já existente.", exchange2.getBody().getDescricao());

    }

    @Test
    public void cadastrarPlanetaComErroPorNomeNaoLocalizadoEmSWAPI() throws URISyntaxException, SwapiValidationException {
        Mockito.when(swApiServiceMocked.getPlanetaInSwapiByName(Mockito.any()))
        		.thenThrow(new SwapiValidationException(HttpStatus.NOT_FOUND, "Planeta inexistente na API Star Wars"));
        Planeta planeta = planetas.get(0);
        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());

    }

    @Test
    public void cadastrarPlanetaComErroPorErroInternoEmSWAPI() throws URISyntaxException, SwapiValidationException {
        Mockito.when(swApiServiceMocked.getPlanetaInSwapiByName(Mockito.any()))
        		.thenThrow(new SwapiValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Planeta inexistente na API Star Wars"));
        Planeta planeta = planetas.get(0);
        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
    }

    @Test
    public void cadastrar3PlanetasComSucessoListandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals(3, exchange.getBody().getPlanetas().size());

        for (Planeta planeta : exchange.getBody().getPlanetas()){
            Assert.assertNotNull(planeta.getId());
            Assert.assertNotNull(planeta.getNome());
            Assert.assertNotNull(planeta.getClima());
            Assert.assertNotNull(planeta.getTerreno());
            Assert.assertNotNull(planeta.getFilmes());
        }
    }

    @Test
    public void buscarPlanetaPorNomeComSucesso() throws URISyntaxException, SwapiValidationException {
        Planeta planeta = planetas.get(0);
        ResponseEntity<PlanetaResponseDTO> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/?nome="+ planeta.getNome()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchangeBusca = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getId(),
                exchangeBusca.getBody().getPlaneta().getId()
        );
        Assert.assertEquals(planeta.getNome(), exchangeBusca.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchangeBusca.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchangeBusca.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getFilmes(),
                exchangeBusca.getBody().getPlaneta().getFilmes()
        );
    }

    @Test
    public void deletarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {
        Planeta planeta = planetas.get(0);
        ResponseEntity<PlanetaResponseDTO> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas/"+exchangeCadastro.getBody().getPlaneta().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchangeBusca = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
    }

    @Test
    public void cadastrar3PlanetasComSucessoDeletandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {
        for (Planeta planeta : planetas)
            this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {
        Planeta planeta = planetas.get(0);
        ResponseEntity<PlanetaResponseDTO> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/"+exchangeCadastro.getBody().getPlaneta().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchangeBusca = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getId(),
                exchangeBusca.getBody().getPlaneta().getId()
        );
        Assert.assertEquals(planeta.getNome(), exchangeBusca.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchangeBusca.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchangeBusca.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getFilmes(),
                exchangeBusca.getBody().getPlaneta().getFilmes()
        );
    }

    private ResponseEntity<PlanetaResponseDTO> cadastrarPlanetaComSwapiPlanetServiceMocked(Planeta planeta) throws
            SwapiValidationException, URISyntaxException {
        SwApi swapiPlaneta = Utility.getSwapiPlanetDeUmPlaneta(planeta);
        Mockito.when(swApiServiceMocked.getPlanetaInSwapiByName(Mockito.any())).thenReturn(swapiPlaneta);
        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertNotNull(exchange.getBody().getPlaneta().getId());
        Assert.assertEquals(planeta.getNome(), exchange.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchange.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchange.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(3, exchange.getBody().getPlaneta().getFilmes().intValue());

        return exchange;
    }
    
}
