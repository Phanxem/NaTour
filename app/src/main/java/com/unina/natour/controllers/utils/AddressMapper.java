package com.unina.natour.controllers.utils;

import com.unina.natour.dto.response.GetListAddressResponseDTO;
import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.models.AddressModel;

import java.util.List;

public class AddressMapper {

    public static boolean dtoToModel(GetAddressResponseDTO dto, AddressModel model){
        model.clear();

        model.setAddressName(dto.getAddressName());
        model.setPoint(dto.getPoint());

        return true;
    }

    public static boolean dtoToModel(GetListAddressResponseDTO dto, List<AddressModel> model){
        model.clear();

        List<GetAddressResponseDTO> dtos = dto.getListAddress();

        for(GetAddressResponseDTO elementDto : dtos){
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
