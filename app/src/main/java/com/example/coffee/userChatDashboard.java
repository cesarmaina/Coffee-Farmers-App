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

import com.example.coffee.fragments.chatFragment;
import com.example.coffee.fragments.userFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class userChatDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchatdashboard);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.messageactivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        Intent me=new Intent(getApplicationContext(),Home.class);
                        overridePendingTransition(0,0);
                        startActivity(me);
                        finish();
                        return true;
                    case R.id.messageactivity:
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
                    case R.id.dashboard:
                        Intent message=new Intent(getApplicationContext(), userDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(message);
                        finish();
                        return true;

                }
                return false;
            }
        });
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new chatFragment(),"Chats");
        viewPagerAdapter.addFragment(new userFragment(),"Users");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{
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
