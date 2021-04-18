package br.com.b2w.swapi.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.b2w.swapi.SwApiB2w;
import br.com.b2w.swapi.dto.PlanetaResponseDTO;
import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.model.SwApi;
import br.com.b2w.swapi.repository.PlanetaRepository;
import br.com.b2w.swapi.service.PlanetaService;
import br.com.b2w.swapi.service.SwApiService;
import br.com.b2w.swapi.validation.SwapiValidationException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SwApiB2w.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
public class PlanetaControllerSwApiMockedPlanetServiceTests {

    @Autowired
    PlanetaRepository planetRepository;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @MockBean
    private SwApiService swApiServiceMocked;

    @MockBean
    private PlanetaService planetServiceMocked;
    
    private List<Planeta> planetas;

    @Before
    public void init() {
        planetRepository.deleteAll();
        
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
    
    public static String getJsonCadastroPlaneta(Planeta planeta) {
        String jsonPlaneta = "{  \n" +
                "   \"planeta\":{  \n" +
                "      \"nome\":\""+ planeta.getNome() +"\",\n" +
                "      \"clima\": \""+ planeta.getClima() +"\",\n" +
                "      \"terreno\": \""+ planeta.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }
    
    public static String getJsonCadastroPlanetaBadRequest(Planeta planeta) {
        String jsonPlaneta = "{  \n" +
                "   \"planeta\":{  \n" +
                "      \"nome\":\""+ planeta.getNome() +"\",\n" +
                "      \"climas\": \""+ planeta.getClima() +"\",\n" +
                "      \"terreno\": \""+ planeta.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static SwApi getSwapiPlanetDeUmPlaneta(Planeta planeta) {
        List<Object> filmes = new ArrayList<>();
        filmes.add("filme 1");
        filmes.add("filme 2");
        filmes.add("filme 3");

        SwApi swapiPlanet = new SwApi();
        swapiPlanet.setName(planeta.getNome());
        swapiPlanet.setClimate(planeta.getClima());
        swapiPlanet.setTerrain(planeta.getTerreno());
        swapiPlanet.setFilms(filmes);

        return swapiPlanet;
    }

    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {
        Mockito.when(planetServiceMocked.validarPlaneta(Mockito.any())).thenReturn(true);
        
        String planetaJson = getJsonCadastroPlaneta(planetas.get(0));

        RequestEntity<String> entity = RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.CONFLICT, exchange.getStatusCode());
        Assert.assertEquals("Planeta " + planetas.get(0).getNome() + " já existe!", exchange.getBody().getDescricao());
    }

    @Test
    public void cadastrarPlanetaComErroInterno() throws URISyntaxException, SwapiValidationException {
        Mockito.when(planetServiceMocked.validarPlaneta(Mockito.any())).thenReturn(false);
        Mockito.when(planetServiceMocked.create(Mockito.any())).thenThrow(new RuntimeException());
        
        String planetaJson = getJsonCadastroPlaneta(planetas.get(0));

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
    public void listarPlanetasComErroInterno() throws URISyntaxException, SwapiValidationException {
        Mockito.when(planetServiceMocked.findAll()).thenThrow(new RuntimeException());

        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorNomeComErroInterno() throws URISyntaxException {
        Mockito.when(planetServiceMocked.findByNome(Mockito.any())).thenThrow(new RuntimeException());

        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorIdComErroInterno() throws URISyntaxException {
        Mockito.when(planetServiceMocked.findById(Mockito.any())).thenThrow(new RuntimeException());

        RequestEntity<Void> entity = RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void deletarPlanetaPorIdComErroInterno() throws URISyntaxException {
        Mockito.when(planetServiceMocked.findById(Mockito.any())).thenThrow(new RuntimeException());

        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void deletarTodosOsPlanetasComErroInterno() throws URISyntaxException {
        Mockito.when(planetServiceMocked.findAll()).thenThrow(new RuntimeException());

        RequestEntity<Void> entity = RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }
    
    @Test
    public void deletarPlanetaPeloIdComStatusNotFound() throws URISyntaxException {
    	Mockito.when(planetServiceMocked.findById(Mockito.any())).thenReturn(null);
    	
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
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescricao());
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
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescricao());
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
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescricao());
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
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescricao());
    }

    @Test
    public void cadastrarPlanetaComSwapiPlanetServiceMocked() throws
	    					SwapiValidationException, URISyntaxException {
    	Planeta planeta = planetas.get(0);
		SwApi swapiPlaneta = getSwapiPlanetDeUmPlaneta(planeta);
		Mockito.when(swApiServiceMocked.getPlanetaInSwapiByName(Mockito.any())).thenReturn(swapiPlaneta);
		String planetaJson = getJsonCadastroPlaneta(planeta);
		
		RequestEntity<String> entity = RequestEntity
		        .post(new URI("/planetas"))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .body(planetaJson);
		
		ResponseEntity<PlanetaResponseDTO> exchange = restTemplate.exchange(entity, PlanetaResponseDTO.class);
		
		Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
		Assert.assertNotNull(exchange);
	}

}
