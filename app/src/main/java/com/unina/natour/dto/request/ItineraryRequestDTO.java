package com.unina.natour.dto.request;

import java.io.File;

import io.jenetics.jpx.GPX;

public class ItineraryRequestDTO {

    private String name;
    private GPX gpx;
    private Float duration;
    private Float lenght;
    private Integer difficulty;
    private String description;


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
}
