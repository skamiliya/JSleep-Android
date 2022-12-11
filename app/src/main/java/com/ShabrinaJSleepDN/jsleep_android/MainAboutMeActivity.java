package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.model.Renter;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("MissingInflatedId")
public class MainAboutMeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    TextView about_balance, nominal;
    EditText nameInput, addressInput, phoneNumberInput;
    CardView registerCardView,dataCardView;
    Button topUpInput;
//    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_about_me);

        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        Account sessionAccount = MainActivity.cookies;
        TextView nameAccount = findViewById(R.id.about_name);
        TextView emailAccount = findViewById(R.id.about_email);
        Button aboutRegisterRenter = findViewById(R.id.aboutme_registerRenter);

        //Second Condition
        nameInput = findViewById(R.id.aboutme_name2);
        addressInput = findViewById(R.id.aboutme_address);
        phoneNumberInput = findViewById(R.id.aboutme_phoneNumber);
        Button aboutRegister = findViewById(R.id.aboutme_register);
        Button cancel = findViewById(R.id.aboutme_cancel);
        Button topUpInput = findViewById(R.id.topUpInput);
        nominal = findViewById(R.id.nominal);
        about_balance = findViewById(R.id.about_balance);


        //Third Condition
        TextView nameRenter = findViewById(R.id.aboutme_textviewStore);
        TextView addressRenter = findViewById(R.id.aboutme_textviewPlace);
        TextView phoneNumberRenter = findViewById(R.id.aboutme_textviewNumber);
        dataCardView = findViewById(R.id.dataCardView);
        registerCardView = findViewById(R.id.registerCardView);

        nameAccount.setText(sessionAccount.name);
        emailAccount.setText(sessionAccount.email);
        about_balance.setText(Double.toString(sessionAccount.balance));

        mApiService = UtilsApi.getApiService();
        mContext = this;

        topUpInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                topUpAccount();
            }
        });
        registerCardView.setVisibility(View.INVISIBLE);
        dataCardView.setVisibility(View.INVISIBLE);

        if (MainActivity.cookies.renter == null) {
            dataCardView.setVisibility(View.INVISIBLE);
            registerCardView.setVisibility(View.INVISIBLE);
            aboutRegisterRenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aboutRegisterRenter.setVisibility(View.INVISIBLE); //Button menghilang
                    registerCardView.setVisibility(View.VISIBLE);
                    dataCardView.setVisibility(View.INVISIBLE);
                    aboutRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            System.out.println(nameInput.getText().toString());
//                            System.out.println(addressInput.getText().toString());
//                            System.out.println(phoneNumberInput.getText().toString());
                            requestRenter();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            aboutRegisterRenter.setVisibility(View.VISIBLE); //Button muncul
                            registerCardView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
        }

        if(MainActivity.cookies.renter != null){
            aboutRegisterRenter.setVisibility(View.INVISIBLE);
            registerCardView.setVisibility(View.INVISIBLE);
            dataCardView.setVisibility(View.VISIBLE);

            nameRenter.setText(MainActivity.cookies.renter.username);
            addressRenter.setText(MainActivity.cookies.renter.address);
            phoneNumberRenter.setText(String.valueOf(MainActivity.cookies.renter.phoneNumber));
        }
    }

    protected boolean topUpAccount() {
        mApiService.topUpRequest(
                MainActivity.cookies.id,
                Double.parseDouble(nominal.getText().toString())
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
//                    topUp = response.body();
                    Toast.makeText(mContext, "Top Up Successful!", Toast.LENGTH_SHORT).show();
                    System.out.println(nominal.getText().toString());
                    about_balance.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(MainActivity.cookies.balance + Double.parseDouble(nominal.getText().toString())));
//                    sessionAccount.balance+= Double.parseDouble(nominal.getText().toString());
                    MainActivity.cookies.balance += Double.parseDouble(nominal.getText().toString());
                    nominal.setText("");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Top Up Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    protected Renter requestRenter(){
        mApiService.registerRenterRequest(
                MainActivity.cookies.id,
                nameInput.getText().toString(),
                addressInput.getText().toString(),
                phoneNumberInput.getText().toString()).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                if(response.isSuccessful()){
                    MainActivity.cookies.renter = response.body();
                    Intent move = new Intent(MainAboutMeActivity.this, MainActivity.class);
                    startActivity(move);
                    Toast.makeText(mContext, "Register Renter Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Register Renter Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }



}


