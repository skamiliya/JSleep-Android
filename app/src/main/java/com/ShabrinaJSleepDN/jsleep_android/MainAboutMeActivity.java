package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;

public class MainAboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_about_me);
    }
        Account sessionAccount = MainActivity.cookies;
        TextView nameAccount = findViewById(R.id.textViewNameIsi);
        TextView emailAccount = findViewById(R.id.textviewEmail);
        TextView balanceAccount = findViewById(R.id.textViewBalanceIsi);
        

    }