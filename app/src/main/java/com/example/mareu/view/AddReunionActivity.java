package com.example.mareu.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;
import com.example.mareu.service.MaReuApiService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddReunionActivity extends AppCompatActivity {

    private static final int DURATION_MIN_HOURS = 0, DURATION_STEP_MINUTES = 5, DURATION_MAX_HOURS = 3;

    private EditText mSubject;
    private Spinner mRoom;
    private MultiAutoCompleteTextView guestsEmails;
    private MaReuApiService mMaReuApiService;
    private final List<Guest> mGuests = new ArrayList<>();
    private Room mRoomReunion;
    private Date mStartDate;
    private NumberPicker durationMinutes, durationHours;
    private TextView startDatePickerText;
    private TextView startTimePickerText;

    private final Calendar datePickerCalendar = Calendar.getInstance();
    private final Calendar timePickerCalendar = Calendar.getInstance();

    private static final String SEPARATOR = ", ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        init();
        setRoomsArrayAdapter();
        setGuestsArrayAdapter();
    }

    //  *************************************** INIT  ***************************************

    /**
     * Instanciates the views and variables
     */
    private void init() {
        // ************************************ Toolbar init ***************************************
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ************************************ Layout bindings ************************************
        mMaReuApiService = DI.getMaReuApiService();
        mSubject = findViewById(R.id.subject_add_reunion);
        startDatePickerText = findViewById(R.id.select_start_date_add_reunion);
        startTimePickerText = findViewById(R.id.select_start_time_add_reunion);
        durationHours = findViewById(R.id.duration_hours_add_reunion);
        durationMinutes = findViewById(R.id.duration_minutes_add_reunion);
        mRoom = findViewById(R.id.room_add_reunion);
        guestsEmails = findViewById(R.id.guest_add_reunion);
        // ************************************ Layout Parametrization *****************************
        durationHours.setMaxValue(DURATION_MAX_HOURS);
        durationHours.setMinValue(DURATION_MIN_HOURS);
        setDurationsMinutesValues();
        setStartDatePickerDialog();
        setStartTimePickerDialog();
    }

    /**
     * DATEPICKER
     */
    private void setStartDatePickerDialog() {
        final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                datePickerCalendar.set(Calendar.YEAR, year);
                datePickerCalendar.set(Calendar.MONTH, monthOfYear);
                datePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDateLabel();
            }
        };

        startDatePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddReunionActivity.this, startDate, datePickerCalendar
                        .get(Calendar.YEAR), datePickerCalendar.get(Calendar.MONTH),
                        datePickerCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateStartDateLabel() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        startDatePickerText.setText(dateFormat.format(datePickerCalendar.getTime()));
    }

    /**
     * TIMEPICKER
     */
    private void setStartTimePickerDialog() {

        final TimePickerDialog.OnTimeSetListener startTime = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timePickerCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timePickerCalendar.set(Calendar.MINUTE, minute);
                updateStartTimeLabel();
            }
        };

        startTimePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(AddReunionActivity.this, startTime, timePickerCalendar
                        .get(Calendar.HOUR), timePickerCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
    }

    private void updateStartTimeLabel() {
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        startTimePickerText.setText(timeFormat.format(timePickerCalendar.getTime()));
    }

    /**
     * DURATION MINUTES
     */
    private void setDurationsMinutesValues() {
        String[] step15 = {"0", "15", "30", "45"};
        String[] step5 = {"0", "5", "10", "15", "20", "25", "30", "35", "40",
                "45", "50", "55"};
        final String[] mins;

        if (Objects.equals(Integer.toString(AddReunionActivity.DURATION_STEP_MINUTES), "15")) {
            mins = step15;
        } else {
            mins = step5;
        }

        int ml = mins.length - 1;

        // Prevent ArrayOutOfBoundExceptions by setting
        // values array to null so its not checked
        durationMinutes.setDisplayedValues(null);
        durationMinutes.setMinValue(0);
        durationMinutes.setMaxValue(ml);
        durationMinutes.setDisplayedValues(mins);
    }

    /**
     * ROOMS SPINNER - Sets the spinner for the Room selection
     */
    private void setRoomsArrayAdapter() {
        ArrayList<String> mRoomsList = new ArrayList<>();
        mRoomsList.add(0, getResources().getString(R.string.room_add_reunion_text));
        for (Room roomIterator : mMaReuApiService.getRooms()) {
            //String roomIteratorString = roomIterator.getRoomName() + " (" + roomIterator.getSeats() + getString(R.string.room_seats_text) + ")";
            String roomIteratorString = roomIterator.getRoomName();
            boolean roomAvailable = true;
/*            for (Reunion reunionIterator : mMaReuApiService.getReunions()) {
                // 1/ roomName
                // 2/ date / duration of the datepicker + duration vs each reunion with the same name
                // 3/
                if (reunionIterator.getRoom().getRoomName().equals(roomIterator.getRoomName())
                        && (getStartReunionDateTimeFromSelection().after(reunionIterator.getStartDate())
                        || getStartReunionDateTimeFromSelection().before((reunionIterator.getEndDate()))
                        || getEndReunionDateTimeFromSelection().after(reunionIterator.getStartDate())
                        || getEndReunionDateTimeFromSelection().before((reunionIterator.getEndDate()))))
                 {
                     roomAvailable = false;
                    break;
                }
            }*/
            if (roomAvailable && !mRoomsList.contains(roomIteratorString)) {
                mRoomsList.add(roomIteratorString);
                String[] mRoomsArray = mRoomsList.toArray(new String[0]);
                ArrayAdapter<String> adapterRooms
                        = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRoomsArray);
                mRoom.setAdapter(adapterRooms);
            }

        }
    }

    private boolean checkRoomAvailability(String roomName, Date startDate, Date endDate) {
        boolean roomAvailable = false;
        for (Reunion reunionIterator : mMaReuApiService.getReunions()) {
            if (roomName.equals(reunionIterator.getRoom().getRoomName())
                    && ((startDate.after(reunionIterator.getStartDate())
                    && startDate.before((reunionIterator.getEndDate())))
                    || (endDate.after(reunionIterator.getStartDate())
                    && endDate.before(reunionIterator.getEndDate())))) {
                roomAvailable = true;
                break;
            }
        }
        return roomAvailable;
    }

    /**
     * GUESTS LIST MENU - Sets the autocompletion for the Guests selection
     */
    private void setGuestsArrayAdapter() {
        String[] guestsEmailsList = mMaReuApiService.getGuestsEmails(mMaReuApiService.getGuests()).toArray(new String[0]);
        // TODO Gérer le cas d'ajout a partir du nom / prénom et pas qu'à partir de l'email
        ArrayAdapter<String> adapterGuests
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guestsEmailsList);
        guestsEmails.setAdapter(adapterGuests);
        guestsEmails.setThreshold(1);                                                  // Sets the minimum number of characters, to show suggestions
        guestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());     // then separates them with a comma
    }

    //  *************************************** SAVE REUNION ***************************************

    /**
     * REUNION CREATION BUTTON - Creates the reunion
     */
    private void createReunion() {
        String mSubjectString = mSubject.getText().toString();
        getGuestsFromEmailsSelected();
        getRoomFromRoomNameSelected();
        mStartDate = getStartReunionDateTimeFromSelection();
        Date mEndDate = getEndReunionDateTimeFromSelection();
        // Avoids reunion creation if the duration is 0h0min *******************************
        if (mSubjectString.isEmpty()) {
            toastCancelCreation(R.string.toast_subject_empty);
        } else if (durationHours.getValue() == 0 && durationMinutes.getValue() == 0) {
            toastCancelCreation(R.string.toast_duration_empty);
        } else if (mRoom.getSelectedItem().toString().equals(getResources().getString(R.string.room_add_reunion_text))) {
            toastCancelCreation(R.string.toast_room_empty);
        } else if (mGuests.isEmpty()) {
            toastCancelCreation(R.string.toast_guests_empty);
        } else if (checkRoomAvailability(mRoomReunion.getRoomName(), mStartDate, mEndDate)) {
            toastCancelCreation(R.string.toast_room_unavailable);
        } else {
            Reunion mReunion = new Reunion(
                    System.currentTimeMillis(),
                    mSubjectString,
                    mStartDate,
                    mEndDate,
                    mRoomReunion,
                    mGuests);
            mMaReuApiService.addReunion(mReunion);

            Intent intent = new Intent(AddReunionActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Gets Date & Time from the pickers
     */
    private Date getStartReunionDateTimeFromSelection() {
        // Creating a calendar
        Calendar mCalendar = Calendar.getInstance();
        // Replacing with a new value - date from datePicker, time from timePicker
        mCalendar.setTime(datePickerCalendar.getTime());
        mCalendar.set(Calendar.HOUR, timePickerCalendar.get(Calendar.HOUR));
        mCalendar.set(Calendar.MINUTE, timePickerCalendar.get(Calendar.MINUTE));
        return mCalendar.getTime();
    }

    private Date getEndReunionDateTimeFromSelection() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mStartDate);
        mCalendar.add(Calendar.HOUR_OF_DAY, durationHours.getValue());
        mCalendar.add(Calendar.MINUTE, durationMinutes.getValue());
        return mCalendar.getTime();
    }

    /**
     * Gets the Guests from the emails chosen in the form
     */
    private void getGuestsFromEmailsSelected() {
        for (String e : guestsEmails.getText().toString().split(SEPARATOR)) {
            for (Guest eG : mMaReuApiService.getGuests()) {
                // Avoid duplicated Guest in the Reunion
                if (e.equals(eG.getEmail()) && !mGuests.contains(eG)) {
                    mGuests.add(eG);
                }
            }
        }
    }

    /**
     * Gets the Room object from the Room name selected in the Spinner
     */
    private void getRoomFromRoomNameSelected() {
        for (Room r : mMaReuApiService.getRooms()) {
            if (r.getRoomName().equals(mRoom.getSelectedItem().toString())) {
                mRoomReunion = r;
            }
        }


    }

    private void toastCancelCreation(int intString) {
        Toast toastCreateReunion = Toast.makeText(getApplicationContext(), intString, Toast.LENGTH_LONG);
        toastCreateReunion.setGravity(Gravity.CENTER, 0, 0);
        View toastViewCreateReunion = toastCreateReunion.getView();
        TextView toastTextCreateReunion = toastViewCreateReunion.findViewById(android.R.id.message);
        toastTextCreateReunion.setTextColor(Color.parseColor(getString(R.string.toast_add_reunion_color)));
        toastCreateReunion.show();
    }

    //  *************************************** ACTIONS ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_reunion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create_reunion_button) {
            createReunion();
            return true;
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }
}






