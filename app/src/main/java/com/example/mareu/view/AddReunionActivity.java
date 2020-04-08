package com.example.mareu.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;

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

import androidx.appcompat.app.AppCompatActivity;

public class AddReunionActivity extends AppCompatActivity /*implements
        View.OnClickListener*/ {

    private Button mButtonCreateReunion;
    private EditText mSubject;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker, mDurationPicker;
    private Spinner mRoom;
    private MultiAutoCompleteTextView mGuestsEmails;
    private Reunion mReunion;
    private MaReuApiService mMaReuApiService;
    private List<Guest> mGuests = new ArrayList<>();
    private Date mDate;

    //private static final int INTERVAL_MINUTES_DURATIONPICKER = 10;
    private static final int INTERVAL_MINUTES_TIMEPICKER = 10;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    private NumberPicker minutePicker;
    private NumberPicker mDurationMinutes;
    private NumberPicker mDurationHours;

    private static final int DURATION_MIN_HOURS = 0;
    private static final int DURATION_MIN_MINUTES = 0;
    private static final int DURATION_MAX_HOURS = 3;
    private static final int DURATION_MAX_MINUTES = 55;



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
/*        setDurationPickerInterval();*/


/*        mDatePicker = (EditText) findViewById(R.id.date_add_reunion_text);
        mTimePicker = (EditText) findViewById(R.id.time_add_reunion_text);
        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);*/

        Calendar mCalendar = Calendar.getInstance();


/*        mDurationPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setHour(4);
                } else {
                    timePicker.setCurrentHour(4);
                }
            }
        });*/

        mButtonCreateReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuestsFromEmailsSelected();
                getDateTimeFromSelection();
                Calendar mCalendar = Calendar.getInstance();
                // Replacing with a new value
/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationPicker.getHour(), mDurationPicker.getMinute());
                } else {
                    mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationPicker.getCurrentHour(), mDurationPicker.getCurrentMinute());
                }*/
                mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationHours.getValue(), mDurationMinutes.getValue());
                Date mDurationDate = mCalendar.getTime();


                // TODO Check les horaires de réunions avant création ( Passer par l'activity )
                // TODO Ne pas créer la réunion si pas de guest trouvé à partir de la saisie
                mReunion = new Reunion(
                        System.currentTimeMillis(),
                        mSubject.getText().toString(),
                        mDate,
                        mDurationDate,
                        mRoom.getSelectedItem().toString(),
                        mGuests);
                mMaReuApiService.addReunion(mReunion);

                Intent intent = new Intent(AddReunionActivity.this, ListReunionActivity.class);
                startActivity(intent);
            }

        });
    }


    /**
     * Instanciates the views and variables
     */
    private void init() {
        mMaReuApiService = DI.getMaReuApiService();
        mButtonCreateReunion = findViewById(R.id.button_create_reunion);
        mSubject = findViewById(R.id.subject_add_reunion);
        mDatePicker = findViewById(R.id.date_add_reunion);
        mTimePicker = findViewById(R.id.time_add_reunion);
        mTimePicker.setIs24HourView(true);
        // Manages version Api < 23 for Time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(mTimePicker.getHour());
        } else {
            mTimePicker.setCurrentHour(mTimePicker.getCurrentHour());
        }
/*        mDurationPicker = findViewById(R.id.duration_add_reunion);
        mDurationPicker.setIs24HourView(true);*/
        mDurationHours = findViewById(R.id.duration_hours_add_reunion);
        mDurationMinutes = findViewById(R.id.duration_minutes_add_reunion);
        mDurationHours.setMaxValue(DURATION_MAX_HOURS);
        mDurationHours.setMinValue(DURATION_MIN_HOURS);
        mDurationMinutes.setMaxValue(DURATION_MAX_MINUTES);
        mDurationMinutes.setMinValue(DURATION_MIN_MINUTES);
        mRoom = findViewById(R.id.room_add_reunion);
        mGuestsEmails = findViewById(R.id.guest_add_reunion);

        mSubject.addTextChangedListener(createTextWatcher);
        mGuestsEmails.addTextChangedListener(createTextWatcher);
    }

    /**
     * Sets the autocompletion for the Guests selection
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
     * Sets the spinner for the Room selection
     */
    private void setRoomsArrayAdapter() {
        ArrayList<String> mRoomsList = new ArrayList<String>();
        for (Room r : mMaReuApiService.getRooms())
            mRoomsList.add(r.getRoomName() + " (" + r.getSeats() + "places)");
        String[] mRoomsArray = mRoomsList.toArray(new String[0]);
        ArrayAdapter<String> adapterRooms
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRoomsArray);
        mRoom.setAdapter(adapterRooms);
    }


    /**
     * Checks fulfilment of the form before creating the reunion
     */
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


    };

    private void getDateTimeFromSelection() {
        // Creating a calendar
        Calendar mCalendar = Calendar.getInstance();
        // Replacing with a new value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getHour(), mTimePicker.getMinute());
        } else {
            mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        }
        mDate = mCalendar.getTime();
    }

    /**
     * Gets the Guests from the emails chosen in the form
     */
    private void getGuestsFromEmailsSelected() {
        for (String e : mGuestsEmails.getText().toString().split(", ")) {
            for (Guest eG : mMaReuApiService.getGuests()) {
                // Avoid duplicated Guest in the Reunion
                if (e.equals(eG.getEmail()) && !mGuests.contains(eG)) {
                    mGuests.add(eG);
                }
            }
        }
    }

    /**
     * Sets the Minutes interval for TimePicker
     */
    public void setTimePickerInterval() {

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

/*    *//**
     * Sets the Minutes interval for DurationPicker
     *//*
    public void setDurationPickerInterval() {

        int numValues = 60 / INTERVAL_MINUTES_DURATIONPICKER;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL_MINUTES_DURATIONPICKER);
        }

        View minute = mDurationPicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }

        if (minutePicker != null) {
            minutePicker.getValue();
        } else {
            mDurationPicker.getCurrentMinute();
        }
    }*/



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






