package com.getective.kmpjual.FragmentAdmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ContentFrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.getective.kmpjual.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TambahProduk extends Fragment {

    private EditText mDeskripsi;
    private EditText mHarga;
    private EditText mNama;
    private EditText mStok;
    private ImageButton mImage;
    private Button mTambah;

    Uri file;
    static final int REQUEST_IMAGE_GET = 1;

    public TambahProduk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tambah_produk, container, false);

        mDeskripsi = rootView.findViewById(R.id.tambahProduk_deskripsi);
        mHarga = rootView.findViewById(R.id.tambahProduk_harga);
        mNama= rootView.findViewById(R.id.tambahProduk_nama);
        mStok= rootView.findViewById(R.id.tambahProduk_stok);
        mTambah = rootView.findViewById(R.id.tambahProduk_tambah);
        mImage = rootView.findViewById(R.id.tambahProduk_gambar);

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

        mTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahProduk();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private void tambahProduk() {
        DatabaseReference mDatabaseBarang = FirebaseDatabase.getInstance().getReference().child("barang");

        int harga = 0;
        int stok = 0;
        String deskripsi = mDeskripsi.getText().toString();
        harga = Integer.parseInt(mHarga.getText().toString());
        String nama = mNama.getText().toString();
        stok = Integer.parseInt(mStok.getText().toString());

        if(!deskripsi.equals("") && harga != 0 && !nama.equals("") && stok != 0 ){

            ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Memasukkan Produk");
            pDialog.show();
            String uid = mDatabaseBarang.push().getKey();
            mDatabaseBarang.child(uid).child("deskripsi_barang").setValue(deskripsi);
            mDatabaseBarang.child(uid).child("harga_barang").setValue(harga);
            mDatabaseBarang.child(uid).child("nama_barang").setValue(nama);
            mDatabaseBarang.child(uid).child("stok_barang").setValue(stok);
            mDatabaseBarang.child(uid).child("uid").setValue(uid);


            // new SendMailTask().execute(mAuth.getCurrentUser().getEmail());
            if(file != null) {
                StorageReference mStorageRef = FirebaseStorage.getInstance()
                        .getReference().child("produk/" + uid);
                mStorageRef.putFile(file)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests") String url = taskSnapshot.getDownloadUrl().toString();
                            mDatabaseBarang.child(uid).child("gambar_barang").setValue(url);
                        })
                        .addOnFailureListener(exception -> {
                            // Handle unsuccessful uploads
                        });
            } else{
                mDatabaseBarang.child(uid).child("gambar_barang").setValue("https://firebasestorage.googleapis.com/v0/b/kmpjual.appspot.com/o/produk%2Fdownload.png?alt=media&token=21b58cf3-c565-4d50-8395-adee62a33367");
            }


            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                pDialog.dismiss();
            }, 2000);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            TambahProduk llf = new TambahProduk();
            ft.replace(R.id.content_frame, llf);
            ft.commit();
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
