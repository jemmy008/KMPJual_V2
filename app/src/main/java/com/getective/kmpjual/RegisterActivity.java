package com.getective.kmpjual;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getective.kmpjual.BaseActivity;
import com.getective.kmpjual.LoginActivity;
import com.getective.kmpjual.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edittext_nama) EditText edittextNama;
    @BindView(R.id.edittext_email) EditText edittextEmail;
    @BindView(R.id.edittext_password) EditText edittextPassword;
    @BindView(R.id.edittext_alamat) EditText edittextAlamat;
    @BindView(R.id.edittext_telpon) EditText edittextTelpon;
    @BindView(R.id.button_daftar) Button buttonDaftar;
    @BindView(R.id.button_kembali) Button buttonKembali;
    @BindView(R.id.image_gallery) ImageView imageGallery;
    @BindView(R.id.radio_jk) RadioGroup radioJK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        radioJK.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            if(checkedId == R.id.radio_wanita){
                imageGallery.setImageDrawable(getResources().getDrawable(R.drawable.woman));
            } else {
                imageGallery.setImageDrawable(getResources().getDrawable(R.drawable.man));
            }
        });

        buttonDaftar.setOnClickListener(v -> {
            if(!getText(edittextEmail).equals("") && !getText(edittextNama).equals("") && !getText(edittextPassword).equals("")){
                ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Membuat akun");
                pDialog.show();
                mAuth.createUserWithEmailAndPassword(getText(edittextEmail),getText(edittextPassword)).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String user_uid = mAuth.getCurrentUser().getUid();

                        // get selected radio button from radioGroup
                        int selectedId = radioJK.getCheckedRadioButtonId();

                        if(selectedId == R.id.radio_wanita){
                            mDatabase.child(user_uid).child("jk").setValue("Wanita");
                        } else {
                            mDatabase.child(user_uid).child("jk").setValue("Pria");
                        }

                        mDatabase.child(user_uid).child("nama").setValue(getText(edittextNama));
                        mDatabase.child(user_uid).child("email").setValue(mAuth.getCurrentUser().getEmail());
                        mDatabase.child(user_uid).child("alamat").setValue(getText(edittextAlamat));
                        mDatabase.child(user_uid).child("telpon").setValue(getText(edittextTelpon));


                       // new SendMailTask().execute(mAuth.getCurrentUser().getEmail());

                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            pDialog.dismiss();
                            finish();
                        }, 2000);
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(this,"Email tidak valid", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if( edittextNama.getText().toString().trim().equals("")){
                edittextNama.setError( "Nama belum diisi!" );
            }
            if( edittextEmail.getText().toString().trim().equals("")){
                edittextEmail.setError( "Email belum diisi!" );
            }
            if( edittextPassword.getText().toString().trim().equals("")){
                edittextPassword.setError( "Password belum diisi!" );
            }
        });

        buttonKembali.setOnClickListener(v -> {
            openNewActivity(LoginActivity.class);
        });
    }

//    private static class SendMailTask extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... auth) {
//            try {
//                GMailSender sender = new GMailSender("aplikasipemilihansmartphoneadm@gmail.com", "apsadm123");
//                sender.sendMail("Daftar Akun Berhasil",
//                        Html.fromHtml("<p>Halo,</p>\n<p>Akun anda telah berhasil terdaftar</p>" +
//                                "\n<p>Silahkan login untuk dapat masuk ke dalam aplikasi</p>\n<p>Terima kasih,</p>\n" +
//                                "<p>Tim Aplikasi Pemilihan Smartphone</p>").toString(),
//                        "aplikasipemilihansmartphoneadm@gmail.com",
//                        auth[0]);
//            } catch (Exception e) {
//                Log.e("SendMail", e.getMessage(), e);
//            }
//            return null;
//        }
//    }
}
