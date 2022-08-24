package com.unina.natour.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.unina.natour.R;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.SelezionaCittàController;
import com.unina.natour.views.fragments.PianificaItinerarioFragment;

public class MessageDialog extends DialogFragment {

    private static final String TAG = "MessageDialog";
    private String message = "";



    public void setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    private FragmentManager supportFragmentManager;



    public MessageDialog() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message, container, false);

        Button buttonOK = view.findViewById(R.id.Message_button_OK);
        buttonOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        TextView textViewMessage = view.findViewById(R.id.Message_textView_messaggio);
        textViewMessage.setText(message);

        return view;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }




    public void showOverUi(){
        this.show(getSupportFragmentManager(), "");
    }


/*
    public void showOverUi(){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                getDialog().show();
            }
        });
    }
*/

}
