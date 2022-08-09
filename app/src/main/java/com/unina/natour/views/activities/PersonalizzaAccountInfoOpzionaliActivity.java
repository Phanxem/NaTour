package com.unina.natour.views.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unina.natour.R;
import com.unina.natour.controllers.*;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.views.dialogs.SelectCityDialog;
import com.unina.natour.views.dialogs.SelectCountryDialog;
import com.unina.natour.views.observers.Observer;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;


public class PersonalizzaAccountInfoOpzionaliActivity
        extends AppCompatActivity
        implements Observer, SelectCountryDialog.OnCountryListener, SelectCityDialog.OnCityListener
{

    private final static String TAG ="PersonalizzaAccountActivity";

    private ImpostaInfoOpzionaliProfiloController impostaInfoOpzionaliProfiloController;

    private ImpostaInfoOpzionaliProfiloModel impostaInfoOpzionaliProfiloModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_info_opzionali);

        impostaInfoOpzionaliProfiloController = new ImpostaInfoOpzionaliProfiloController(this);

        impostaInfoOpzionaliProfiloModel = impostaInfoOpzionaliProfiloController.getImpostaInfoOpzionaliProfiloModel();
        impostaInfoOpzionaliProfiloModel.registerObserver(this);

        DatePickerDialog datePickerDialog = initDatePicker();
        pressButtonDate(datePickerDialog);


        pressCountryField();
        pressCityField();


        pressButtonNext();
    }

    private DatePickerDialog initDatePicker() {
        TextView textView_date = findViewById(R.id.PersonalizzaAccount_textView_dataDiNascita);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);

                impostaInfoOpzionaliProfiloController.setDateOfBirth(cal);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
        return datePickerDialog;
    }

    public void pressButtonDate(DatePickerDialog datePickerDialog){
        TextView textView_date = findViewById(R.id.PersonalizzaAccount_textView_data);
        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });
    }

    public void pressCountryField(){
        TextView textView_country = findViewById(R.id.PersonalizzaAccount_textView_nazione);
        textView_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCountryDialog selectCountryDialog = new SelectCountryDialog();
                selectCountryDialog.show(getSupportFragmentManager(),TAG);
            }
        });
    }

    public void pressCityField(){


        TextView textView_city = findViewById(R.id.PersonalizzaAccount_textView_città);
        textView_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = impostaInfoOpzionaliProfiloController.getCountry();

                SelectCityDialog selectCityDialog = SelectCityDialog.newInstance(country);
                selectCityDialog.show(getSupportFragmentManager(),TAG);
            }
        });
    }

    public void pressButtonNext(){
        Button button_next = findViewById(R.id.PersonalizzaAccount2_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText_luogoDiResidenza = findViewById(R.id.PersonalizzaAccount_editText_indirizzo);
                String luogoDiResidenza = String.valueOf(editText_luogoDiResidenza.getText());

                impostaInfoOpzionaliProfiloController.modificaInfoOpzionali(luogoDiResidenza);

            }
        });
    }





    @Override
    public void update() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //DATE OF BIRTH
                Calendar date = impostaInfoOpzionaliProfiloModel.getDateOfBirth();
                TextView textView_date = findViewById(R.id.PersonalizzaAccount_textView_data);
                if(date == null){
                    textView_date.setText(null);
                }
                else{
                    int year = date.get(Calendar.YEAR);
                    int month = date.get(Calendar.MONTH);
                    int day = date.get(Calendar.DAY_OF_MONTH);

                    String stringMonth = Month.of(date.get(Calendar.MONTH)).getDisplayName(TextStyle.FULL, Locale.getDefault());
                    stringMonth = stringMonth.substring(0, 1).toUpperCase() + stringMonth.substring(1);
                    String displayDate = day + " / " + stringMonth + " / " + year;

                    textView_date.setText(displayDate);
                }



                //RESIDENCE
                TextView textView_country = findViewById(R.id.PersonalizzaAccount_textView_nazione);
                TextView textView_city = findViewById(R.id.PersonalizzaAccount_textView_città);
                ConstraintLayout constraintLayout_city = findViewById(R.id.PersonalizzaAccount_constraintLayout_cittàDiResidenza);
                EditText editText_address = findViewById(R.id.PersonalizzaAccount_editText_indirizzo);
                ConstraintLayout constraintLayout_address = findViewById(R.id.PersonalizzaAccount_constraintLayout_indirizzoDiResidenza);

                String country = impostaInfoOpzionaliProfiloModel.getCountry();
                if(country == null || country.isEmpty()){
                    textView_country.setText(null);

                    //impostaInfoOpzionaliProfiloController.setCity(null);
                    textView_city.setText(null);
                    constraintLayout_city.setVisibility(View.GONE);

                    editText_address.setText(null);
                    constraintLayout_address.setVisibility(View.GONE);
                    return;
                }

                String current_country = (String) textView_country.getText();
                if(current_country != null && !current_country.isEmpty()){
                    if(!country.equals(current_country)){

                        textView_country.setText(country);

                        //impostaInfoOpzionaliProfiloController.setCity(null);
                        textView_city.setText(null);
                        constraintLayout_city.setVisibility(View.VISIBLE);


                        editText_address.setText(null);
                        constraintLayout_address.setVisibility(View.GONE);
                        return;
                    }
                }

                textView_country.setText(country);
                textView_city.setText(null);
                constraintLayout_city.setVisibility(View.VISIBLE);


                String city = impostaInfoOpzionaliProfiloModel.getCity();
                if(city == null || city.isEmpty()){
                    textView_city.setText(null);

                    editText_address.setText(null);
                    constraintLayout_address.setVisibility(View.GONE);
                    return;
                }

                textView_city.setText(city);
                editText_address.setText(null);
                constraintLayout_address.setVisibility(View.VISIBLE);

            }
        });
    }




    @Override
    public void getCountry(String country) {
        Log.i(TAG, country);

        //Controller update model
        impostaInfoOpzionaliProfiloController.setCountry(country);
    }

    @Override
    public void getCity(String city) {
        Log.i(TAG, city);

        //Controller update model
        impostaInfoOpzionaliProfiloController.setCity(city);
    }
}