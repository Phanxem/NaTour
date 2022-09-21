package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class AddressListResponseDTO {

    private MessageResponseDTO resultMessage;
    private List<AddressResponseDTO> addresses;

    public AddressListResponseDTO(){
        this.addresses = new ArrayList<AddressResponseDTO>();
    }

    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<AddressResponseDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressResponseDTO> addresses) {
        this.addresses = addresses;
    }
}
