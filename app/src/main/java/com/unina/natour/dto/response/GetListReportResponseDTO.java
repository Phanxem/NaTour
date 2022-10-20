package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class GetListReportResponseDTO {
    private ResultMessageDTO resultMessage;
    private List<GetReportResponseDTO> listReport;

    public GetListReportResponseDTO(){
        this.listReport = new ArrayList<GetReportResponseDTO>();
    }

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GetReportResponseDTO> getListReport() {
        return listReport;
    }

    public void setListReport(List<GetReportResponseDTO> listReport) {
        this.listReport = listReport;
    }
}
