package com.unina.natour.views.listAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.controllers.ListaUtentiController;
import com.unina.natour.controllers.ProfiloController;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private NaTourActivity activity;

    private ArrayList<ElementUserModel> elementsUserModel;

    private long researchCode;


    public UserListAdapter(NaTourActivity activity, ArrayList<ElementUserModel> model, long researchCode){
        this.elementsUserModel = model;
        this.activity = activity;
        this.researchCode = researchCode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_element_user, parent, false);
        return new UserListAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ElementUserModel user = elementsUserModel.get(position);

        holder.textView_user.setText(user.getUsername());

        if(user.getProfileImage() != null) holder.imageView_user.setImageBitmap(user.getProfileImage());
        else{
            holder.imageView_user.setImageDrawable(activity.getDrawable(R.drawable.ic_generic_account));
        }

        if(researchCode == ListaUtentiController.CODE_USER_BY_RESEARCH) {
            holder.relativeLayout_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfiloController.openProfiloUtenteActivity(activity, user.getUserId());
                }
            });

            holder.imageView_notification.setVisibility(View.GONE);
        }
        else{
            holder.relativeLayout_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatController.openChatActivity(activity,user.getUserId());
                }
            });

            if(user.hasMessagesToRead()) holder.imageView_notification.setVisibility(View.VISIBLE);
            else holder.imageView_notification.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return elementsUserModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout_user;

        ImageView imageView_user;
        TextView textView_user;

        ImageView imageView_notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout_user = itemView.findViewById(R.id.ListElementUser_relativeLayout_user);

            imageView_user = itemView.findViewById(R.id.ListElementUser_imageView_immagineProfilo);
            textView_user = itemView.findViewById(R.id.ListElementUser_textView_username);

            imageView_notification = itemView.findViewById(R.id.ListElementUser_imageView_notification);
        }
    }

}
