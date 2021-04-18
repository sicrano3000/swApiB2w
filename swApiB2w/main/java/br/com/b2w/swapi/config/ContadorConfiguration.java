package br.com.b2w.swapi.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.b2w.swapi.service.ContadorService;

@Configuration
public class ContadorConfiguration {

    @Autowired
    private ContadorService contadorService;

    @PostConstruct
    private void configureContador() {
        if(!contadorService.collectionExists("Contador")) {
        	contadorService.createCollection();
        }
    }
    
}
