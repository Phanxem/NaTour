package com.unina.natour.dto.response;

import org.osmdroid.util.GeoPoint;

public class GetAddressResponseDTO {

    private ResultMessageDTO resultMessage;

    private GeoPoint point;
    private String addressName;

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public GetAddressResponseDTO(){
        super();
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

}
