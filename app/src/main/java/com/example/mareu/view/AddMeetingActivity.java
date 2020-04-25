package com.example.mareu.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.ApiService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity {

    public static final String EMAILS_LIST_SEPARATOR = ", "; // Separator for listview in UI
    private static final int DURATION_MAX_HOURS = 3; // Max duration for a meeting (in hours)
    private static final int DURATION_STEP_MINUTES = 5; // Duration interval for minutes

    public final List<Guest> mGuests = new ArrayList<>();
    public Spinner mRoom;
    public MultiAutoCompleteTextView guestsEmails;

    private final Calendar datePickerCalendar = Calendar.getInstance();
    private final Calendar timePickerCalendar = Calendar.getInstance();
    private ApiService apiService;
    private Room mRoomReunion;
    private Date mStartDate;
    private NumberPicker durationMinutes, durationHours;
    private EditText mSubject;
    private TextView startDatePickerText, startTimePickerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        init();
    }

    /**************************************** INIT  ***************************************/
    // Instanciates the views and variables
    private void init() {
        // ************************************ Toolbar init ***************************************
        Toolbar toolbar = findViewById(R.id.toolbar_add_meeting);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        // FOR UI
        // ************************************ Layout bindings ************************************
        apiService = DI.getApiService();
        mSubject = findViewById(R.id.edit_text_add_meeting_subject);
        startDatePickerText = findViewById(R.id.text_add_meeting_datepicker);
        startTimePickerText = findViewById(R.id.text_add_meeting_timepicker);
        durationHours = findViewById(R.id.numberpicker_add_meeting_duration_hours_);
        durationMinutes = findViewById(R.id.numberpicker_add_meeting_duration_minutes_);
        mRoom = findViewById(R.id.spinner_add_meeting_room);
        guestsEmails = findViewById(R.id.autocomplete_text_add_meeting_guests);
        // ************************************ Layout Parametrization *****************************
        durationHours.setMaxValue(DURATION_MAX_HOURS);
        durationHours.setMinValue(0);
        setDurationsMinutesValues();
        setStartDatePickerDialog();
        setStartTimePickerDialog();
        setRoomsArrayAdapter();
        setGuestsArrayAdapter();
    }

    // DATEPICKER
    private void setStartDatePickerDialog() {
        final DatePickerDialog.OnDateSetListener startDate = (view, year, monthOfYear, dayOfMonth) -> {
            datePickerCalendar.set(Calendar.YEAR, year);
            datePickerCalendar.set(Calendar.MONTH, monthOfYear);
            datePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartDateLabel();
        };
        startDatePickerText.setOnClickListener(v -> new DatePickerDialog(AddMeetingActivity.this, startDate, datePickerCalendar
                .get(Calendar.YEAR), datePickerCalendar.get(Calendar.MONTH),
                datePickerCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateStartDateLabel() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        startDatePickerText.setText(dateFormat.format(datePickerCalendar.getTime()));
    }

    // TIMEPICKER
    private void setStartTimePickerDialog() {
        final TimePickerDialog.OnTimeSetListener startTime = (view, hourOfDay, minute) -> {
            timePickerCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timePickerCalendar.set(Calendar.MINUTE, minute);
            updateStartTimeLabel();
        };
        startTimePickerText.setOnClickListener(v -> new TimePickerDialog(AddMeetingActivity.this, startTime, timePickerCalendar
                .get(Calendar.HOUR), timePickerCalendar.get(Calendar.MINUTE),
                true).show());
    }

    private void updateStartTimeLabel() {
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        startTimePickerText.setText(timeFormat.format(timePickerCalendar.getTime()));
    }

    // DURATION MINUTES
    private void setDurationsMinutesValues() {
        int sizeSteps = 60 / DURATION_STEP_MINUTES;
        String[] mins = new String[sizeSteps];
        for (int i = 0; i < sizeSteps; i++) {
            mins[i] = String.valueOf(DURATION_STEP_MINUTES * i);
        }
        int ml = sizeSteps - 1;
        // Prevent ArrayOutOfBoundExceptions by setting values array to null so its not checked
        durationMinutes.setDisplayedValues(null);
        durationMinutes.setMinValue(0);
        durationMinutes.setMaxValue(ml);
        durationMinutes.setDisplayedValues(mins);
    }

    // ROOM AVAILABILITY CHECKER
    private boolean checkRoomAvailability(String roomName, Date startDate, Date endDate) {
        boolean roomAvailable = false;
        for (Meeting meetingIterator : apiService.getReunions()) {
            if (roomName.equals(meetingIterator.getRoom().getRoomName())
                    && ((startDate.after(meetingIterator.getStartDate())
                    && startDate.before((meetingIterator.getEndDate())))
                    || (endDate.after(meetingIterator.getStartDate())
                    && endDate.before(meetingIterator.getEndDate())))) {
                roomAvailable = true;
                break;
            }
        }
        return roomAvailable;
    }

    // GUESTS LIST MENU - Sets the autocompletion for the Guests selection
    private void setGuestsArrayAdapter() {
        String[] guestsEmailsList = apiService.getGuestsEmails(apiService.getGuests()).toArray(new String[0]);
        ArrayAdapter<String> adapterGuests
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guestsEmailsList);
        guestsEmails.setAdapter(adapterGuests);
        guestsEmails.setThreshold(1);                                                  // Sets the minimum number of characters, to show suggestions
        guestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());     // then separates them with a comma
    }

    // ROOMS SPINNER - Sets the spinner for the Room selection
    private void setRoomsArrayAdapter() {
        ArrayList<String> mRoomsList = new ArrayList<>();
        mRoomsList.add(0, getResources().getString(R.string.add_meeting_room));
        for (Room roomIterator : apiService.getRooms()) {
            String roomIteratorString = roomIterator.getRoomName();
            if (!mRoomsList.contains(roomIteratorString)) {
                mRoomsList.add(roomIteratorString);
                String[] mRoomsArray = mRoomsList.toArray(new String[0]);
                ArrayAdapter<String> adapterRooms
                        = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRoomsArray);
                mRoom.setAdapter(adapterRooms);
            }
        }
    }


    /*************************************** SAVE REUNION ***********************************/
    // REUNION CREATION BUTTON - Creates the meeting
    private void createReunion() {
        String mSubjectString = mSubject.getText().toString();
        apiService.getGuestsFromEmailsSelected(this);
        getRoomFromRoomNameSelected();
        mStartDate = getStartReunionDateTimeFromSelection();
        Date mEndDate = getEndReunionDateTimeFromSelection();
        // Avoids meeting creation if the duration is 0h0min *******************************
        if (mSubjectString.isEmpty()) {
            toastCancelCreation(R.string.toast_subject_empty);
        } else if (durationHours.getValue() == 0 && durationMinutes.getValue() == 0) {
            toastCancelCreation(R.string.toast_duration_empty);
        } else if (mRoom.getSelectedItem().toString().equals(getResources().getString(R.string.add_meeting_room))) {
            toastCancelCreation(R.string.toast_room_empty);
        } else if (mGuests.isEmpty()) {
            toastCancelCreation(R.string.toast_guests_empty);
        } else if (checkRoomAvailability(mRoomReunion.getRoomName(), mStartDate, mEndDate)) {
            toastCancelCreation(R.string.toast_room_unavailable);
        } else {
            Meeting mMeeting = new Meeting(
                    System.currentTimeMillis(),
                    mSubjectString,
                    mStartDate,
                    mEndDate,
                    mRoomReunion,
                    mGuests);
            apiService.addReunion(mMeeting);

            Intent intent = new Intent(AddMeetingActivity.this, ListActivity.class);
            startActivity(intent);
        }
    }

    // Get Date & Time from the pickers
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
        mCalendar.add(Calendar.MINUTE, durationMinutes.getValue() * DURATION_STEP_MINUTES);
        return mCalendar.getTime();
    }

    // Gets the Room object from the Room name selected in the Spinner
    private void getRoomFromRoomNameSelected() {
        for (Room r : apiService.getRooms()) {
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
        toastTextCreateReunion.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toast_add_meeting_color));
        toastCreateReunion.show();
    }

    /*************************************** MENU *****************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_meeting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_button_create_meeting) {
            createReunion();
            return true;
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

}






