package com.example.bookreminder.ui.home.tab;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    int counttab;

    public PageAdapter(FragmentManager fm, int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:
                Tab_Tutti_Libri tabTuttiLibri = new Tab_Tutti_Libri();
                return tabTuttiLibri;

            case 1:
                Tab_In_Corso tabInCorso = new Tab_In_Corso();
                return tabInCorso;

            case 2:
                Tab_Completati tab_completati = new Tab_Completati();
                return tab_completati;

            case 3:
                Tab_Futuri tabFuturi = new Tab_Futuri();
                return tabFuturi;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
