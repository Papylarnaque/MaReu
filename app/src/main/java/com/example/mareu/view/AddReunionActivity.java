package com.example.mareu.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddReunionActivity extends AppCompatActivity {

    private static final int INTERVAL_MINUTES_TIMEPICKER = 10, DURATION_MIN_HOURS = 0, DURATION_STEP_MINUTES = 5, DURATION_MAX_HOURS = 3;

    private EditText mSubject;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Spinner mRoom;
    private MultiAutoCompleteTextView mGuestsEmails;
    private MaReuApiService mMaReuApiService;
    private List<Guest> mGuests = new ArrayList<>();
    private Room mRoomReunion;
    private Date mStartDate, mEndDate;
    private NumberPicker minutePicker, mDurationMinutes, mDurationHours;

    private static final DecimalFormat FORMATTER = new DecimalFormat("00");
    private static final String SEPARATOR = ", ";

/*    EditText mDatePicker, mTimePicker;
    int mYear, mMonth, mDay, mHour, mMinute;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        init();
        setRoomsArrayAdapter();
        setGuestsArrayAdapter();
        setTimePickerInterval();
/*      mDatePicker = (EditText) findViewById(R.id.date_add_reunion_text);
        mTimePicker = (EditText) findViewById(R.id.time_add_reunion_text);
        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);*/
    }

    //  *************************************** INIT  ***************************************

    /**
     * Instanciates the views and variables
     *
     * @return
     */
    private void init() {
        // ************************************ Toolbar init ***************************************
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_reunion_title);
        setSupportActionBar(toolbar);
        // ************************************ Layout bindings ************************************
        mMaReuApiService = DI.getMaReuApiService();
        mSubject = findViewById(R.id.subject_add_reunion);
        mDatePicker = findViewById(R.id.date_add_reunion);
        mTimePicker = findViewById(R.id.time_add_reunion);
        mDurationHours = findViewById(R.id.duration_hours_add_reunion);
        mDurationMinutes = findViewById(R.id.duration_minutes_add_reunion);
        mRoom = findViewById(R.id.room_add_reunion);
        mGuestsEmails = findViewById(R.id.guest_add_reunion);
        // ************************************ Layout Parametrization *****************************
        mDatePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        mTimePicker.setIs24HourView(true);

        mDurationHours.setMaxValue(DURATION_MAX_HOURS);
        mDurationHours.setMinValue(DURATION_MIN_HOURS);
        setDurationsMinutesValues();


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddReunionActivity.this);

                final DatePicker mDatePicker = new DatePicker(AddReunionActivity.this);
                mDatePicker.setCalendarViewShown(false);

                builder.setTitle(R.string.filter_date_text);
                builder.setView(mDatePicker);
                builder.setMessage(R.string.action_filter_date_text)
                        .setPositiveButton(R.string.filter_date_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        })
                        .setNegativeButton(R.string.filter_cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.show();
            }

        });
    }


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
        mDurationMinutes.setDisplayedValues(null);

        mDurationMinutes.setMinValue(0);
        mDurationMinutes.setMaxValue(ml);
        mDurationMinutes.setDisplayedValues(mins);
    }

    /**
     * ROOMS SPINNER - Sets the spinner for the Room selection
     */
    private void setRoomsArrayAdapter() {
        ArrayList<String> mRoomsList = new ArrayList<String>();
        mRoomsList.add(0, getResources().getString(R.string.room_add_reunion_text));
        for (Room roomIterator : mMaReuApiService.getRooms()) {
            //String roomIteratorString = roomIterator.getRoomName() + " (" + roomIterator.getSeats() + getString(R.string.room_seats_text) + ")";
            String roomIteratorString = roomIterator.getRoomName();
/*            int test = 0;
            for (Reunion reunionIterator : mMaReuApiService.getReunions()) {
                // TODO Check if the room is available
                if (reunionIterator.getRoom().equals(roomIterator)  //&& (reunionIterator.getDuration().after(reunionIterator.getDate()))
                        || mRoomsList.contains(roomIteratorString)) {
                    test = 1;
                    break;
                }
            }
            if (test == 0) {*/
            mRoomsList.add(roomIteratorString);
            String[] mRoomsArray = mRoomsList.toArray(new String[0]);
            ArrayAdapter<String> adapterRooms
                    = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRoomsArray);
            mRoom.setAdapter(adapterRooms);
        }

    }

    /**
     * GUESTS LIST MENU - Sets the autocompletion for the Guests selection
     */
    private void setGuestsArrayAdapter() {
        String[] guestsEmailsList = mMaReuApiService.getGuestsEmails(mMaReuApiService.getGuests()).toArray(new String[0]);
        // TODO Gérer le cas d'ajout a partir du nom / prénom et pas qu'à partir de l'email
        ArrayAdapter<String> adapterGuests
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guestsEmailsList);
        mGuestsEmails.setAdapter(adapterGuests);
        mGuestsEmails.setThreshold(1);                                                  // Sets the minimum number of characters, to show suggestions
        mGuestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());     // then separates them with a comma
    }

    /**
     * Sets the Minutes interval for TimePicker
     */
    private void setTimePickerInterval() {

        int numValues = 60 / INTERVAL_MINUTES_TIMEPICKER;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL_MINUTES_TIMEPICKER);
        }

        View minute = mTimePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }

        if (minutePicker != null) {
            minutePicker.getValue();
        } else {
            mTimePicker.getCurrentMinute();
        }
    }

    //  *************************************** SAVE REUNION ***************************************

    /**
     * REUNION CREATION BUTTON - Creates the reunion
     */
    private void createReunion() {
        getGuestsFromEmailsSelected();
        getDateTimeFromSelection();
        getDurationReunion();
        getRoomFromRoomNameSelected();
        String mSubjectString = mSubject.getText().toString();
        // Avoids reunion creation if the duration is 0h0min *******************************
        if (mSubjectString.isEmpty()) {
            toastCancelCreation(R.string.toast_subject_empty);
        } else if (mDurationHours.getValue() == 0 && mDurationMinutes.getValue() == 0) {
            toastCancelCreation(R.string.toast_duration_empty);
        } else if (mRoom.getSelectedItem().toString().equals(getResources().getString(R.string.room_add_reunion_text))) {
            toastCancelCreation(R.string.toast_room_empty);

        } else {
            //mRoom.getSelectedItem().toString(),
            Reunion mReunion = new Reunion(
                    System.currentTimeMillis(),
                    mSubject.getText().toString(),
                    mStartDate,
                    mEndDate,
                    //mRoom.getSelectedItem().toString(),
                    mRoomReunion,
                    mGuests);
            mMaReuApiService.addReunion(mReunion);

            Intent intent = new Intent(AddReunionActivity.this, ListReunionActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Gets Date & Time from the pickers
     */
    private void getDateTimeFromSelection() {
        // Creating a calendar
        Calendar mCalendar = Calendar.getInstance();
        // Replacing with a new value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getHour(), mTimePicker.getMinute());
        } else {
            mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        }
        mStartDate = mCalendar.getTime();
    }

    private void getDurationReunion() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mStartDate);
        //mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationHours.getValue(), mDurationMinutes.getValue());
        mCalendar.set(Calendar.HOUR_OF_DAY, mDurationHours.getValue());
        mCalendar.set(Calendar.MINUTE, mDurationMinutes.getValue());
        mEndDate = mCalendar.getTime();
    }

    /**
     * Gets the Guests from the emails chosen in the form
     */
    private void getGuestsFromEmailsSelected() {
        for (String e : mGuestsEmails.getText().toString().split(SEPARATOR)) {
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
        TextView toastTextCreateReunion = (TextView) toastViewCreateReunion.findViewById(android.R.id.message);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.create_reunion_button) {
            createReunion();
            return true;
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }


    //  *************************************** ARCHIVES *******************************************

/*        mSubject.addTextChangedListener(createTextWatcher);
        mGuestsEmails.addTextChangedListener(createTextWatcher);*/

    /*   /**
     * TEXTWATCHER - Checks fulfilment of the form before creating the reunion
     * /
    private final TextWatcher createTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String mSubjectInput = mSubject.getText().toString();
            String mRoomInput = mRoom.toString();
            getGuestsFromEmailsSelected();
            if (!mSubjectInput.isEmpty()
                    && !mRoomInput.isEmpty()
                    && !mGuests.isEmpty()) {
                mButtonCreateReunion.setEnabled(true);
                mButtonCreateReunion.setBackgroundColor(getResources().getColor(R.color.colorButton));
            } else {
                mButtonCreateReunion.setEnabled(false);
                mButtonCreateReunion.setBackgroundColor(getResources().getColor(R.color.colorDisabledButton));
            }
        }
    };*/


/*    @Override
    public void onClick(View v) {

        if (v == mDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mDatePicker.setText(strDate);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            final String strDate = dateFormat.format(Calendar.getInstance().getTime());
        }
        if (v == mTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            DateFormat dateFormat = DateFormat.getTimeInstance((DateFormat.MEDIUM));
            final String strDate = dateFormat.format(Calendar.getInstance().getTime());

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            mTimePicker.setText(strDate);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }


    }*/


}






