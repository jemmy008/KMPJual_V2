package com.getective.kmpjual.FragmentAdmin;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.getective.kmpjual.R;
import com.getective.kmpjual.StaticList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProduk extends Fragment {

    private EditText mDeskripsi;
    private EditText mHarga;
    private EditText mNama;
    private EditText mStok;
    private ImageButton mImage;
    @BindView(R.id.editProduk_edit) Button mEdit;

    Uri file;
    static final int REQUEST_IMAGE_GET = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment)
        View rootView = inflater.inflate(R.layout.fragment_edit_produk, container, false);
        ButterKnife.bind(this, rootView);

        mDeskripsi = rootView.findViewById(R.id.editProduk_deskripsi);
        mHarga = rootView.findViewById(R.id.editProduk_harga);
        mNama = rootView.findViewById(R.id.editProduk_nama);
        mStok = rootView.findViewById(R.id.editProduk_stok);
        mImage = rootView.findViewById(R.id.editProduk_gambar);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        mEdit.setOnClickListener(v -> editProduk());
        mDeskripsi.setText(StaticList.getProduk().getDeskripsi_barang());
        mHarga.setText(StaticList.getProduk().getHarga_barang() + "");
        mNama.setText(StaticList.getProduk().getNama_barang());
        mStok.setText(StaticList.getProduk().getStok_barang() + "");
        Picasso.with(getContext()).load(StaticList.getProduk().getGambar_barang()).into(mImage);

        return rootView;
    }

    private void editProduk() {
        DatabaseReference mDatabaseBarang = FirebaseDatabase.getInstance().getReference().child("barang").child(StaticList.getProduk().getUid());

        int harga = 0;
        int stok = 0;
        String deskripsi = mDeskripsi.getText().toString();
        harga = Integer.parseInt(mHarga.getText().toString());
        String nama = mNama.getText().toString();
        stok = Integer.parseInt(mStok.getText().toString());

        mDatabaseBarang.child("deskripsi_barang").setValue(deskripsi);
        mDatabaseBarang.child("harga_barang").setValue(harga);
        mDatabaseBarang.child("nama_barang").setValue(nama);
        mDatabaseBarang.child("stok_barang").setValue(stok);
        mDatabaseBarang.child("uid").setValue(StaticList.getProduk().getUid());

        // new SendMailTask().execute(mAuth.getCurrentUser().getEmail());
        if(file != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance()
                    .getReference().child("produk/" + StaticList.getProduk().getUid());
            mStorageRef.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests") String url = taskSnapshot.getDownloadUrl().toString();
                        mDatabaseBarang.child("gambar_barang").setValue(url);
                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                    });
        } else{
            mDatabaseBarang.child("gambar_barang").setValue("https://firebasestorage.googleapis.com/v0/b/kmpjual.appspot.com/o/produk%2Fdownload.png?alt=media&token=21b58cf3-c565-4d50-8395-adee62a33367");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            file = data.getData();
            mImage.setImageURI(file);
        }
    }
}
