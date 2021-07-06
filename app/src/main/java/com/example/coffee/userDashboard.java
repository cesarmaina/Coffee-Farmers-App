package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coffee.fragments.Products;
import com.example.coffee.fragments.news;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class userDashboard extends AppCompatActivity{
    ImageView image;
    TextView user1,email,phone,coffeeid;
    DrawerLayout drawerLayout;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference reference;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmerdashboard);

        toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        drawerLayout =findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.editprof:
                        startActivity(new Intent(getApplicationContext(),myaccount.class));
                        break;

                }
                return true;
            }
        });

        View view=navigationView.getHeaderView(0);
        image=view.findViewById(R.id.profpic);
        user1=view.findViewById(R.id.username);
        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phone);
        coffeeid=view.findViewById(R.id.coffeeid);

        storageReference= FirebaseStorage.getInstance().getReference("profile");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerClass users=snapshot.getValue(registerClass.class);
                user1.setText(users.getName());
                email.setText(users.getEmail());
                phone.setText(users.getPhone());
                coffeeid.setText(users.getCid());

                if (users.getImage().equals("Default")){
                    image.setImageResource(R.drawable.account1);
                }
                else {
                    Glide.with(getApplicationContext()).load(users.getImage()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.dashboard:
                        return true;
                    case R.id.records:
                        Intent record=new Intent(getApplicationContext(), userrecords.class);
                        overridePendingTransition(0,0);
                        startActivity(record);
                        finish();
                        return true;
                    case R.id.loan:
                        Intent loan=new Intent(getApplicationContext(), loan.class);
                        overridePendingTransition(0,0);
                        startActivity(loan);
                        finish();
                        return true;
                    case R.id.shop:
                        Intent shop=new Intent(getApplicationContext(), Shop.class);
                        overridePendingTransition(0,0);
                        startActivity(shop);
                        finish();
                        return true;
                    case R.id.messageactivity:
                        Intent message=new Intent(getApplicationContext(), userChatDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(message);
                        finish();
                        return true;

                }
                return false;
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TabLayout tabLayout=findViewById(R.id.tablayout1);
        ViewPager viewPager=findViewById(R.id.viewpager1);
        userDashboard.ViewPagerAdapter viewPagerAdapter=new userDashboard.ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new news(),"News");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(userDashboard.this,userlogin.class));
                finish();
                return true;
        }
        return false;
    }
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String>titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return  titles.get(position);
        }
    }
}
