package com.unina.natour.views.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.unina.natour.R;
import com.unina.natour.controllers.SelezionaDurataController;
import com.unina.natour.controllers.SelezionaNazioneController;

public class SelectDurationDialog extends DialogFragment {

    SelezionaDurataController selezionaDurataController;

    public SelectDurationDialog(){ }

    public interface OnDurationListener {
        void getDuration(Float duration);
    }

    public SelectDurationDialog.OnDurationListener onDurationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_duration, container, false);

        selezionaDurataController = new SelezionaDurataController(getActivity());

        Button button_ok = view.findViewById(R.id.SelectDuration_button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText_hours = view.findViewById(R.id.SelectDuration_editNumber_hours);
                float hours;
                if(editText_hours.getText().toString().isEmpty()) hours = 0;
                else hours = Float.valueOf(editText_hours.getText().toString());

                EditText editText_minutes = view.findViewById(R.id.SelectDuration_editNumber_minutes);
                float minutes;
                if(editText_minutes.getText().toString().isEmpty()) minutes = 0;
                else minutes = Float.valueOf(editText_minutes.getText().toString());

                float result = selezionaDurataController.toSeconds(hours,minutes);
                if(result != 0) onDurationListener.getDuration(result);
                getDialog().dismiss();
            }
        });

        Button button_cancel = view.findViewById(R.id.SelectDuration_button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onDurationListener.getDuration(null);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDurationListener = (SelectDurationDialog.OnDurationListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }
    }

}
