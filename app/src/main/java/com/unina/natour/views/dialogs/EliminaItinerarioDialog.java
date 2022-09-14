package com.unina.natour.views.dialogs;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.controllers.EliminaItinerarioController;
@RequiresApi(api = Build.VERSION_CODES.N)
public class EliminaItinerarioDialog extends NaTourDialog{

    private View view;

    private EliminaItinerarioController eliminaItinerarioController;


    public EliminaItinerarioDialog(long itineraryId) {
        this.eliminaItinerarioController = new EliminaItinerarioController(getNaTourActivity(), itineraryId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_elimina_itinerario, container, false);

        Button button_yes = view.findViewById(R.id.EliminaItinerario_button_yes);
        button_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                eliminaItinerarioController.deleteItinerary();
                getDialog().dismiss();
                getNaTourActivity().finish();
            }
        });

        Button button_no = view.findViewById(R.id.EliminaItinerario_button_no);
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
