package br.com.b2w.swapi.controller;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import br.com.b2w.swapi.dto.PlanetaRequestDTO;
import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.service.PlanetaService;

@RunWith(SpringRunner.class)
public class PlanetaControllerMockTest {
	
	@InjectMocks
	private PlanetaController controller;
	
	@Mock
    private PlanetaService planetService;
	
	@Mock
	private BindingResult bind;
    
	@Mock
    private Logger log = Logger.getLogger(PlanetaController.class);
	
	private PlanetaRequestDTO dto;
	
	private Planeta planeta;
	
	private List<Planeta> planetas;
	
	@Before
	public void getterAndSetterTest() {
		planetas = new ArrayList<Planeta>();
		dto = new PlanetaRequestDTO();
		planeta = new Planeta();
		
		planeta.setNome("Tatooine");
		planeta.setClima("arid");
		planeta.setTerreno("desert");
		
		dto.setPlaneta(planeta);
		
		planetas.add(planeta);
		
		when(bind.hasErrors()).thenReturn(false);
	}
	
	@Test
	public void cresteTest() {
		controller.create(dto, bind);
	}
	
	@Test
	public void findAllTest() {
		when(planetService.findAll()).thenReturn(null);
		controller.findAll();
		
		when(planetService.findAll()).thenReturn(planetas);
		controller.findAll();
	}
	
	@Test
	public void findByNameTest() {
		when(planetService.findByNome(anyString())).thenReturn(null);
		controller.findByName(anyString());
		
		when(planetService.findByNome(anyString())).thenReturn(planeta);
		controller.findByName(anyString());
	}
	
	@Test
	public void findByIdTest() {
		when(planetService.findById(anyLong())).thenReturn(null);
		controller.findById(anyLong());
		
		when(planetService.findById(anyLong())).thenReturn(planeta);
		controller.findById(anyLong());
	}
	
	@Test
	public void deleteByIdTest() {
		when(planetService.findById(anyLong())).thenReturn(null);
		controller.deleteById(anyLong());
		
		when(planetService.findById(anyLong())).thenReturn(planeta);
		controller.deleteById(anyLong());
	}
	
	@Test
	public void deleteAllTest() {
		when(planetService.findAll()).thenReturn(null);
		controller.deleteAll();
		
		when(planetService.findAll()).thenReturn(planetas);
		controller.deleteAll();
	}

}
