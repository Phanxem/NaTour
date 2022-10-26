package com.unina.natour.views.listAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.models.ElementMessageModel;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.ArrayList;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private NaTourActivity activity;

    private ArrayList<ElementMessageModel> elementsMessageModel;


    public MessagesListAdapter(NaTourActivity activity, ArrayList<ElementMessageModel> model){
        this.elementsMessageModel = model;
        this.activity = activity;
    }


    @Override
    public int getItemViewType(int position) {
        ElementMessageModel message = elementsMessageModel.get(position);
        return message.getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ElementMessageModel.CODE_MESSAGE_SENT) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_element_chat_message_sent, parent, false);
            return new MessageSentViewHolder(view);
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.list_element_chat_message_received, parent, false);
        return new MessageReceivedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ElementMessageModel message = elementsMessageModel.get(position);

        if(message.getType() == ElementMessageModel.CODE_MESSAGE_SENT){
            MessageSentViewHolder viewHolder = (MessageSentViewHolder) holder;
            viewHolder.textView_message.setText(message.getMessage());
        }
        else{
            MessageReceivedViewHolder viewHolder = (MessageReceivedViewHolder) holder;
            viewHolder.textView_message.setText(message.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return elementsMessageModel.size();
    }


    public class MessageSentViewHolder extends RecyclerView.ViewHolder {
        TextView textView_message;

        public MessageSentViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_message = itemView.findViewById(R.id.MessageSent_textView_message);
        }
    }



    public class MessageReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView textView_message;

        public MessageReceivedViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_message = itemView.findViewById(R.id.MessageReceived_textView_message);
        }
    }


}
