package com.unina.natour.views.listAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.models.ElementItineraryModel;

import java.util.ArrayList;

public class ItineraryListAdapter extends RecyclerView.Adapter<ItineraryListAdapter.ViewHolder>{

    private FragmentActivity activity;
    private ArrayList<ElementItineraryModel> elementsItineraryModel;
    private boolean doBelongToSameUser;

    public ItineraryListAdapter(FragmentActivity activity, ArrayList<ElementItineraryModel> model, boolean doBelongToSameUser){
        this.elementsItineraryModel = model;
        this.activity = activity;
        this.doBelongToSameUser = doBelongToSameUser;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_element_itinerary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ElementItineraryModel itinerary = elementsItineraryModel.get(position);

        holder.textView_name.setText(itinerary.getName());

        if(doBelongToSameUser){
            holder.relativeLayout_user.setVisibility(View.GONE);
        }
        else {
            holder.textView_username.setText(itinerary.getUsername());
            if (itinerary.getUserImage() != null) holder.imageView_userImage.setImageBitmap(itinerary.getUserImage());
            else holder.imageView_userImage.setImageDrawable(activity.getDrawable(R.drawable.ic_generic_account_minor));
        }


        String description = itinerary.getDescription();
        if(description == null || description.isEmpty()) holder.textView_difficulty.setText("");
        else holder.textView_description.setText(description);

        holder.textView_duration.setText(itinerary.getDuration());
        holder.textView_lenght.setText(itinerary.getLenght());
        holder.textView_difficulty.setText(itinerary.getDifficulty());

        holder.linearLayout_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO controller Dettagli itinerario. open(id itinerario)
                //DettagliItinerarioController.open(idItinerario);
            }
        });
    }


    @Override
    public int getItemCount() {
        return elementsItineraryModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout_itinerary;

        TextView textView_name;

        RelativeLayout relativeLayout_user;
        ImageView imageView_userImage;
        TextView textView_username;

        TextView textView_description;

        TextView textView_duration;
        TextView textView_lenght;
        TextView textView_difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            linearLayout_itinerary = itemView.findViewById(R.id.ListElementItinerary_linearLayout_itinerary);

            textView_name = itemView.findViewById(R.id.ListElementItinerary_textView_name);

            relativeLayout_user = itemView.findViewById(R.id.ListElementItinerary_relativeLayout_user);
            imageView_userImage = itemView.findViewById(R.id.ListElementItinerary_imageView_immagineProfilo);
            textView_username = itemView.findViewById(R.id.ListElementItinerary_textView_username);

            textView_description = itemView.findViewById(R.id.ListElementItinerary_textView_descrizione);

            textView_duration = itemView.findViewById(R.id.ListElementItinerary_textView_duration);
            textView_lenght = itemView.findViewById(R.id.ListElementItinerary_textView_distance);
            textView_difficulty = itemView.findViewById(R.id.ListElementItinerary_textView_difficulty);
        }
    }

}
