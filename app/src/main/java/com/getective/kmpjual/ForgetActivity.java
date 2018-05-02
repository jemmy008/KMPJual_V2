package com.getective.kmpjual;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getective.kmpjual.BaseActivity;
import com.getective.kmpjual.LoginActivity;
import com.getective.kmpjual.R;
import com.google.firebase.auth.FirebaseAuth;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetActivity extends BaseActivity {
    @BindView(R.id.edittext_email) EditText edittextEmail;
    @BindView(R.id.button_forgot) Button buttonForgot;
    @BindView(R.id.button_kembali) Button buttonKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        buttonForgot.setOnClickListener(view -> {
            if( edittextEmail.getText().toString().trim().equals("")){
                edittextEmail.setError( "Email belum diisi!" );
            } if (!getText(edittextEmail).equals("")) {
                String email = edittextEmail.getText().toString();
                ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Mengirim email");
                pDialog.show();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            pDialog.dismiss();
                            Toast.makeText(this, "Cek email anda untuk reset password!", Toast.LENGTH_SHORT).show();
                            finish();
                        }, 2000);

                    } else {
                        pDialog.dismiss();
                        Toast.makeText(this, "Gagal mengirim email reset password!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        buttonKembali.setOnClickListener(v -> {
            openNewActivity(LoginActivity.class);
        });
    }
}
