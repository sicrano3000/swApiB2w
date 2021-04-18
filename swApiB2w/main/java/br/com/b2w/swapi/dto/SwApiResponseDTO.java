package br.com.b2w.swapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.b2w.swapi.model.SwApi;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwApiResponseDTO {

    public Integer count;
    public Object next;
    public Object previous;

    public List<SwApi> results;

    public SwApiResponseDTO() {
    }

    public SwApiResponseDTO(Integer count, Object next, Object previous, List<SwApi> results) {
		this.count = count;
		this.next = next;
		this.previous = previous;
		this.results = results;
	}

	public List<SwApi> getResults() {
        return results;
    }

    public void setResults(List<SwApi> results) {
        this.results = results;
    }

}
