package com.unina.natour.dto.response;

import org.osmdroid.util.GeoPoint;

public class AddressResponseDTO {

    private MessageResponseDTO resultMessage;

    private GeoPoint point;
    private String addressName;

    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public AddressResponseDTO(){
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
