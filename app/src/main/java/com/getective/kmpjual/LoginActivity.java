package com.getective.kmpjual;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.edittext_email) EditText edittextEmail;
    @BindView(R.id.edittext_password) EditText edittextPassword;
    @BindView(R.id.button_masuk) Button buttonMasuk;
    @BindView(R.id.button_register) Button buttonRegister;
    @BindView(R.id.button_forgot) Button buttonForgot;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        buttonMasuk.setOnClickListener(view -> {
            if( edittextEmail.getText().toString().trim().equals("")){
                edittextEmail.setError( "Email belum diisi!" );
            } if ( edittextPassword.getText().toString().trim().equals("")){
                edittextPassword.setError( "Password belum diisi!" );
            } if (!getText(edittextEmail).equals("") && !getText(edittextPassword).equals("")) {
                String email = edittextEmail.getText().toString();
                String password = edittextPassword.getText().toString();
                ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Memeriksa akun");
                pDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        pDialog.dismiss();
                        Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        buttonForgot.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgetActivity.class));
        });

        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Silahkan tekan kembali untuk keluar", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            super.onBackPressed();
        }
    }
}
