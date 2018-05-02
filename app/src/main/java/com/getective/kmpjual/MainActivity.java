package com.getective.kmpjual;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.getective.kmpjual.FragmentAdmin.TambahProduk;
import com.getective.kmpjual.FragmentUser.FragmentProduk;
import com.getective.kmpjual.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosef.aplikasipemilihansmartphone.view.fragment.EditProfilFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        try {
            String user_id = mAuth.getCurrentUser().getUid();
            if(mAuth.getCurrentUser() != null){
                if(user_id.equals("qro44ZX1I3Yb22RoFVDTr3CCeUs2")){
                    navigationView.getMenu().setGroupVisible(R.id.nav_user, false);
                }else {
                    navigationView.getMenu().setGroupVisible(R.id.nav_admin, false);
                }
            } else {
                openNewActivity(LoginActivity.class);
            }

            DatabaseReference mUser = FirebaseDatabase.getInstance().getReference().child("users");

            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    StaticList.setUser(dataSnapshot.child(user_id).getValue(User.class));

                    View hView =  navigationView.getHeaderView(0);
                    TextView emailUser = hView.findViewById(R.id.user_email);
                    TextView namaUser = hView.findViewById(R.id.user_name);
                    ImageView imageView = hView.findViewById(R.id.imageAvatar);

                    emailUser.setText(StaticList.getUser().getEmail());
                    namaUser.setText(StaticList.getUser().getNama());
                    if(StaticList.getUser().getJk().equals("Pria")) {
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.man));
                    } else{
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.woman));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("The read failed: " ,databaseError.getMessage());
                }
            });
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_produk) {
            fragment = new FragmentProduk();

        } else if (id == R.id.nav_review) {

        } else if (id == R.id.nav_keranjang) {

        } else if (id == R.id.nav_cara) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_profil) {
            fragment = new EditProfilFragment();

        }  else if (id == R.id.nav_logout) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            openNewActivity(LoginActivity.class);

        } else if (id == R.id.nav_signout_admin) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            openNewActivity(LoginActivity.class);
        } else if (id == R.id.nav_add) {
            fragment = new TambahProduk();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
