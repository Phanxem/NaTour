package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.request.SaveReportRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetListReportResponseDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;

public interface ReportDAO {

    //GETs
    public GetReportResponseDTO getReportById(long idReport);

    public GetListReportResponseDTO getReportByIdItinerary(long idItinerary);

    //POSTs
    public ResultMessageDTO addReport(SaveReportRequestDTO saveReportRequestDTO);

    //DELETEs
    public ResultMessageDTO deleteReportById(long reportId);
}
