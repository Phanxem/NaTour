package com.unina.natour.views.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.unina.natour.R;
import com.unina.natour.controllers.SelezionaNazioneController;

public class SelectCountryDialog extends DialogFragment {

    SelezionaNazioneController selezionaNazioneController;

    public SelectCountryDialog(){ }

    public interface OnCountryListener {
        void getCountry(String input);
    }

    public OnCountryListener onCountryListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_string, container, false);

        selezionaNazioneController = new SelezionaNazioneController(getActivity());

        String[] countries = selezionaNazioneController.getCountries();

        TextView textView_titolo = view.findViewById(R.id.SelectString_textView_titolo);
        textView_titolo.setText(getActivity().getString(R.string.SelezionaNazione_titolo));

        ListView listView = view.findViewById(R.id.SelectString_listView_lista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = countries[position];
                onCountryListener.getCountry(result);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onCountryListener = (OnCountryListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }
    }
};

