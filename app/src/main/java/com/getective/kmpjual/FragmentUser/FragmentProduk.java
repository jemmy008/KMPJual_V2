package com.getective.kmpjual.FragmentUser;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getective.kmpjual.Adapter.ProdukAdapter;
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
 * A simple {@link Fragment} subclass.
 */
public class FragmentProduk extends Fragment {

    private DatabaseReference mDatabaseProduk;
    private RecyclerView recyclerViewProduk;
    @BindView(R.id.button_cari) Button cari;
    @BindView(R.id.edittext_cari) EditText editTextCari;


    public FragmentProduk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_produk, container, false);
        ButterKnife.bind(this, rootView);



        mDatabaseProduk = FirebaseDatabase.getInstance().getReference().child("barang");

        recyclerViewProduk = (RecyclerView) rootView.findViewById(R.id.recyclerView_Produk);

        mDatabaseProduk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StaticList.setListProduk(getDataSnapshot(dataSnapshot));
                LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                linearLayout.setStackFromEnd(true);
                recyclerViewProduk.setHasFixedSize(true);
                recyclerViewProduk.setLayoutManager(linearLayout);
                recyclerViewProduk.setDrawingCacheEnabled(true);
                recyclerViewProduk.setItemViewCacheSize(10);
                recyclerViewProduk.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                ProdukAdapter produkAdapter = new ProdukAdapter(StaticList.getListProduk());
                recyclerViewProduk.setAdapter(produkAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });

        List<Produk> tempList = new ArrayList<>();

        cari.setOnClickListener(v -> {
            tempList.clear();
            for(Produk produk : StaticList.getListProduk()){
                int result = KMP(produk.getNama_barang().toLowerCase(), editTextCari.getText().toString().toLowerCase());
                if (result != -1) {
                    tempList.add(produk);
                }
            }
            LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewProduk.setHasFixedSize(true);
            recyclerViewProduk.setLayoutManager(linearLayout);
            recyclerViewProduk.setDrawingCacheEnabled(true);
            recyclerViewProduk.setItemViewCacheSize(10);
            recyclerViewProduk.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            ProdukAdapter produkAdapter = new ProdukAdapter(tempList);
            recyclerViewProduk.setAdapter(produkAdapter);
        });

        return rootView;
    }


    private List<Produk> getDataSnapshot(DataSnapshot dataSnapshot){
        List<Produk> lProduk = new ArrayList<>();
        for (DataSnapshot child: dataSnapshot.getChildren()) {
            lProduk.add(child.getValue(Produk.class));
        }
        return lProduk;
    }

    public static int KMP(String search, String target) {
        int[] failureTable = failureTable(target);

        int targetPointer = 0; // current char in target string
        int searchPointer = 0; // current char in search string

        while (searchPointer < search.length()) { // while there is more to search with, keep searching
            if (search.charAt(searchPointer) == target.charAt(targetPointer)) { // case 1
                // found current char in targetPointer in search string
                targetPointer++;
                if (targetPointer == target.length()) { // found all characters
                    int x = target.length() + 1;
                    return searchPointer - x; // return starting index of found target inside searched string
                }
                searchPointer++; // move forward if not found target string
            } else if (targetPointer > 0) { // case 2
                // use failureTable to use pointer pointed at nearest location of usable string prefix
                targetPointer = failureTable[targetPointer];
            } else { // case 3
                // targetPointer is pointing at state 0, so restart search with current searchPointer index
                searchPointer++;
            }
        }
        return -1;
    }

    /**
     * Returns an int[] that points to last valid string prefix, given target string
     */
    public static int[] failureTable(String target) {
        int[] table = new int[target.length() + 1];
        // state 0 and 1 are guarenteed be the prior
        table[0] = -1;
        table[1] = 0;

        // the pointers pointing at last failure and current satte
        int left = 0;
        int right = 2;

        while (right < table.length) { // RIGHT NEVER MOVES RIGHT UNTIL ASSIGNED A VALID POINTER
            if (target.charAt(right - 1) == target.charAt(left)) { // when both chars before left and right are equal, link both and move both forward
                left++;
                table[right] = left;
                right++;
            }  else if (left > 0) { // if left isn't at the very beginning, then send left backward
                // by following the already set pointer to where it is pointing to
                left = table[left];
            } else { // left has fallen all the way back to the beginning
                table[right] = left;
                right++;
            }
        }
        return table;
    }


}
