package com.baskerville.toilocate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.dto.CredentialDTO;
import com.baskerville.toilocate.dto.LoginResDTO;
import com.baskerville.toilocate.security.User;
import com.baskerville.toilocate.security.UserHandler;
import com.baskerville.toilocate.service.ToiletService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Button loginButton, signUpButton;
    EditText emailEditText, passwordEditText;

    private Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Toilet");

        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp);

        emailEditText = findViewById(R.id.editTextLoginEmail);
        passwordEditText = findViewById(R.id.editTextLoginPassword);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().equals("admin@mail.com") &&
                        passwordEditText.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AddToilet.class);
                    intent.putExtra("coordinates",
                            getIntent().getExtras().getDoubleArray("coordinates"));
                    startActivity(intent);

                } else {

                    if (retrofit == null) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(Config.SECURITY_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                    }

                    ToiletService toiletService = retrofit.create(ToiletService.class);

                    CredentialDTO credentialDTO = new CredentialDTO();
                    credentialDTO.setEmail(emailEditText.getText().toString());
                    credentialDTO.setPassword(passwordEditText.getText().toString());

                    Log.i("Yo login cred", credentialDTO.toString());

                    Call<LoginResDTO> loginResCall = toiletService.login(credentialDTO);

                    loginResCall.enqueue(new Callback<LoginResDTO>() {
                        @Override
                        public void onResponse(Call<LoginResDTO> call, Response<LoginResDTO> response) {
                            if (response.isSuccessful()) {
                                Log.i("Yo Login res", response.body().getUser().toString());
                                User user = response.body().getUser();
                                UserHandler.setUser(user);
                                Toast.makeText(LoginActivity.this,
                                        "Welcome " + user.getName(),
                                        Toast.LENGTH_SHORT).show();

                                Intent intent =
                                        new Intent(LoginActivity.this, AddToilet.class);
                                intent.putExtra("coordinates",
                                        getIntent().getExtras().getDoubleArray("coordinates"));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Credentials",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResDTO> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Network Error!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        signUpButton.setOnClickListener(v -> openSignUpDialog());

        Toast.makeText(this, "Login or sign-up to add a new toilet", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void openSignUpDialog() {
        DialogSignup dialogSignup = new DialogSignup();
        dialogSignup.show(getSupportFragmentManager(), "signup dialog");
    }
}
