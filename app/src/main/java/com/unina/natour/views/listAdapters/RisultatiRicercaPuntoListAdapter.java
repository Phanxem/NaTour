package com.unina.natour.views.listAdapters;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.unina.natour.R;
import com.unina.natour.controllers.RicercaPuntoController;
import com.unina.natour.models.AddressModel;

import java.util.List;

public class RisultatiRicercaPuntoListAdapter extends ArrayAdapter<AddressModel> {

    private RicercaPuntoController ricercaPuntoController;

    public RisultatiRicercaPuntoListAdapter(Context context, List<AddressModel> resultPoints, RicercaPuntoController controller) {
        super(context, R.layout.list_element_risultato_ricerca_punto,resultPoints);

        this.ricercaPuntoController = controller;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressModel resultPoint = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_element_risultato_ricerca_punto, parent, false);
        }

        TextView textView_resultPoint =  convertView.findViewById(R.id.listElementResultPoint_textView_listElement);
        RelativeLayout relativeLayout_listItem = convertView.findViewById(R.id.listElementResultPoint_relativeLayout_listElement);

        textView_resultPoint.setText(resultPoint.getAddressName());

        relativeLayout_listItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ricercaPuntoController.selectResultPoint(resultPoint);
            }
        });
        return convertView;
    }
}
