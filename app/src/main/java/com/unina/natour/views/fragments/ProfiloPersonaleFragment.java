package com.unina.natour.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.unina.natour.R;
import com.unina.natour.controllers.ProfiloPersonaleController;
import com.unina.natour.models.ProfiloPersonaleModel;


public class ProfiloPersonaleFragment extends Fragment {

    View view;

    ProfiloPersonaleController profiloPersonaleController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profilo, container, false);

        this.profiloPersonaleController = new ProfiloPersonaleController(getActivity());

        profiloPersonaleController.initModel();

        ProfiloPersonaleModel model = profiloPersonaleController.getProfiloPersonaleModel();
        updateUI(model);

        pressMenuIcon();

        return view;
    }

    public void pressMenuIcon() {
        ImageView imageView_iconMenu = view.findViewById(R.id.ProfiloPersonaleF_imageView_iconaMenu);

        PopupMenu popupMenu = new PopupMenu(view.getContext(),imageView_iconMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_profilo_personale, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.ProfiloPersonaleF_popupMenu_modifica){

                    return true;
                }
                if(item.getItemId() == R.id.ProfiloPersonaleF_popupMenu_esci){

                    return true;
                }
                else{
                    return false;
                }
            }
        });


        imageView_iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    public void updateUI(ProfiloPersonaleModel model){
        Activity activity = getActivity();
        TextView textView_username = view.findViewById(R.id.ProfiloPersonaleF_textView_username);
        textView_username.setText(model.getUsername());

        TextView textView_email = view.findViewById(R.id.ProfiloPersonaleF_textView_email);
        //textView_email.setText(model.getEmail());

        if(model.getPlaceOfResidence() != null) {
            TextView textView_residence = view.findViewById(R.id.ProfiloPersonaleF_textView_residence);
            textView_residence.setText("Luogo di Residenza: " + model.getPlaceOfResidence());
            textView_residence.setVisibility(View.VISIBLE);
        }

        if(model.getDateOfBirth() != null) {
            TextView textView_birth = view.findViewById(R.id.ProfiloPersonaleF_textView_birth);
            textView_birth.setText("Data di Nascita: " + model.getDateOfBirth());
            textView_birth.setVisibility(View.VISIBLE);
        }

        if(model.getProfileImage() != null){
            ImageView imageView_profilePicture = view.findViewById(R.id.ProfiloPersonaleF_imageView_immagineProfilo);
            imageView_profilePicture.setImageBitmap(model.getProfileImage());
        }

    }
}