package br.com.b2w.swapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.b2w.swapi.dto.PlanetaRequestDTO;
import br.com.b2w.swapi.dto.PlanetaResponseDTO;
import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.service.PlanetaService;
import br.com.b2w.swapi.validation.SwapiValidationException;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class PlanetaController {

    @Autowired
    private PlanetaService planetService;
    
    private Logger log = Logger.getLogger(PlanetaController.class);

    /**
     * Endpoint para adicionar/criar um planeta (com nome, clima e terreno)
     * @param planetaRequest
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.POST)
    public HttpEntity<PlanetaResponseDTO> create(@Valid @RequestBody PlanetaRequestDTO planetaRequest, BindingResult bindingResult) {
    	PlanetaResponseDTO responseBody = new PlanetaResponseDTO();
    	
        try {
            if (bindingResult.hasErrors()) {
                log.error("Erros na requisicao do cliente: " + bindingResult.toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(bindingResult));
            }

            String planetaNome = "";

            if(!planetaRequest.getPlaneta().getNome().isEmpty())
                planetaNome = planetaRequest.getPlaneta().getNome();

            if(planetService.validarPlaneta(planetaNome)) {
                responseBody.setDescricao("Planeta " + planetaNome + " já existe!");
                log.error("Nao é permitido criar um planeta com este nome!");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
            } else {
                Planeta planeta = planetService.create(planetaRequest.getPlaneta());
                responseBody.setPlaneta(planeta);
                log.info("Planeta adicionado com sucesso: " + planeta);

                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            }

        } catch (SwapiValidationException swapiException) {
            String error = String.valueOf(System.nanoTime());

            if(swapiException.getHttpStatus() == HttpStatus.NOT_FOUND) {
            	responseBody.setDescricao("Não é possível adicionar um planeta inexistente na API Star Wars");
            } else {
            	responseBody.setDescricao("Houve um erro na API Star Wars ao consultar a existência do planeta para cadastro");
            }
            log.error("Erro na SWAPI ao cadastrar o planeta: " + error + " - " + swapiException.getMessage()  + " - " + swapiException);

            return ResponseEntity.status(swapiException.getHttpStatus()).body(responseBody);

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro no cadastro do planeta: " + error + " - " + e.getMessage() + " - " + e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para listar os planetas cadastrados
     * @return HttpEntity<PlanetResponseBody> planeta
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseDTO> findAll() {
        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();

        try {
            List<Planeta> planetas = planetService.findAll();

            if (!planetas.isEmpty()) {
                responseBody.setPlanetas(planetas);
                log.info("Buscando todos os planetas!");
                
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                responseBody.setDescricao("Nenhum planeta foi encontrado");
                log.error("Nenhum planeta foi encontrado");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro ao listar os planetas cadastrados: " + error + " - " + e.getMessage() + " - " + e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para buscar um planeta pelo nome
     * @param nome
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas/", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseDTO> findByName(@RequestParam("nome") String nome) {
        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();

        try {
            Planeta planeta = planetService.findByNome(nome);

            if (planeta != null ? true : false) {
                responseBody.setPlaneta(planeta);
                log.info("Buscando planeta pelo nome: " + planeta);

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                responseBody.setDescricao("Nenhum planeta encontrado");
                log.error("Nenhum planeta com o nome: " + nome + " foi encontrado!");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro na exibição do Planeta: " + nome + " - " + error + " - " + e.getMessage() + " - " + e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para buscar um planeta pelo id
     * @param id
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseDTO> findById(@PathVariable Long id) {
        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();

        try {
            Planeta planeta = planetService.findById(id);

            if (planeta != null ? true : false) {
                responseBody.setPlaneta(planeta);
                log.info("Buscando planeta: " + planeta);
                
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                responseBody.setDescricao("Nenhum planeta encontrado");
                log.error("Nenhum planeta encontrado pelo id: " + id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro na exibição do Planeta solicitado com o ID: " + id + " - " + error + " - " + e.getMessage() + " - " + e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para deletar um planeta pelo id
     * @param id
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.DELETE)
    public HttpEntity<PlanetaResponseDTO> deleteById(@PathVariable Long id) {
        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();

        try {
           Planeta planeta = planetService.findById(id);

            if (planeta != null ? true : false) {
                planetService.delete(planeta);
                responseBody.setDescricao("Planeta com ID: " + id + " deletado com sucesso!");
                log.info("Planeta com ID: " + id + " deletado com sucesso!");
                
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                responseBody.setDescricao("Nenhum planeta foi encontrado");
                log.error("Nenhum planeta com ID: " + id + " foi encontrado!");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro ao deletar o Planeta com ID: " + id + " - " + error + " - " + e.getMessage() + " - " + e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para deletar todos os planetas
     * @return
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.DELETE)
    public HttpEntity<PlanetaResponseDTO> deleteAll() {
        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();

        try {
            List<Planeta> planetas = planetService.findAll();

            if (!planetas.isEmpty()) {
                planetService.deleteAll();
                responseBody.setDescricao("Todos os planeta foram deletados com sucesso!");
                log.info("Todos os planetas foram deletados");
                
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                responseBody.setDescricao("Não existem planetas para deletar");
                log.error("Não existem planetas para deletar");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String error = String.valueOf(System.nanoTime());
            responseBody.setDescricao("Houve um erro interno no servidor: " + error);
            log.error("Erro ao deletar todos os Planetas: " + error + " - " + e.getMessage() + " - " + e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Coletando os erros de BindResult para setar ResponseBody
     * @param bindingResult
     * @return PlanetResponseBody
     */
    private PlanetaResponseDTO buildErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(
                        fieldError -> bindingResult.getFieldError(
                                fieldError.getField()
                        ).getDefaultMessage()
                )
                .collect(Collectors.toList());

        PlanetaResponseDTO responseBody = new PlanetaResponseDTO();
        responseBody.setDescricao(errors.toString());
        
        return responseBody;
    }
    
}
