package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.model.Renter;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAboutMeActivity extends AppCompatActivity {
    Context mContext;BaseApiService mApiService;TextView name, email, balance;

    Button RegisterRenterButton;CardView RegisterButtonCard;Button RegisterRenterConfirmation, RegisterRenterCancel;

    CardView RenterRegistrationCard;TextView RenterName, RenterAddress, RenterPhoneNumber;

    CardView RegisterDetailCard;TextView RenterNameFill, AddressFill, PhoneNumberFill;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_main_about_me);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        name = findViewById(R.id.textViewNameIsi);
        email = findViewById(R.id.textViewEmailsi);
        balance = findViewById(R.id.textViewBalanceIsi);

        if (balance == null)
        {
            balance.setText("0");
        }

        name.setText(MainActivity.loggedAccount.name);
        email.setText(MainActivity.loggedAccount.email);
        balance.setText(String.valueOf(MainActivity.loggedAccount.balance));


        RegisterRenterButton = findViewById(R.id.registerRenterButton);RegisterButtonCard = findViewById(R.id.RegisterRenterCard);
        RegisterRenterConfirmation = findViewById(R.id.ButtonRegister);
        RegisterRenterCancel = findViewById(R.id.ButtonCancel);RenterRegistrationCard = findViewById(R.id.RegisterRenterCard);
        RenterName = findViewById(R.id.NameRegisterRenter);RenterAddress = findViewById(R.id.AddressRegisterRenter);RenterPhoneNumber = findViewById(R.id.PhoneNumberRegisterRenter);

        RegisterDetailCard = findViewById(R.id.RegisterRenterDetail);
        RenterNameFill = findViewById(R.id.NameRegisterRenter);AddressFill = findViewById(R.id.AddressRegisterRenter);PhoneNumberFill = findViewById(R.id.PhoneNumberRegisterRenter);

        if (MainActivity.loggedAccount.renter == null) {
            RegisterButtonCard.setVisibility(CardView.VISIBLE);
            RegisterDetailCard.setVisibility(CardView.GONE);
            RenterRegistrationCard.setVisibility(CardView.GONE);
            RegisterRenterButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    RegisterButtonCard.setVisibility(CardView.GONE);RenterRegistrationCard.setVisibility(CardView.VISIBLE);RegisterDetailCard.setVisibility(CardView.GONE);
                    RegisterRenterConfirmation.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Renter renter = requestRenter(MainActivity.loggedAccount.id, RenterName.getText().toString(), RenterAddress.getText().toString(), RenterPhoneNumber.getText().toString());Intent move = new Intent(MainAboutMeActivity.this, MainAboutMeActivity.class);
                            startActivity(move);
                        }

                    });
                    RegisterRenterCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            RenterRegistrationCard.setVisibility(CardView.GONE);
                            RegisterDetailCard.setVisibility(CardView.GONE);
                            RegisterButtonCard.setVisibility(CardView.VISIBLE);
                        }
                    });
                }});

        }
        else
        {
            RegisterButtonCard.setVisibility(CardView.GONE);
            RegisterDetailCard.setVisibility(CardView.VISIBLE);
            RenterRegistrationCard.setVisibility(CardView.GONE);

            RenterNameFill.setText(MainActivity.loggedAccount.renter.username);
            PhoneNumberFill.setText(MainActivity.loggedAccount.renter.address);
            AddressFill.setText(MainActivity.loggedAccount.renter.phoneNumber);
        }
    }

    protected Renter requestRenter(int id, String username, String address, String phone)
    {
        mApiService.registerRenter(id, username,
                address,
                phone).
                enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call,
                                   Response<Renter> response) {
                if (response.isSuccessful())
                {
                    Renter renter;renter = response.body();
                    MainActivity.loggedAccount.renter = renter;System.out.println("Renter Registered");
                    Toast.makeText(mContext, "Registered Renter", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t)

            {
                Toast.makeText(mContext, "Failed to Register Renter", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}