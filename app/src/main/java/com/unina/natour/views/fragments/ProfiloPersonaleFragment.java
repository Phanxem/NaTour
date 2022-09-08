package com.unina.natour.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.DisconnessioneController;
import com.unina.natour.controllers.ListaItinerariController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.ProfiloPersonaleController;
import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.views.dialogs.MessageDialog;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfiloPersonaleFragment extends Fragment {

    View view;
    MessageDialog messageDialog;

    ProfiloPersonaleController profiloPersonaleController;
    ListaItinerariController listaItinerariController;
    DisconnessioneController disconnessioneController;
    AutenticazioneController autenticazioneController;


    ProfiloPersonaleModel profiloPersonaleModel;



    public static ProfiloPersonaleFragment newInstance(Parcelable controller){
        ProfiloPersonaleFragment profiloPersonaleFragment = new ProfiloPersonaleFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        profiloPersonaleFragment.setArguments(args);

        return profiloPersonaleFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profilo_personale, container, false);

        Bundle args = getArguments();
        if(args != null){
            this.profiloPersonaleController = (ProfiloPersonaleController) args.getParcelable(MainController.KEY_CONTROLLER);
            this.messageDialog = profiloPersonaleController.getMessageDialog();

        }
        else{
            messageDialog = new MessageDialog();
            this.messageDialog.setFragmentActivity(getActivity());
            this.profiloPersonaleController = new ProfiloPersonaleController(getActivity(),messageDialog);
        }

        this.disconnessioneController = new DisconnessioneController(getActivity(), messageDialog);
        this.autenticazioneController = new AutenticazioneController(getActivity(), messageDialog);

        this.listaItinerariController = profiloPersonaleController.getListaItinerariController();

        this.profiloPersonaleModel = profiloPersonaleController.getProfiloPersonaleModel();


        RecyclerView recyclerView_itineraries = view.findViewById(R.id.ProfiloPersonaleF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.ProfiloPersonaleF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.ProfiloPersonaleF_progressBar_itinerari);

        listaItinerariController.initItineraryList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

        update();

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
                    //open modifica Profilo Activity
                    return true;
                }
                if(item.getItemId() == R.id.ProfiloPersonaleF_popupMenu_esci){
                    Boolean result = disconnessioneController.signOut();

                    if(result) autenticazioneController.openAutenticazioneActivity();

                    return true;
                }
                else return false;

            }
        });


        imageView_iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    public void update(){

        TextView textView_username = view.findViewById(R.id.ProfiloPersonaleF_textView_username);
        textView_username.setText(profiloPersonaleModel.getUsername());

        TextView textView_email = view.findViewById(R.id.ProfiloPersonaleF_textView_email);
        textView_email.setText(profiloPersonaleModel.getEmail());

        LinearLayout linearLayout_residence = view.findViewById(R.id.ProfiloPersonale_linearLayout_residence);
        if(profiloPersonaleModel.getPlaceOfResidence() != null) {
            TextView textView_residence = view.findViewById(R.id.ProfiloPersonaleF_textView_residence);
            textView_residence.setText(profiloPersonaleModel.getPlaceOfResidence());
            linearLayout_residence.setVisibility(View.VISIBLE);
        }
        else linearLayout_residence.setVisibility(View.GONE);

        LinearLayout linearLayout_birth = view.findViewById(R.id.ProfiloPersonale_linearLayout_birth);
        if(profiloPersonaleModel.getDateOfBirth() != null) {
            TextView textView_birth = view.findViewById(R.id.ProfiloPersonaleF_textView_birth);
            textView_birth.setText(profiloPersonaleModel.getDateOfBirth());
            linearLayout_birth.setVisibility(View.VISIBLE);
        }
        else linearLayout_birth.setVisibility(View.GONE);

        if(profiloPersonaleModel.getProfileImage() != null){
            ImageView imageView_profilePicture = view.findViewById(R.id.ProfiloPersonaleF_imageView_immagineProfilo);
            imageView_profilePicture.setImageBitmap(profiloPersonaleModel.getProfileImage());
        }

    }
}