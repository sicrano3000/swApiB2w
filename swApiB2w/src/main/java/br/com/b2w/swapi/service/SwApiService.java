package br.com.b2w.swapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.b2w.swapi.dto.SwApiResponseDTO;
import br.com.b2w.swapi.model.SwApi;
import br.com.b2w.swapi.validation.SwapiValidationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@PropertySource("classpath:application.properties")
public class SwApiService {
	private final static String INTERNAL_ERROR = "Ocorreu um erro interno ao consultar a API Star Wars";
	private final static String NOT_FOUND_ERROR = "Planeta inexistente na API Star Wars";

    @Value("${swapi.base.url}")
    private String swapiBaseUrl;

    @Value("${swapi.resource}")
    private String swapiResource;

    @Value("${swapi.resource.search}")
    private String swapiResourceSearch;

    /**
     * Retorna a URL de pesquisa de um planeta pelo nome em Star Wars API
     * @return String
     */
    private String getSwapiUrlResourceSearch() {
        return this.swapiBaseUrl + this.swapiResource + this.swapiResourceSearch;
    }

    /**
     * Verifica em SWAPI a Existencia de um planeta pelo nome e retorna sua instancia
     * @param name
     * @return SwapiPlanet
     * @throws SwapiValidationException
     */
    public SwApi getPlanetaInSwapiByName(String name) throws SwapiValidationException {
        String url = this.getSwapiUrlResourceSearch() + name;
        SwApi swApi;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                SwApiResponseDTO swApiResponseDTO = mapper.readValue(response.body().bytes(), SwApiResponseDTO.class);

                if(swApiResponseDTO.count > 0) {
                    List<SwApi> swApiList = swApiResponseDTO.getResults();
                    swApi = swApiList.get(0);

                    return swApi;
                } else {
                    throw new SwapiValidationException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR);
                }
            } else {
                throw new SwapiValidationException(
                        HttpStatus.valueOf(response.code()), response.message()
                );
            }

        } catch (SwapiValidationException SwApiException) {
            throw new SwapiValidationException(
                    SwApiException.getHttpStatus(), SwApiException.getMessage(), SwApiException
            );
        }  catch (Exception exception) {
            throw new SwapiValidationException(
                    HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, exception
            );
        }
    }

}
