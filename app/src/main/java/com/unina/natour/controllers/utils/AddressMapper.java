package com.unina.natour.controllers.utils;

import com.unina.natour.dto.response.AddressListResponseDTO;
import com.unina.natour.dto.response.AddressResponseDTO;
import com.unina.natour.models.AddressModel;

import java.util.List;

public class AddressMapper {

    public static boolean dtoToModel(AddressResponseDTO dto, AddressModel model){
        model.clear();

        model.setAddressName(dto.getAddressName());
        model.setPoint(dto.getPoint());

        return true;
    }

    public static boolean dtoToModel(AddressListResponseDTO dto, List<AddressModel> model){
        model.clear();

        List<AddressResponseDTO> dtos = dto.getAddresses();

        for(AddressResponseDTO elementDto : dtos){
            AddressModel elementModel = new AddressModel();
            boolean result = dtoToModel(elementDto, elementModel);
            if(!result){
                //todo errro
                return false;
            }

            model.add(elementModel);
        }

        return true;
    }
}
