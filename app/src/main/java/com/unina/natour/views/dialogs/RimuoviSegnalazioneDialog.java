package com.unina.natour.views.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unina.natour.R;
import com.unina.natour.controllers.EliminaItinerarioController;
import com.unina.natour.controllers.RimuoviSegnalazioneController;

public class RimuoviSegnalazioneDialog extends NaTourDialog{

    private View view;

    private RimuoviSegnalazioneController rimuoviSegnalazioneController;


    public RimuoviSegnalazioneDialog(long reportId) {
        this.rimuoviSegnalazioneController = new RimuoviSegnalazioneController(getNaTourActivity(), reportId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_rimuovi_segnalazione, container, false);

        Button button_yes = view.findViewById(R.id.RemoveReport_button_yes);
        button_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rimuoviSegnalazioneController.deleteReport();
                getDialog().dismiss();
                getNaTourActivity().finish();
            }
        });

        Button button_no = view.findViewById(R.id.RemoveReport_button_no);
        button_no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void showOverUi(){
        this.show(getNaTourActivity().getSupportFragmentManager(), "");
    }

}
