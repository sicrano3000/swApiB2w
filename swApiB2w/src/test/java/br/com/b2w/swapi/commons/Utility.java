package br.com.b2w.swapi.commons;

import org.springframework.stereotype.Service;

import br.com.b2w.swapi.model.Planeta;
import br.com.b2w.swapi.model.SwApi;

import java.util.ArrayList;
import java.util.List;

@Service
public class Utility {

    public static List<Planeta> getPlanetasDefault() {
        List<Planeta> planets = new ArrayList<>();

        Planeta planet1 = new Planeta();
        planet1.setNome("Tatooine");
        planet1.setClima("arid");
        planet1.setTerreno("desert");

        Planeta planet2 = new Planeta();
        planet2.setNome("Alderaan");
        planet2.setClima("temperate");
        planet2.setTerreno("grasslands, mountains");

        Planeta planet3 = new Planeta();
        planet3.setNome("Yavin IV");
        planet3.setClima("temperate, tropical");
        planet3.setTerreno("jungle, rainforests");

        planets.add(planet1);
        planets.add(planet2);
        planets.add(planet3);

        return planets;
    }

    public static String getJsonCadastroPlaneta(Planeta planet) {
        String jsonPlaneta = "{  \n" +
                "   \"planet\":{  \n" +
                "      \"nome\":\""+ planet.getNome() +"\",\n" +
                "      \"clima\": \""+ planet.getClima() +"\",\n" +
                "      \"terreno\": \""+ planet.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static String getJsonCadastroPlanetaBadRequest(Planeta planet) {
        String jsonPlaneta = "{  \n" +
                "   \"planet\":{  \n" +
                "      \"nome\":\""+ planet.getNome() +"\",\n" +
                "      \"climas\": \""+ planet.getClima() +"\",\n" +
                "      \"terreno\": \""+ planet.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static SwApi getSwapiPlanetDeUmPlaneta(Planeta planet) {
        List<Object> filmes = new ArrayList<>();
        filmes.add("filme 1");
        filmes.add("filme 2");
        filmes.add("filme 3");

        SwApi swapiPlanet = new SwApi();
        swapiPlanet.setName(planet.getNome());
        swapiPlanet.setClimate(planet.getClima());
        swapiPlanet.setTerrain(planet.getTerreno());
        swapiPlanet.setFilms(filmes);

        return swapiPlanet;
    }
}
