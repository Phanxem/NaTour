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
import com.unina.natour.controllers.SelezionaCittàController;
import com.unina.natour.controllers.SelezionaNazioneController;

public class SelectCityDialog extends NaTourDialog {

    SelezionaCittàController selezionaCittàController;

    public SelectCityDialog(){ }

    public interface OnCityListener {
        void getCity(String city);
    }

    public SelectCityDialog.OnCityListener onCityListener;


    public static SelectCityDialog newInstance(String country) {
        SelectCityDialog selectCityDialog = new SelectCityDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("country", country);
        selectCityDialog.setArguments(args);

        return selectCityDialog;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_string, container, false);

        String country = getArguments().getString("country");
        selezionaCittàController = new SelezionaCittàController(getNaTourActivity());
        selezionaCittàController.setCountry(country);

        String[] cities = selezionaCittàController.getCities();


        TextView textView_titolo = view.findViewById(R.id.SelectString_textView_titolo);
        textView_titolo.setText(getActivity().getString(R.string.SelezionaCittà_titolo));

        ListView listView = view.findViewById(R.id.SelectString_listView_lista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = cities[position];
                onCityListener.getCity(result);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onCityListener = (SelectCityDialog.OnCityListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }
    }
};

