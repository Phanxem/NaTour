package com.unina.natour.controllers;

import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.views.activities.NaTourActivity;

public class NaTourController {

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private ResultMessageController resultMessageController;


    public NaTourController(NaTourActivity activity){
        this.activity = activity;
        this.resultMessageController = new ResultMessageController(activity);

    }

    public NaTourController(){}

    public NaTourActivity getActivity() {
        return activity;
    }

    public void setActivity(NaTourActivity activity) {
        this.activity = activity;
    }

    public ResultMessageController getMessageController() {
        return resultMessageController;
    }

    public void setErrorMessageController(ResultMessageController resultMessageController) {
        this.resultMessageController = resultMessageController;
    }

    public void showErrorMessage(ResultMessageDTO resultMessageDTO){
        this.resultMessageController.showErrorMessage(resultMessageDTO);
    }

    public void showErrorMessage(long code){
        this.resultMessageController.showErrorMessage(code);
    }

//---

}
