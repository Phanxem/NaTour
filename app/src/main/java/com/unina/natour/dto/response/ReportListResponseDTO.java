package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ReportListResponseDTO {
    private MessageResponseDTO resultMessage;
    private List<ReportResponseDTO> reports;

    public ReportListResponseDTO(){
        this.reports = new ArrayList<ReportResponseDTO>();
    }

    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<ReportResponseDTO> getReports() {
        return reports;
    }

    public void setReports(List<ReportResponseDTO> reports) {
        this.reports = reports;
    }
}
