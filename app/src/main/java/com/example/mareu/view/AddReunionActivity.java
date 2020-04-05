package com.example.mareu.view;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;
import com.example.mareu.service.MaReuApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AddReunionActivity extends AppCompatActivity {

    private Button mButtonCreateReunion;
    private EditText mSubject;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private TimePicker mDurationPicker;
    private Spinner mRoom;
    private MultiAutoCompleteTextView mGuestsEmails;
    private Reunion mReunion;
    private MaReuApiService mMaReuApiService;

    private List<Guest> mGuests = new ArrayList<>();
    private Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        init();
        roomsArrayAdapter();
        guestsArrayAdapter();

        Calendar mCalendar = Calendar.getInstance();


        mButtonCreateReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuestsFromEmailsSelected();
                getDateTimeFromSelection();
                Calendar mCalendar = Calendar.getInstance();
                // Replacing with a new value
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationPicker.getHour(), mDurationPicker.getMinute());
                } else {
                    mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mDurationPicker.getCurrentHour(), mDurationPicker.getCurrentMinute());
                }
                Date mDurationDate = mCalendar.getTime();

                // TODO Check les horaires de réunions avant création ( Passer par l'activity )
                // TODO Ajouter la durée de réunion
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

    private void guestsArrayAdapter() {
        String[] guestsEmailsList = mMaReuApiService.getGuestsEmails(mMaReuApiService.getGuests()).toArray(new String[0]);
        // TODO Gérer le cas d'ajout a partir du nom / prénom et pas qu'à partir de l'email
        ArrayAdapter<String> adapterGuests
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guestsEmailsList);
        mGuestsEmails.setAdapter(adapterGuests);
        mGuestsEmails.setThreshold(1);                                                  // Sets the minimum number of characters, to show suggestions
        mGuestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());     // then separates them with a comma
    }

    private void roomsArrayAdapter() {
        ArrayList<String> mRoomsList = new ArrayList<String>();
        for (Room r : mMaReuApiService.getRooms())
            mRoomsList.add(r.getRoomName() + " (" + r.getSeats() + "places)");
        String[] mRoomsArray = mRoomsList.toArray(new String[0]);
        ArrayAdapter<String> adapterRooms
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRoomsArray);
        mRoom.setAdapter(adapterRooms);
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
        mDurationPicker = findViewById(R.id.duration_add_reunion);
        mDurationPicker.setIs24HourView(true);
        mRoom = findViewById(R.id.room_add_reunion);
        mGuestsEmails = findViewById(R.id.guest_add_reunion);

        mSubject.addTextChangedListener(createTextWatcher);
        mGuestsEmails.addTextChangedListener(createTextWatcher);
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

    private void getGuestsFromEmailsSelected() {
        // Gets the Guests from the emails chosen in the form
        for (String e : mGuestsEmails.getText().toString().split(", ")) {
            for (Guest eG : mMaReuApiService.getGuests()) {
                // Avoid duplicated Guest in the Reunion
                if (e.equals(eG.getEmail()) && !mGuests.contains(eG)) {
                    mGuests.add(eG);
                }
            }
        }
    }

}


