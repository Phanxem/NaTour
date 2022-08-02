package com.unina.natour.views.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.*;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;


public class PersonalizzaAccountInfoOpzionaliActivity extends AppCompatActivity {

    private final static String TAG ="PersonalizzaAccountActivity";

    private ImpostaInfoOpzionaliProfiloController impostaInfoOpzionaliProfiloController;


    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_info_opzionali);

        impostaInfoOpzionaliProfiloController = new ImpostaInfoOpzionaliProfiloController(this);


        initDatePicker();
        pressButtonDate();
        pressButtonNext();
    }

    private void initDatePicker() {
        Button button_date = findViewById(R.id.PersonalizzaAccount_button_dataDiNascita);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String stringMonth = Month.of(month+1).getDisplayName(TextStyle.FULL, Locale.getDefault());
                stringMonth = stringMonth.substring(0, 1).toUpperCase() + stringMonth.substring(1);
                String date = day + " / " + stringMonth + " / " + year;

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                impostaInfoOpzionaliProfiloController.setDate(cal);

                button_date.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    public void pressButtonDate(){
        Button button_date = findViewById(R.id.PersonalizzaAccount_button_dataDiNascita);
        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    public void pressButtonNext(){
        Button button_next = findViewById(R.id.PersonalizzaAccount2_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText_luogoDiResidenza = findViewById(R.id.PersonalizzaAccount_editText_luogoDiResidenza);
                String luogoDiResidenza = String.valueOf(editText_luogoDiResidenza.getText());

                impostaInfoOpzionaliProfiloController.modificaInfoOpzionali(luogoDiResidenza);

            }
        });
    }



}