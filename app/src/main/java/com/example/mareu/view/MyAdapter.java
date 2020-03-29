package com.example.mareu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Reunion> mReunions;
    private MaReuApiService mMaReuApiService;

    public void setData(List<Reunion> reunions){
        this.mReunions = reunions;
        notifyDataSetChanged(); // dit à l'adapter de se rafraichir
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reunion, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        String subjectReunion = mReunions.get(position).getSubject();
        String timeReunion = mReunions.get(position).getHour();
        String roomReunion = mReunions.get(position).getRoom();
        String textSeparator = " - ";
        String headReunionText = subjectReunion + textSeparator + timeReunion + textSeparator + roomReunion;
        // TextHolder for HeadLine
        holder.headReunion.setText(headReunionText);

        // Get the Emails through ApiService
        mMaReuApiService = DI.getMaReuApiService();
        String mGuestsEmails = mMaReuApiService.getGuestsEmails(mReunions.get(position).getGuestList());
        // TextHolder for GuestsLine
        holder.guestListReunion.setText(mGuestsEmails);


          holder.mButtonDeleteReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Delete reunion fonctionality
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReunions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageButton mButtonDeleteReunion;
        TextView headReunion;
        TextView guestListReunion;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headReunion = itemView.findViewById(R.id.item_reunion_subject_time_room);
            guestListReunion = itemView.findViewById((R.id.item_reunion_guest));
            guestListReunion.setSelected(true);
            mButtonDeleteReunion = itemView.findViewById(R.id.item_reunion_delete);
        }
    }
}

