package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.request.ReportRequestDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.ReportListResponseDTO;
import com.unina.natour.dto.response.ReportResponseDTO;

import java.util.List;

public interface ReportDAO extends ServerDAO{

    MessageResponseDTO addReport(ReportRequestDTO reportRequestDTO);

    ReportListResponseDTO findByItineraryId(long itineraryId);

    ReportResponseDTO findById(long reportId);

    MessageResponseDTO deleteById(long reportId);
}
