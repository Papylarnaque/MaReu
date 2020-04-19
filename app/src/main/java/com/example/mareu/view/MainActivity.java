package com.example.mareu.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private MaReuApiService mMaReuApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();

        mMaReuApiService = DI.getMaReuApiService();
        adapter.setData(mMaReuApiService.getReunions());

        createNewReunionAction();
    }

    //  ****************************************** INIT  *******************************************
    private void setUpRecyclerView() {
        final RecyclerView rv = findViewById(R.id.maReu_list_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        rv.setAdapter(adapter);
    }

    //  ****************************************** MENU ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_sort_date: {
                setDateSorter();

                return true;
            }

            case R.id.action_filter_date: {
                setDateFilter();
                return true;
            }
            case R.id.action_filter_room: {
                setRoomsFilter();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  ****************************************** ACTIONS  ****************************************

    private void createNewReunionAction() {
        FloatingActionButton mButtonNewReunion = findViewById(R.id.button_add_reunion);
        mButtonNewReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReunionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDateSorter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        Collections.sort(mReunions, new Comparator<Reunion>() {
            public int compare(Reunion o1, Reunion o2) {
                if (o1.getStartDate() == null || o2.getStartDate() == null)
                    return 0;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        adapter.setData(mReunions);
    }

    private void setDateFilter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final DatePicker datePicker = new DatePicker(MainActivity.this);
        datePicker.setCalendarViewShown(false);

        builder.setView(datePicker);

        builder.setPositiveButton(R.string.filter_ok_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Calendar mCalendarPicker = Calendar.getInstance();
                mCalendarPicker.set(year, month, day);

                List<Reunion> mReunionsFiltered = new ArrayList<>();
                int size = mReunions.size();
                for (int e = 0; e < size; e++) {
                    Calendar mReunionsCalendar = Calendar.getInstance();
                    mReunionsCalendar.setTime(mReunions.get(e).getStartDate());
                    if (mCalendarPicker.get(Calendar.YEAR) == mReunionsCalendar.get(Calendar.YEAR)
                            && mCalendarPicker.get(Calendar.DAY_OF_YEAR) == mReunionsCalendar.get(Calendar.DAY_OF_YEAR)) {
                        mReunionsFiltered.add(mReunions.get(e));
                    }
                }
                adapter.setData(mReunionsFiltered);

            }
        });

        builder.setNeutralButton(R.string.filter_reset_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.setData(mReunions);
            }
        });

        builder.show();
    }

    private void setRoomsFilter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // String array for alert dialog multi choice items
        final int numberRooms = mMaReuApiService.getRooms().size();
        String[] mRooms = new String[numberRooms];
        for (int i = 0; i < numberRooms; i++) {
            mRooms[i] = mMaReuApiService.getRooms().get(i).getRoomName();
        }
        // Boolean array for initial selected items
        final boolean[] checkedRooms = new boolean[numberRooms];
        // Convert the Rooms array to list
        final List<String> mRoomsList = Arrays.asList(mRooms);

        // Set multiple choice items for alert dialog
        builder.setMultiChoiceItems(mRooms, checkedRooms, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedRooms[which] = isChecked;

/*                // Get the current focused item
                String currentItem = mRoomsList.get(which);*/
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle(R.string.filter_rooms_text);


        // Set the positive/yes button click listener
        builder.setPositiveButton(R.string.filter_ok_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        // Set the negative/no button click listener
        builder.setNeutralButton(R.string.filter_reset_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.setData(mReunions);
            }
        });

        final AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // Checks the checked rooms
                        List<Reunion> mReunionsFiltered = new ArrayList<>();
                        int sizeReunionsList = mReunions.size();
                        boolean atLeastOneChecked = false;
                        for (int i = 0; i < checkedRooms.length; i++) {
                            boolean checked = checkedRooms[i];
                            if (checked) {
                                atLeastOneChecked = true;
                                String mRoomFiltered = mRoomsList.get(i);
                                for (int e = 0; e < sizeReunionsList; e++) {
                                    if (mRoomFiltered.equals(mReunions.get(e).getRoom().getRoomName())) {
                                        mReunionsFiltered.add(mReunions.get(e));
                                    }
                                }
                            }
                        }
                        // if no rooms checked, Toast to alert user
                        if (!atLeastOneChecked) {
                            Toast toastRoomNotSelected = Toast.makeText(getApplicationContext(), R.string.toast_room_not_selected, Toast.LENGTH_LONG);
                            toastRoomNotSelected.setGravity(Gravity.CENTER, 0, 0);
                            View toastViewCreateReunion = toastRoomNotSelected.getView();
                            TextView toastTextCreateReunion = toastViewCreateReunion.findViewById(android.R.id.message);
                            toastTextCreateReunion.setTextColor(Color.parseColor(getString(R.string.toast_add_reunion_color)));
                            toastRoomNotSelected.show();
                        } // Otherwise, show the selection result
                        else {
                            dialog.dismiss();
                            adapter.setData(mReunionsFiltered);
                        }
                    }
                });
            }
        });
        dialog.show();

    }


}
