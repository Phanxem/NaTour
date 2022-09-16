package com.unina.natour.views.fragments;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.unina.natour.R;
import com.unina.natour.controllers.CommunityController;
import com.unina.natour.controllers.ListaUtentiController;

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
        this.listaUtentiController = communityController.getListUtentiController();


        RecyclerView recyclerView_users = view.findViewById(R.id.Community_recycleView_utenti);
        NestedScrollView nestedScrollView_users = view.findViewById(R.id.Community_nestedScrollView_utenti);
        ProgressBar progressBar_users = view.findViewById(R.id.Community_progressBar_utenti);

        listaUtentiController.initItineraryList(recyclerView_users,nestedScrollView_users, progressBar_users);

        searchFromSearchBar();

        return view;
    }

    public void searchFromSearchBar(){
        EditText editText_barraRicerca = view.findViewById(R.id.Community_editText_research);
        editText_barraRicerca.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchString = editText_barraRicerca.getText().toString();

                    editText_barraRicerca.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    return true;
                }
                return false;
            }
        });
    }
}