package com.unina.natour.views.listAdapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.DettagliItinerarioController;
import com.unina.natour.controllers.ProfiloController;
import com.unina.natour.models.ElementItineraryModel;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private NaTourActivity activity;

    private ArrayList<ElementUserModel> elementsUserModel;


    public UserListAdapter(NaTourActivity activity, ArrayList<ElementUserModel> model){
        this.elementsUserModel = model;
        this.activity = activity;

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

        holder.relativeLayout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfiloController.openProfiloUtenteActivity(activity, user.getUserId());
                //TODO openProfiloActivity;
            }
        });
    }


    @Override
    public int getItemCount() {
        return elementsUserModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout_user;

        ImageView imageView_user;
        TextView textView_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout_user = itemView.findViewById(R.id.ListElementUser_relativeLayout_user);

            imageView_user = itemView.findViewById(R.id.ListElementUser_imageView_immagineProfilo);
            textView_user = itemView.findViewById(R.id.ListElementUser_textView_username);
        }
    }

}
