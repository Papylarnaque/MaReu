package com.example.mareu.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Reunion> mReunions;
    private MaReuApiService mMaReuApiService;

    void setData(List<Reunion> reunions) {
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
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
        // Call the ApiService
        mMaReuApiService = DI.getMaReuApiService();

        // Affect a random color for each reunion
        int mIntColor = (int) ((0.2 + Math.random()) * 1000000000);
        holder.mColor.setTint(Color.argb(mIntColor, mIntColor, mIntColor, mIntColor));

        // First line of the reunion
        String subjectReunion = mReunions.get(position).getSubject();
        Date mDuration = mReunions.get(position).getDuration();
        // TODO Utiliser Calendar au lieu de getHours / getMinutes
        long minutes = mDuration.getHours()*60 + mDuration.getMinutes();
        // TextHolder for the first line
        String mFirstLineString = subjectReunion + " ("+minutes+"min)";
        holder.mFirstLine.setText(mFirstLineString);

        // Second line of the reunion
        Date dateReunion = mReunions.get(position).getDate();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        String roomReunion = mReunions.get(position).getRoom();
        String strDate = dateFormat.format(dateReunion);
        // TextHolder for the second line
        String textSeparator = " - ";
        String mSecondLineString = strDate + textSeparator + "<b>" +roomReunion + "</b>";
        holder.mSecondLine.setText(Html.fromHtml(mSecondLineString));

        // Third line of the reunion
        List<Guest> mGuestsList = mReunions.get(position).getGuests();
        List<String> mGuestsEmails = mMaReuApiService.getGuestsEmails(mGuestsList);
        // TextHolder for the third line
        StringBuilder mGuestsEmailsStringBuilder = new StringBuilder();
        for (String s : mGuestsEmails) {
            mGuestsEmailsStringBuilder.append(s);
            mGuestsEmailsStringBuilder.append(", ");
        }
        holder.mThirdLine.setText(mGuestsEmailsStringBuilder.toString());

        // Delete button
        holder.mButtonDeleteReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Suppression de la réunion " + mReunions.get(position).getSubject(), Toast.LENGTH_SHORT).show();
                deleteItem(position);
                setData(mReunions);
            }
        });
    }

    private void deleteItem(int position) {
        mMaReuApiService.deleteReunion(mReunions.get(position));
    }

    @Override
    public int getItemCount() {
        return mReunions.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        Drawable mColor;
        TextView mFirstLine;
        TextView mSecondLine;
        TextView mThirdLine;
        ImageButton mButtonDeleteReunion;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mColor = AppCompatResources.getDrawable(itemView.getContext(), R.drawable.circle);
            mFirstLine = itemView.findViewById(R.id.item_reunion_first_line);
            mSecondLine = itemView.findViewById(R.id.item_reunion_second_line);
            mThirdLine = itemView.findViewById((R.id.item_reunion_third_line));
            mThirdLine.setSelected(true);
            mButtonDeleteReunion = itemView.findViewById(R.id.item_reunion_delete);
        }
    }
}

