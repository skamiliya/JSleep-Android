package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

public class LoginActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        TextView register = findViewById(R.id.RegisterNow);
        Button login = findViewById(R.id.buttonLogin);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLogin();
//                Intent move = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(move);

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(move);
            }

        });
    }
    protected Account requestLogin(){
        mApiService.login(
                username.getText().toString(), password.getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){

                    MainActivity.cookies = response.body();

                    Intent go = new Intent(LoginActivity.this,
                            MainActivity.class);


                    startActivity(go);
                    Toast.makeText(mContext, "Login Successfull", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Account> call, Throwable t){
                System.out.println(t.toString());

                Toast.makeText(mContext, "invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }
    protected Account requestAccount(){
        mApiService.getAccount(0).
                enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call,
                                   Response<Account> response) {
                if(response.isSuccessful()){
                    MainActivity.cookies = response.body();
                    Intent move = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(move);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t)
            {

                t.printStackTrace();
                Toast.makeText(mContext,
                        "no Account id=0",
                        Toast.LENGTH_SHORT).show();System.out.println("test");
            }
        });
        return null;
    }

}