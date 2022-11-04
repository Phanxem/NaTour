package com.unina.natour.dto.response.composted;

import android.graphics.Bitmap;

import com.unina.natour.dto.response.ResultMessageDTO;

import io.jenetics.jpx.GPX;

public class GetItineraryWithGpxAndUserAndReportResponseDTO {

    private ResultMessageDTO resultMessage;

    private long id;
    private String name;
    private Float duration;
    private Float lenght;
    private Integer difficulty;
    private String description;

    private long idUser;
    private String username;
    private Bitmap profileImage;

    private GPX gpx;
    private boolean isReported;

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

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

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
