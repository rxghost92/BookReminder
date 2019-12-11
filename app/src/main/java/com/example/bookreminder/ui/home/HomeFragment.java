package com.example.bookreminder.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookreminder.R;
import com.example.bookreminder.ui.home.tab.PageAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;


public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setText("Totali");
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(1).setText("In Corso");
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(2).setText("Completati");
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(3).setText("Da Iniziare");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fragManager = getActivity().getSupportFragmentManager();

        final ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        PageAdapter pageAdapter = new PageAdapter(fragManager, tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }


}