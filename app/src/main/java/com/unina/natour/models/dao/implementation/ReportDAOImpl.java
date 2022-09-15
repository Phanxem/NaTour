package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.ReportDTO;
import com.unina.natour.models.dao.interfaces.ReportDAO;

import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public MessageDTO addReport(ReportDTO reportDTO) {
        Log.i("TAG","segnalazione effettuata");
        return null;
    }

    @Override
    public List<ReportDTO> findByItineraryId(long itineraryId) {
        Log.i("TAG","itinerari recuperati");

        List<ReportDTO> test = new ArrayList<ReportDTO>();

        ReportDTO test1 = new ReportDTO();
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

        return test;
    }

    @Override
    public ReportDTO findById(long reportId) {

        ReportDTO test = new ReportDTO();
        test.setId(15);
        test.setName("jdfsdfsennifer");
        test.setDescription("pyrfgdfgo");
        test.setDateOfInput("11/11/21");
        test.setId_user(23);
        test.setId_itinerary(6);

        return test;
    }

    @Override
    public MessageDTO deleteById(long reportId) {
        Log.i("TAG","segnalazione rimossa");
        return null;
    }
}
