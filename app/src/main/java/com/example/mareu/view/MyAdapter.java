package com.example.mareu.view;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private static final String TEXT_SEPARATOR = " - ";
    private List<Reunion> mReunions;
    private MaReuApiService mMaReuApiService;

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reunion, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(mReunions.get(position).getStartDate());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(mReunions.get(position).getEndDate());

        long durationReunion = endDate.getTimeInMillis() - startDate.getTimeInMillis();

        // Call the ApiService
        mMaReuApiService = DI.getMaReuApiService();
        // Affect a color for each room
        setRoomColor(holder, position);

        // First line of the reunion
        String subjectReunion = mReunions.get(position).getSubject();
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatDuration = new SimpleDateFormat("(HH:mm)", Locale.getDefault());
        TimeZone UTC = TimeZone.getTimeZone("UTC");
        dateFormatDuration.setTimeZone(UTC);
        mCalendar.setTimeInMillis(durationReunion);
        String durationString = dateFormatDuration.format(mCalendar.getTimeInMillis());

        // TextHolder for the first line
        String mFirstLineString = subjectReunion + TEXT_SEPARATOR + durationString;
        holder.mFirstLine.setText(mFirstLineString);

        // Second line of the reunion
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        String roomReunion = mReunions.get(position).getRoom().getRoomName();
        String startDateString = dateFormat.format(startDate.getTime());
        // TextHolder for the second line
        String mSecondLineString = startDateString + TEXT_SEPARATOR + "<b>" + roomReunion + "</b>";
        holder.mSecondLine.setText(Html.fromHtml(mSecondLineString));

        // Third line of the reunion
        List<Guest> mGuestsList = mReunions.get(position).getGuests();
        List<String> mGuestsEmails = mMaReuApiService.getGuestsEmails(mGuestsList);
        // TextHolder for the third line
        StringBuilder mGuestsEmailsStringBuilder = new StringBuilder();
        for (String s : mGuestsEmails) {
            if (!s.equals(mGuestsEmails.get(mGuestsEmails.size() - 1))) {
                mGuestsEmailsStringBuilder.append(s);
                mGuestsEmailsStringBuilder.append(", ");
            } else {
                mGuestsEmailsStringBuilder.append(s);
            }
        }
        holder.mThirdLine.setText(mGuestsEmailsStringBuilder.toString());


        // ACTIONS ******************************************************************************************

        // Delete button
        deleteButton(holder, position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView mColor;
        final TextView mFirstLine;
        final TextView mSecondLine;
        final TextView mThirdLine;
        final ImageButton mButtonDeleteReunion;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mColor = itemView.findViewById(R.id.item_reunion_image);
            mFirstLine = itemView.findViewById(R.id.item_reunion_first_line);
            mSecondLine = itemView.findViewById(R.id.item_reunion_second_line);
            mThirdLine = itemView.findViewById((R.id.item_reunion_third_line));
            //mThirdLine.setSelected(true);
            mButtonDeleteReunion = itemView.findViewById(R.id.item_reunion_delete);
        }
    }

    //  ****************************************** INIT ********************************************

    void setData(List<Reunion> reunions) {
        this.mReunions = reunions;
        notifyDataSetChanged(); // dit à l'adapter de se rafraichir
    }

    private void setRoomColor(@NonNull MyViewHolder holder, int position) {
        switch ((int) mReunions.get(position).getRoom().getId()) {
            case 1:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRoom1));
                break;
            case 2:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom2));
                break;
            case 3:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom3));
                break;
            case 4:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom4));
                break;
            case 5:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom5));
                break;
            case 6:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom6));
                break;
            case 7:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom7));
                break;
            case 8:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom8));
                break;
            case 9:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom9));
                break;
            case 10:
                holder.mColor.getBackground().setTint(ContextCompat.getColor(holder.mColor.getContext(), R.color.colorRoom10));
                break;

        }
    }


    //  ****************************************** ACTIONS  ****************************************


    private void deleteButton(@NonNull MyViewHolder holder, final int position) {
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

    @Override
    public Filter getFilter() {
        return null;
    }

}

