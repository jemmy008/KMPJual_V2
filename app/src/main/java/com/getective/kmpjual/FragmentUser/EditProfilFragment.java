package com.yosef.aplikasipemilihansmartphone.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getective.kmpjual.R;
import com.getective.kmpjual.StaticList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfilFragment extends Fragment {

    @BindView(R.id.edittext_nama) EditText edittextNama;
    @BindView(R.id.edittext_alamat) EditText edittextAlamat;
    @BindView(R.id.edittext_telpon) EditText edittextTelpon;
    @BindView(R.id.button_edit) Button buttonEdit;
    @BindView(R.id.image_gallery) ImageView imageGallery;
    @BindView(R.id.radio_jk) RadioGroup radioJK;
    @BindView(R.id.radio_pria) RadioButton radioPria;
    @BindView(R.id.radio_wanita) RadioButton radioWanita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profil, container, false);
        ButterKnife.bind(this, rootView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        edittextNama.setText(StaticList.getUser().getNama());
        edittextAlamat.setText(StaticList.getUser().getAlamat());
        if(StaticList.getUser().getJk().equals("Wanita")) {
            imageGallery.setImageDrawable(getResources().getDrawable(R.drawable.woman));
            radioWanita.setChecked(true);
        }

        radioJK.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            if(checkedId == R.id.radio_wanita){
                imageGallery.setImageDrawable(getResources().getDrawable(R.drawable.woman));
            } else {
                imageGallery.setImageDrawable(getResources().getDrawable(R.drawable.man));
            }
        });

        buttonEdit.setOnClickListener(v -> {
            if(!getText(edittextNama).equals("")){
                ProgressDialog pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Mengedit akun");
                pDialog.show();

                String user_uid = mAuth.getCurrentUser().getUid();

                // get selected radio button from radioGroup
                int selectedId = radioJK.getCheckedRadioButtonId();

                if(selectedId == R.id.radio_wanita){
                    mDatabase.child(user_uid).child("jk").setValue("Wanita");
                } else {
                    mDatabase.child(user_uid).child("jk").setValue("Pria");
                }

                mDatabase.child(user_uid).child("nama").setValue(getText(edittextNama));
                mDatabase.child(user_uid).child("alamat").setValue(getText(edittextAlamat));
                mDatabase.child(user_uid).child("telpon").setValue(getText(edittextTelpon));
                new Handler().postDelayed(() -> {
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Ubah profil berhasil!", Toast.LENGTH_SHORT).show();
                }, 2000);
            }  else {
                edittextNama.setError( "Nama belum diisi!" );
            }
        });

        return rootView;
    }

    protected String getText(EditText editText){
        return editText.getText().toString().trim();
    }
}
