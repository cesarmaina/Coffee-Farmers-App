package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.coffee.fragments.Products;
import com.example.coffee.fragments.Sell;
import com.example.coffee.fragments.viewsales;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Adminsell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsell);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Adminshop);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {

                    case R.id.Adminshop:
                        return true;
                    case R.id.Adminrecords:
                        Intent record=new Intent(getApplicationContext(), AdminSaveDash.class);
                        overridePendingTransition(0,0);
                        startActivity(record);
                        finish();
                        return true;
                    case R.id.Admindashboard:
                        Intent dashboard=new Intent(getApplicationContext(), AdminDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(dashboard);
                        finish();
                        return true;
                    case R.id.AdminaLoans:
                        Intent loans=new Intent(getApplicationContext(), adminLoanProcessing.class);
                        overridePendingTransition(0,0);
                        startActivity(loans);
                        finish();
                        return true;
                    case R.id.Adminmsg:
                        Intent message=new Intent(getApplicationContext(), adminChatDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(message);
                        finish();
                        return true;

                }
                return false;
            }
        });
        ViewPager viewPager=findViewById(R.id.sellviewpager);
        TabLayout tabLayout=findViewById(R.id.selltablayout);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Products(),"Products");
        viewPagerAdapter.addFragment(new Sell(),"Sell");
        viewPagerAdapter.addFragment(new viewsales(),"Sold");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
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
