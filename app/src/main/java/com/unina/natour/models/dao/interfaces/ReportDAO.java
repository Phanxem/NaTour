package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.ReportDTO;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.List;

public interface ReportDAO {

    MessageDTO addReport(ReportDTO reportDTO);

    List<ReportDTO> findByItineraryId(long itineraryId);

    ReportDTO findById(long reportId);

    MessageDTO deleteById(long reportId);
}
