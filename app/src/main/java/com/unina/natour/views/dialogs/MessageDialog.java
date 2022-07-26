package com.unina.natour.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.unina.natour.R;


public class MessageDialog extends Dialog {

    Context context;

    public MessageDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.setContentView(R.layout.dialog_message);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Dialog dialog = this;
        Button buttonOK = dialog.findViewById(R.id.Message_button_OK);
        buttonOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void setMessage(String message){
        TextView textViewMessage = this.findViewById(R.id.Message_textView_messaggio);
        textViewMessage.setText(message);
    }

    public String getMessage(){
        TextView textViewMessage = this.findViewById(R.id.Message_textView_messaggio);
        return textViewMessage.getText().toString();
    }

    public void showOverUi(){
        Dialog dialog = this;
        ((Activity)context).runOnUiThread(new Runnable() {
            public void run() {
                dialog.show();
            }
        });
    }

}
