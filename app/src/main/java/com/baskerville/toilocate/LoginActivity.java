package com.baskerville.toilocate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Button loginButton, b2, signUpButton;
    EditText emailEditText, passwordEditText;
    TextView tx1;
    int counter = 3;
    private Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.buttonLogin);
        signUpButton = (Button) findViewById(R.id.buttonSignUp);

        emailEditText = (EditText) findViewById(R.id.editTextLoginEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);

        b2 = (Button) findViewById(R.id.button2);
        tx1 = (TextView) findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);


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
                                User user = response.body().getUser();
                                UserHandler.setUser(user);
                                Toast.makeText(LoginActivity.this,
                                        "Welcome "+user.getName(),
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

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        loginButton.setEnabled(false);
                    }
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpDialog();
            }
        });
    }

    private void openSignUpDialog() {
        DialogSignup dialogSignup = new DialogSignup();
        dialogSignup.show(getSupportFragmentManager(), "signup dialog");
    }
}
