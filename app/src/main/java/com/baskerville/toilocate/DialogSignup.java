package com.baskerville.toilocate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.dto.CredentialDTO;
import com.baskerville.toilocate.dto.LoginResDTO;
import com.baskerville.toilocate.service.ToiletService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogSignup extends AppCompatDialogFragment {
    private EditText editTextUserName;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private TextView textViewPasswordMiss;
    private Button signUpButton;
    private boolean userRegistered = false;
    private Retrofit retrofit = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signup, null);

        editTextUserName = view.findViewById(R.id.editTextRegUserName);
        editTextEmailAddress = view.findViewById(R.id.editTextRegEmail);
        editTextPassword = view.findViewById(R.id.editTextRegPassword);
        editTextRePassword = view.findViewById(R.id.editTextRegRePassword);
        textViewPasswordMiss = view.findViewById(R.id.textViewRegPassMiss);
        signUpButton = view.findViewById(R.id.buttonSignUp);
        setUpRePassword();
        setupSignUpButton();

        builder.setView(view).setTitle("Sign Up");
        return builder.create();
    }

    private void setUpRePassword() {
        editTextRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextPassword.getText().toString().equals(editable.toString())) {
                    textViewPasswordMiss.setVisibility(View.INVISIBLE);
                } else {
                    textViewPasswordMiss.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupSignUpButton() {
        signUpButton.setOnClickListener(view -> {
            if (allFieldsFilled()) {
                if (isPasswordsSame()) {
                    if (retrofit == null) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(Config.SECURITY_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                    }
                    ToiletService toiletService = retrofit.create(ToiletService.class);

                    CredentialDTO credentialDTO = new CredentialDTO();
                    credentialDTO.setName(editTextUserName.getText().toString());
                    credentialDTO.setEmail(editTextEmailAddress.getText().toString());
                    credentialDTO.setPassword(editTextPassword.getText().toString());

                    Call<LoginResDTO> signUpResCall = toiletService.signUp(credentialDTO);

                    signUpResCall.enqueue(new Callback<LoginResDTO>() {
                        @Override
                        public void onResponse(Call<LoginResDTO> call,
                                               Response<LoginResDTO> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(view.getContext(),
                                        response.body().getMessage()
                                                + "Login to continue ",
                                        Toast.LENGTH_LONG).show();
                                getDialog().dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResDTO> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(view.getContext(), "Passwords dose not match",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(view.getContext(), "Fill all the fields",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean allFieldsFilled() {
        return (editTextUserName.getText().length() > 0) &&
                (editTextEmailAddress.getText().length() > 0) &&
                (editTextPassword.getText().length() > 0) &&
                (editTextRePassword.getText().length() > 0);
    }

    private boolean isPasswordsSame() {
        return editTextPassword.getText().toString().equals(editTextRePassword.getText().toString());
    }


}
