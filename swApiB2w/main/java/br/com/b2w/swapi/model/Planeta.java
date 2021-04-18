package br.com.b2w.swapi.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Planeta")
public class Planeta {

    @Id
    private long id;

    @Indexed(unique = true)
    @NotNull(message = "O nome é obrigatório!")
    private String nome;

    @NotNull(message = "O clima é obrigatório!")
    private String clima;

    @NotNull(message = "O terreno é obrigatório!")
    private String terreno;

    private Integer filmes;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return clima;
    }
    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }
    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public Integer getFilmes() {
        return filmes;
    }
    public void setFilmes(Integer filmes) {
        this.filmes = filmes;
    }    
    
    public Planeta() {
	}
    
    public Planeta(String nome) {
		this.nome = nome;
	}
    
	public Planeta(long id, String nome, String clima, String terreno, Integer filmes) {
		this.id = id;
		this.nome = nome;
		this.clima = clima;
		this.terreno = terreno;
		this.filmes = filmes;
	}
}
