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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAboutMeActivity extends AppCompatActivity {
    Context mContext;
    BaseApiService mApiService;
    TextView name, email, balance;

    Button RegisterRenterButton;
    CardView RegisterButtonCard;Button RegisterRenterConfirmation, RegisterRenterCancel;

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

        name.setText(MainActivity.cookies.name);
        email.setText(MainActivity.cookies.email);
        balance.setText(String.valueOf(MainActivity.cookies.balance));

        RegisterRenterButton = findViewById(R.id.registerRenterButton);
        //Second Condition
        EditText RegisterRenterName = findViewById(R.id.NameRegisterRenter);
        EditText RegisterRenterAddress = findViewById(R.id.AddressRegisterRenter);
        EditText PhoneNumberAccount = findViewById(R.id.PhoneNumberRegisterRenter);
        Button RegistAccept = findViewById(R.id.ButtonRegister);
        Button RegistCancel = findViewById(R.id.ButtonCancel);

        CardView CardViewRegister = findViewById(R.id.RegisterDetail);
        CardView CardViewAccount = findViewById(R.id.RegisterRenterCard);

        //Third Condition
        TextView NameInput = findViewById(R.id.NameIsi);
        TextView AddressInput = findViewById(R.id.AlamatIsi);
        TextView PhoneInput = findViewById(R.id.PhoneNumberIsi);


//        RegisterButtonCard = findViewById(R.id.RegisterRenterCard);
//        RegisterRenterConfirmation = findViewById(R.id.ButtonRegister);
//        RegisterRenterCancel = findViewById(R.id.ButtonCancel);
//        RenterRegistrationCard = findViewById(R.id.RegisterRenterCard);
//        RenterName = findViewById(R.id.NameRegisterRenter);
//        RenterAddress = findViewById(R.id.AddressRegisterRenter);
//        RenterPhoneNumber = findViewById(R.id.PhoneNumberRegisterRenter);
//        RegisterDetailCard = findViewById(R.id.RegisterRenterDetail);
//        RenterNameFill = findViewById(R.id.NameRegisterRenter);
//        AddressFill = findViewById(R.id.AddressRegisterRenter);
//        PhoneNumberFill = findViewById(R.id.PhoneNumberRegisterRenter);

        CardViewRegister.setVisibility(CardView.INVISIBLE);
        CardViewAccount.setVisibility(CardView.INVISIBLE);

        if (MainActivity.cookies.renter == null)
        {
//            CardViewRegister.setVisibility(CardView.INVISIBLE);
//            CardViewAccount.setVisibility(CardView.INVISIBLE);

            RegisterRenterButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    RegisterRenterButton.setVisibility(Button.INVISIBLE);
                    CardViewRegister.setVisibility(CardView.VISIBLE);
                    CardViewAccount.setVisibility(CardView.INVISIBLE);

                    RegistAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            System.out.println("hahaha");

                            Renter renter = requestRenter(MainActivity.cookies.id, RegisterRenterName.getText().toString(),
                                    RegisterRenterAddress.getText().toString(), PhoneNumberAccount.getText().toString());

//                          Intent move = new Intent(MainAboutMeActivity.this, MainAboutMeActivity.class);
//                            startActivity(move);
                        }

                    });
                    RegistCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            CardViewRegister.setVisibility(CardView.INVISIBLE);
                            CardViewAccount.setVisibility(CardView.INVISIBLE);
                        }
                    });
                }
            });

        } else {
            RegisterRenterButton.setVisibility(Button.INVISIBLE);
            CardViewRegister.setVisibility(CardView.INVISIBLE);
            CardViewAccount.setVisibility(CardView.VISIBLE);

            NameInput.setText(MainActivity.cookies.renter.username);
            AddressInput.setText(MainActivity.cookies.renter.address);
            PhoneInput.setText(MainActivity.cookies.renter.phoneNumber);
        }
    }

    protected Renter requestRenter(int id, String username, String address, String phone) {
        System.out.println("hehehhehe");
        mApiService.registerRenter(id, username, address, phone).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {

                if (response.isSuccessful()) {
                    Renter renter;
                    renter = response.body();
                    MainActivity.cookies.renter = renter;
                    System.out.println("Renter Registered Success");
                    Intent move = new Intent(MainAboutMeActivity.this, MainAboutMeActivity.class);
                    startActivity(move);
                    Toast.makeText(mContext, "Registered Renter", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Failed to Register!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}