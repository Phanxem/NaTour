package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.request.SaveReportRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetListReportResponseDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;
import com.unina.natour.models.dao.interfaces.ReportDAO;

import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl extends ServerDAO  implements ReportDAO {

    @Override
    public ResultMessageDTO addReport(SaveReportRequestDTO saveReportRequestDTO) {
        Log.i("TAG","segnalazione effettuata");
        return null;
    }

    @Override
    public GetListReportResponseDTO getReportByIdItinerary(long idItinerary) {
        Log.i("TAG","itinerari recuperati");

        List<GetReportResponseDTO> test = new ArrayList<GetReportResponseDTO>();

        GetReportResponseDTO test1 = new GetReportResponseDTO();
        test1.setId(1);
        test1.setName("jennifer");
        test1.setDescription("pyro");
        test1.setDateOfInput("12/12/22");
        test1.setIdUser(23);
        test1.setIdItinerary(3);

        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);

        GetListReportResponseDTO getListReportResponseDTO = new GetListReportResponseDTO();
        ResultMessageDTO resultMessageDTO = MessageController.getSuccessMessage();

        getListReportResponseDTO.setResultMessage(resultMessageDTO);
        getListReportResponseDTO.setListReport(test);

        return getListReportResponseDTO;
    }

    @Override
    public GetReportResponseDTO getReportById(long idReport) {

        GetReportResponseDTO test = new GetReportResponseDTO();
        test.setId(15);
        test.setName("jdfsdfsennifer");
        test.setDescription("pyrfgdfgo");
        test.setDateOfInput("11/11/21");
        test.setIdUser(23);
        test.setIdItinerary(6);

        return test;
    }

    @Override
    public ResultMessageDTO deleteReportById(long reportId) {
        Log.i("TAG","segnalazione rimossa");
        return null;
    }
}
