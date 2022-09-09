package com.unina.natour.dto.response;

import io.jenetics.jpx.GPX;

public class ItineraryResponseDTO {

    private long id;
    private String name;
    private GPX gpx;
    private Float duration;
    private Float lenght;
    private Integer difficulty;
    private String description;

    private boolean hasBeenReported;

    private long id_user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public Float getLenght() {
        return lenght;
    }

    public void setLenght(Float lenght) {
        this.lenght = lenght;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getHasBeenReported() {
        return hasBeenReported;
    }

    public void setHasBeenReported(boolean hasBeenReported) {
        this.hasBeenReported = hasBeenReported;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }
}
