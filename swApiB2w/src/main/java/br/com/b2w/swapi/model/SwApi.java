package br.com.b2w.swapi.model;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class SwApi {

    /**
     * Um número denotando a gravidade deste planeta. Onde 1 é normal.
     * (Obrigatório)
     */
    public String gravity;
    
    /**
     * O terreno deste planeta. Separados por vírgulas, se diversos.
     * (Obrigatório)
     */
    public String terrain;
    
    /**
     * O formato de data ISO 8601 da hora em que este recurso foi criado.
     * (Obrigatório)
     */
    public Date created;
    
    /**
     * Uma série de recursos de URL de pessoas que vivem neste planeta.
     * (Obrigatório)
     */
    public List<Object> residents = null;
    
    /**
     * A porcentagem da superfície do planeta que é formada por água ou corpos d'água que ocorrem naturalmente.
     * (Obrigatório)
     */
    public String surface_water;
    
    /**
     * O formato de data ISO 8601 da hora em que este recurso foi editado.
     * (Obrigatório)
     */
    public Date edited;
    
    /**
     * Uma série de recursos de URL de filme em que este planeta apareceu.
     * (Obrigatório)
     */
    public List<Object> films = null;
    
    /**
     * O clima deste planeta. Separados por vírgulas, se diversos.
     * (Obrigatório)
     */
    public String climate;
    
    /**
     * O nome deste planeta.
     * (Obrigatório)
     */
    public String name;
    
    /**
     * O diâmetro deste planeta em quilômetros.
     * (Obrigatório)
     */
    public String diameter;
    
    /**
     * A população média de seres sencientes que habitam este planeta.
     * (Obrigatório)
     */
    public String population;
    
    /**
     * O número de horas padrão que este planeta leva para completar uma única rotação em seu eixo.
     * (Obrigatório)
     */
    public String rotation_period;
    
    /**
     * A URL hipermídia deste recurso.
     * (Obrigatório)
     */
    public URI url;
    
    /**
     * O número de dias padrão que este planeta leva para completar uma única órbita de sua estrela local.
     * (Obrigatório)
     */
    public String orbital_period;

    public SwApi() {}

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Object> getResidents() {
        return residents;
    }

    public void setResidents(List<Object> residents) {
        this.residents = residents;
    }

    public String getSurface_water() {
        return surface_water;
    }

    public void setSurface_water(String surface_water) {
        this.surface_water = surface_water;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public List<Object> getFilms() {
        return films;
    }

    public void setFilms(List<Object> films) {
        this.films = films;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getRotation_period() {
        return rotation_period;
    }

    public void setRotation_period(String rotation_period) {
        this.rotation_period = rotation_period;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getOrbital_period() {
        return orbital_period;
    }

    public void setOrbital_period(String orbital_period) {
        this.orbital_period = orbital_period;
    }
}
