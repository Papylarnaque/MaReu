package com.example.mareu.view;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddReunionActivity extends AppCompatActivity {

    private Button mButtonCreateReunion;
    private EditText mSubject;
    private EditText mTime;
    private AutoCompleteTextView mRoom;
    private MultiAutoCompleteTextView mGuestsEmails;
    private Reunion mReunion;
    private MaReuApiService mMaReuApiService;
// TODO Rediriger les listes String ver les DummyGenerators generateGuestsList et genertae RoomsList
    private String[] mRoomsList = {"Vietnam","England","Canada", "France","Australia"};

    private List<String> mGuestsEmailsList;
    //={"Java ","CSharp","Visual Basic","Swift","C/C++"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        init();

        ArrayAdapter adapterRooms
                = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mRoomsList);

        mRoom.setAdapter(adapterRooms);

        // Set the minimum number of characters, to show suggestions
        mRoom.setThreshold(1);

        mGuestsEmailsList = mMaReuApiService.getGuestsEmails(mMaReuApiService.getGuestList());

        ArrayAdapter adapterGuests
                = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mGuestsEmailsList);

        mGuestsEmails.setAdapter(adapterGuests);

        mGuestsEmails.setThreshold(1);

        // The text separated by commas
        mGuestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mButtonCreateReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Gérer le cas d'une adresse mail incorrecte
                // TODO Autocomplétion du texte pour obtenir l'adresse mail en saisie
                //String mGuestsEmails = mMaReuApiService.getGuestsEmails(mReunion.getGuestList());
                /*mReunion = new Reunion(
                        System.currentTimeMillis(),
                        mSubject.getText().toString(),
                        mTime.getText().toString(),
                        mRoom.getText().toString(),
                        mGuestsEmails);*/



            }
        });
    }

    private void init() {
        mMaReuApiService = DI.getMaReuApiService();
        mButtonCreateReunion = findViewById(R.id.button_create_reunion);
        mSubject = findViewById(R.id.subject_add_reunion);
        mTime = findViewById(R.id.time_add_reunion);
        mRoom = findViewById(R.id.room_add_reunion);
        mGuestsEmails = findViewById(R.id.guest_text_view);

        mSubject.addTextChangedListener(createTextWatcher);
        mTime.addTextChangedListener(createTextWatcher);
        mRoom.addTextChangedListener(createTextWatcher);
        mGuestsEmails.addTextChangedListener(createTextWatcher);
    }


    TextWatcher createTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void afterTextChanged(Editable editable) {
            String mSubjectInput = mSubject.getText().toString();
            String mTimeInput = mTime.getText().toString();
            String mRoomInput = mRoom.getText().toString();

            if (!mSubjectInput.isEmpty()
                    && !mTimeInput.isEmpty()
                    && !mRoomInput.isEmpty()) {
                mButtonCreateReunion.setEnabled(true);
                mButtonCreateReunion.setBackgroundColor(getResources().getColor(R.color.colorButton));
            } else {
                mButtonCreateReunion.setEnabled(false);
                mButtonCreateReunion.setBackgroundColor(getResources().getColor(R.color.colorDisabledButton));
            }
        }


    };
}


