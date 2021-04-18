package br.com.b2w.swapi.controller;

import br.com.b2w.swapi.SwApiB2w;
import br.com.b2w.swapi.commons.Utility;
import br.com.b2w.swapi.dto.PlanetaResponseDTO;
import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.repository.PlanetaRepository;
import br.com.b2w.swapi.validation.SwapiValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SwApiB2w.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PlanetaControllerTests {

    @Autowired
    PlanetaRepository planetRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        planetRepository.deleteAll();
    }

    @Test
    public void cadastrarPlanetaComErroPorBadRequest() throws URISyntaxException {
        List<Planeta> planetas = Utility.getPlanetasDefault();
        Planeta planeta = planetas.get(0);
        String planetaJson = Utility.getJsonCadastroPlanetaBadRequest(planeta);

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void listandoPlanetasSemNenhumCadastradoComStatusNotFound() throws URISyntaxException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchange.getBody().getDescricao());
    }

    @Test
    public void buscarPlanetaPorNomeComNomeVazioComStatusNotFound() throws URISyntaxException, SwapiValidationException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/?nome="))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchangeBusca = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchangeBusca.getBody().getDescricao());
    }

    @Test
    public void buscarPlanetaPorNomeComStatusNotFound() throws URISyntaxException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchange.getBody().getDescricao());
    }

    @Test
    public void buscarPlanetaPorIDComIdZeroComStatusNotFound() throws URISyntaxException, SwapiValidationException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/0"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchangeBusca = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchangeBusca.getBody().getDescricao());
    }

    @Test
    public void buscarPlanetaPorIdComStatusNotFound() throws URISyntaxException {
        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchange.getBody().getDescricao());
    }

    @Test
    public void deletarPlanetaPeloIdComStatusNotFound() throws URISyntaxException {
        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas/99"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta foi encontrado", exchange.getBody().getDescricao());
    }

    @Test
    public void deletarTodosOsPlanetasComStatusNotFound() throws URISyntaxException {
        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Não existem planetas para deletar", exchange.getBody().getDescricao());
    }

}
