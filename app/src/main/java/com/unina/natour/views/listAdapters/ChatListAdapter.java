package com.unina.natour.views.listAdapters;

import android.content.Context;
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
import com.unina.natour.controllers.ImportaFileGPXController;
import com.unina.natour.models.ChatMessageModel;
import com.unina.natour.views.activities.ChatActivity;

import java.io.File;
import java.util.List;

public class ChatListAdapter extends ArrayAdapter<ChatMessageModel> {

    public ChatListAdapter(Context context, List<ChatMessageModel> messages) {
        super(context, R.layout.list_element_chat_message_sent,messages);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessageModel message = getItem(position);

        TextView textView_message = null;

        if (convertView == null) {
            if (message.didISendIt()) {
                convertView = LayoutInflater
                        .from(getContext())
                        .inflate(R.layout.list_element_chat_message_sent, parent, false);

                textView_message = convertView.findViewById(R.id.MessageSent_textView_message);
            }
            else {
                convertView = LayoutInflater
                        .from(getContext())
                        .inflate(R.layout.list_element_chat_message_received, parent, false);

                textView_message = convertView.findViewById(R.id.MessageReceived_textView_message);
            }
        }


        if(textView_message != null) textView_message.setText(message.getMessage());



        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
