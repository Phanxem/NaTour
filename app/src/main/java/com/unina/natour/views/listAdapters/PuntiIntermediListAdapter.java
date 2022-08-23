package com.unina.natour.views.listAdapters;

import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.unina.natour.R;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.controllers.utils.AddressUtils;
import com.unina.natour.models.AddressModel;

import java.util.List;

public class PuntiIntermediListAdapter extends ArrayAdapter<AddressModel> {

    private PianificaItinerarioController pianificaItinerarioController;

    public PuntiIntermediListAdapter(Context context, List<AddressModel> interestPoints, PianificaItinerarioController controller) {
        super(context, R.layout.list_element_punto_intermedio,interestPoints);
        this.pianificaItinerarioController = controller;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        AddressModel interestPoint = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_element_punto_intermedio, parent, false);
        }


        TextView textView_pos =  convertView.findViewById(R.id.listElement_textView_pos);
        textView_pos.setText( (position + 1) + " :");

        TextView textView_name =  convertView.findViewById(R.id.listElement_textView_name);
        textView_name.setText(interestPoint.getAddressName());

        RelativeLayout relativeLayout_listItem = convertView.findViewById(R.id.listElementIntermediatePoint_relativeLayout_listElement);
        relativeLayout_listItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAGDF: ", "CLICK " + position);
                pianificaItinerarioController.selectIntermediatePoint(position);
                notifyDataSetChanged();
            }
        });

        ImageView imageView_iconCancel = convertView.findViewById(R.id.listElementIntermediatePoint_imageView_listElementCancel);
        imageView_iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGDF: ", "canc " + position);
                pianificaItinerarioController.cancelIntermediatePoint(position);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }


}
