package com.getective.kmpjual.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getective.kmpjual.FragmentAdmin.EditProduk;
import com.getective.kmpjual.FragmentAdmin.TambahProduk;
import com.getective.kmpjual.Model.Produk;
import com.getective.kmpjual.R;
import com.getective.kmpjual.StaticList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yosefricaro on 18/11/2017.
 */

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewViewHolder> {

    private Context context;
    private List<Produk> produks;



    public ProdukAdapter(List<Produk> produks) {
        this.produks = new ArrayList<>();
        this.produks.addAll(produks);
    }

    @Override
    public ViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_product, parent, false);
        return new ViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewViewHolder holder, int position) {
        final Produk produk = produks.get(position);

        holder.setDeskripsi(produk.getDeskripsi_barang());
        holder.setHarga(produk.getHarga_barang());
        holder.setNama(produk.getNama_barang());
        holder.setStok(produk.getStok_barang());
        holder.setGambar(context, produk.getGambar_barang());
        String uid = produk.getUid();
        DatabaseReference mDatabaseProduk = FirebaseDatabase.getInstance().getReference().child("barang");

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseProduk.child(uid).removeValue();
            }
        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticList.setProduk(produk);

                Fragment fragment = new EditProduk();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(fragment.getClass().getName()).replace(R.id.content_frame, fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return produks.size();
    }

    public static class ViewViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private ImageButton mDeleteButton;
        private ImageButton mEditButton;

        public ViewViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mDeleteButton = mView.findViewById(R.id.produk_hapus);
            mEditButton = mView.findViewById(R.id.produk_edit);
        }
        public void setDeskripsi(String deskripsi){
            TextView mDeskripsi = mView.findViewById(R.id.deskripsi_barang);
            mDeskripsi.setText("Deskripsi : " + deskripsi);
        }
        public void setHarga(int harga){
            TextView mHarga = mView.findViewById(R.id.harga_barang);
            mHarga.setText("Harga : " + harga);
        }
        public void setNama(String nama){
            TextView mNama = mView.findViewById(R.id.nama_barang);
            mNama.setText("Nama : " + nama);
        }
        public void setStok(int stok){
            TextView mStok = mView.findViewById(R.id.stok_barang);
            mStok.setText("Stok : " + stok);
        }

        public void setGambar(Context ctx, String gambar){
            ImageView mGambar = (ImageView) mView.findViewById(R.id.gambar_barang);
            Picasso.with(ctx).load(gambar).into(mGambar);
        }
    }

}
