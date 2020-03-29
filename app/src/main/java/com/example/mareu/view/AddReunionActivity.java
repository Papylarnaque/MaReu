package com.example.mareu.view;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddReunionActivity extends AppCompatActivity {

    private Button mButtonCreateReunion;
    private EditText mSubject;
    private EditText mTime;
    private EditText mRoom;
    private EditText mGuestsEmails;
    private Reunion mReunion;
    private MaReuApiService mMaReuApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        init();


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


