package com.unina.natour.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.CommunityController;
import com.unina.natour.controllers.ListaUtentiController;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.dialogs.MessageDialog;

public class CommunityFragment extends NaTourFragment {

    private View view;
    private CommunityController communityController;
    private ListaUtentiController listaUtentiController;








    /*
    public static CommunityFragment newInstance(Parcelable controller){
        CommunityFragment communityFragment = new CommunityFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        communityFragment.setArguments(args);

        return communityFragment;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);
        setFragmentView(view);



        /*
        Bundle args = getArguments();
        if(args != null){
            this.communityController = (CommunityController) args.getParcelable(MainController.KEY_CONTROLLER);
        }
        else{
            this.communityController = new CommunityController(getNaTourActivity());
        }
        */

        this.communityController = new CommunityController(getNaTourActivity());
        this.listaUtentiController = communityController.getListaUtentiController();


        RecyclerView recyclerView_users = view.findViewById(R.id.Community_recycleView_utenti);
        NestedScrollView nestedScrollView_users = view.findViewById(R.id.Community_nestedScrollView_utenti);
        ProgressBar progressBar_users = view.findViewById(R.id.Community_progressBar_utenti);

        listaUtentiController.initList(nestedScrollView_users,recyclerView_users, progressBar_users);

        searchFromSearchBar();
        pressIconCancel();


        return view;
    }

    public void searchFromSearchBar(){
        ImageView imageView_iconClose = view.findViewById(R.id.Community_imageView_cancelResearch);
        RecyclerView recyclerView_itineraries = view.findViewById(R.id.Community_recycleView_utenti);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.Community_nestedScrollView_utenti);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.Community_progressBar_utenti);

        EditText editText_barraRicerca = view.findViewById(R.id.Community_editText_research);
        editText_barraRicerca.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchString = editText_barraRicerca.getText().toString();

                    if(searchString != null && !searchString.isEmpty()) {
                        communityController.searchUser(searchString);
                        listaUtentiController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

                        editText_barraRicerca.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        imageView_iconClose.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void pressIconCancel() {
        EditText editText_barraRicerca = view.findViewById(R.id.Community_editText_research);
        RecyclerView recyclerView_itineraries = view.findViewById(R.id.Community_recycleView_utenti);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.Community_nestedScrollView_utenti);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.Community_progressBar_utenti);

        ImageView imageView_iconClose = view.findViewById(R.id.Community_imageView_cancelResearch);
        imageView_iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityController.cancelReseach();
                listaUtentiController.initList(nestedScrollView_itineraries, recyclerView_itineraries, progressBar_itinearies);

                editText_barraRicerca.setText("");
                imageView_iconClose.setVisibility(View.GONE);
            }
        });
    }


}