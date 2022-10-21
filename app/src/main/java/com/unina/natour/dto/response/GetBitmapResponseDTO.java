package com.unina.natour.dto.response;

import android.graphics.Bitmap;

public class GetBitmapResponseDTO {

    private ResultMessageDTO resultMessage;

    private Bitmap bitmap;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
