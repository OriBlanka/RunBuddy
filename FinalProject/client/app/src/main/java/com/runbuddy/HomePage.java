package com.runbuddy;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    //Initialize variable
    TabLayout tabLayout;
    ViewPager viewPager;
    MapsFragment mapfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Assign variable
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);

        //Initialize array list
        ArrayList<String> arrayList = new ArrayList<>();

        //Add title in array list
        arrayList.add("Radius Settings");
        arrayList.add("Start Activities");
        arrayList.add("Show Activities");

        //Prepare view pager
        prepareViewPager(viewPager, arrayList);

        //Setup with view pager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        //Initialize main adapter
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());

        //Initialize main fragment
        mapfragment = new MapsFragment();
        Bundle bundle = new Bundle();
        //Put string
        bundle.putString("title", "map");
        //Set argument
        mapfragment.setArguments(bundle);
        adapter.addFragment(mapfragment, arrayList.get(0));

        StartActivityFragment freg = new StartActivityFragment();
        bundle.putString("title", "start");
        //Set argument
        freg.setArguments(bundle);
        adapter.addFragment(freg, arrayList.get(1));


        ShowActivitiesFragment freg1 = new ShowActivitiesFragment();
        bundle.putString("title", "start1");
        //Set argument
        freg1.setArguments(bundle);
        adapter.addFragment(freg1, arrayList.get(2));

        //Set adapter
        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {
        //Initialize array list
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        //Create constructor
        public void addFragment(Fragment fragment, String title) {
            //Add title
            arrayList.add(title);
            //Add fragment
            fragmentList.add(fragment);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //Return fragment position
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Return fragment list size
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //Return array list position
            return arrayList.get(position);
        }
    }
}