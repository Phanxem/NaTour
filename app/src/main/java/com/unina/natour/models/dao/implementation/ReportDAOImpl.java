package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.request.ReportRequestDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.ReportListResponseDTO;
import com.unina.natour.dto.response.ReportResponseDTO;
import com.unina.natour.models.dao.interfaces.ReportDAO;

import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl extends ServerDAO  implements ReportDAO {

    @Override
    public MessageResponseDTO addReport(ReportRequestDTO reportRequestDTO) {
        Log.i("TAG","segnalazione effettuata");
        return null;
    }

    @Override
    public ReportListResponseDTO findByItineraryId(long itineraryId) {
        Log.i("TAG","itinerari recuperati");

        List<ReportResponseDTO> test = new ArrayList<ReportResponseDTO>();

        ReportResponseDTO test1 = new ReportResponseDTO();
        test1.setId(1);
        test1.setName("jennifer");
        test1.setDescription("pyro");
        test1.setDateOfInput("12/12/22");
        test1.setId_user(23);
        test1.setId_itinerary(3);

        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);
        test.add(test1);

        ReportListResponseDTO reportListResponseDTO = new ReportListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();

        reportListResponseDTO.setResultMessage(messageResponseDTO);
        reportListResponseDTO.setReports(test);

        return reportListResponseDTO;
    }

    @Override
    public ReportResponseDTO findById(long reportId) {

        ReportResponseDTO test = new ReportResponseDTO();
        test.setId(15);
        test.setName("jdfsdfsennifer");
        test.setDescription("pyrfgdfgo");
        test.setDateOfInput("11/11/21");
        test.setId_user(23);
        test.setId_itinerary(6);

        return test;
    }

    @Override
    public MessageResponseDTO deleteById(long reportId) {
        Log.i("TAG","segnalazione rimossa");
        return null;
    }
}
