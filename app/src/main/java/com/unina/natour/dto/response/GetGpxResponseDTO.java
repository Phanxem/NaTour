package com.unina.natour.dto.response;

import android.graphics.Bitmap;

import io.jenetics.jpx.GPX;

public class GetGpxResponseDTO {

    private ResultMessageDTO resultMessage;

    private GPX gpx;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }
}
