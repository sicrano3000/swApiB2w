package br.com.b2w.swapi.dto;

import javax.validation.Valid;

import br.com.b2w.swapi.model.Planeta;

public class PlanetaRequestDTO {

	@Valid
    private Planeta planeta;

    public Planeta getPlaneta() {
        return planeta;
    }

    public void setPlaneta(Planeta planeta) {
        this.planeta = planeta;
    }
    
    public PlanetaRequestDTO() {
	}

	public PlanetaRequestDTO(Planeta planeta) {
		this.planeta = planeta;
	}
	
}
