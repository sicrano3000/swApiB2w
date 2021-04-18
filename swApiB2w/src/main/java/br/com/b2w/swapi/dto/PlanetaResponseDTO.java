package br.com.b2w.swapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.b2w.swapi.model.Planeta;

@JsonInclude(Include.NON_NULL)
public class PlanetaResponseDTO {

    private String descricao;

    private List<Planeta> planetas;

    private Planeta planeta;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Planeta> getPlanetas() {
		return planetas;
	}

	public void setPlanetas(List<Planeta> planetas) {
		this.planetas = planetas;
	}

	public Planeta getPlaneta() {
		return planeta;
	}

	public void setPlaneta(Planeta planeta) {
		this.planeta = planeta;
	}

	public PlanetaResponseDTO() {
	}
	
	public PlanetaResponseDTO(Planeta planeta) {
		this.planeta = planeta;
	}

	public PlanetaResponseDTO(String descricao, List<Planeta> planetas, Planeta planeta) {
		this.descricao = descricao;
		this.planetas = planetas;
		this.planeta = planeta;
	}
	
}
