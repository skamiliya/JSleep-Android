package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password, email;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApiService = UtilsApi.getApiService();

        mContext = this;

        username = findViewById(R.id.editTextTextPersonName);

        password = findViewById(R.id.editTextTextPassword);

        email = findViewById(R.id.editTextTextEmailAddress);

        Button register = findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override

            public void onClick(View view)
            {
                requestRegister();
            }
        });


    }

    protected Account requestRegister()
    {
        mApiService.register(username.getText().toString(),email.getText().toString(),
                password.getText().toString()).enqueue
                (new Callback<Account>()
                {
            @Override
            public void onResponse(Call<Account> call,
                                   Response<Account> response)
            {
                if(response.isSuccessful())
                {

                    MainActivity.cookies = response.body();

                    Intent go = new Intent(RegisterActivity.this, MainActivity.class);

                    startActivity(go);

                    Toast.makeText(mContext,
                            "Register Succeed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t)
            {
                System.out.println(t.toString());

                Toast.makeText(mContext, "Email and Username already use, Please choose another", Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }
}