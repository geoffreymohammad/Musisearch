package com.project.android.musisearch.ui.register;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.project.android.musisearch.R;
import com.project.android.musisearch.utils.Sessions;

import java.util.Calendar;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class Step1Activity extends AppCompatActivity {
    private SharedPreferences sharedPreferencesGoogle;
    TextInputLayout textInputFullName, textInputEmail, textInputTelp, textInputDate;
    EditText editFullName, editEmail, editTelp, editDate;
    DatePickerDialog  datePicker;
    Button btnNext;
    int gender = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);

        btnNext = findViewById(R.id.btnNext);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editTelp = findViewById(R.id.editTelp);
        editDate = findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(Step1Activity.this,R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDate.setText(year + "-"+(monthOfYear + 1) + "-" + dayOfMonth );
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });
        editFullName.setText(sharedPreferencesGoogle.getString("fullname", null));
        editEmail.setText(sharedPreferencesGoogle.getString("email", null));
        editTelp.setText(sharedPreferencesGoogle.getString("phoneNumber", null));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    Sessions.setupSessionLoginGoogle(Step1Activity.this, 0, editFullName.getText().toString(), editEmail.getText().toString(), editTelp.getText().toString(), editDate.getText().toString(), null);
                    Intent i = new Intent(Step1Activity.this, Step2Activity.class);
                    i.putExtra("fullname", editFullName.getText().toString());
                    i.putExtra("email", editEmail.getText().toString());
                    i.putExtra("phoneNumber", editTelp.getText().toString());
                    i.putExtra("tglLahir", editDate.getText().toString());
                    i.putExtra("gender", gender);
                    startActivity(i);
                }
            }
        });
    }


    private boolean validate() {
        textInputFullName = findViewById(R.id.textInputFullName);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputTelp = findViewById(R.id.textInputTelp);
        textInputDate = findViewById(R.id.textInputDate);

        boolean valid = true;
        String fullName = editFullName.getText().toString();
        String email = editEmail.getText().toString();
        String telp = editTelp.getText().toString();
        String date = editDate.getText().toString();
        if (fullName.isEmpty()) {
            textInputFullName.setErrorEnabled(true);
            textInputFullName.setError("Please fill out this field");
            valid = false;
        } else {
            textInputFullName.setError(null);
            textInputFullName.setErrorEnabled(false);
        }
        if (email.isEmpty()) {
            textInputEmail.setErrorEnabled(true);
            textInputEmail.setError("Please fill out this field");
            valid = false;
        } else {
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
        }
        if (telp.isEmpty()) {
            textInputTelp.setErrorEnabled(true);
            textInputTelp.setError("Please fill out this field");
            valid = false;
        } else {
            textInputTelp.setError(null);
            textInputTelp.setErrorEnabled(false);
        }
        if (date.isEmpty()) {
            textInputDate.setErrorEnabled(true);
            textInputDate.setError("Please fill out this field");
            valid = false;
        } else {
            textInputDate.setError(null);
            textInputDate.setErrorEnabled(false);
        }
        return valid;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender = 1;
                    break;
            case R.id.radio_female:
                if (checked)
                    gender = 2;
                break;
        }

    }
}
