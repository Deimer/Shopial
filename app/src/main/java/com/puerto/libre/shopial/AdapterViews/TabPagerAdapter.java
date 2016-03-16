package com.puerto.libre.shopial.AdapterViews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.puerto.libre.shopial.Fragments.TabFacebook;
import com.puerto.libre.shopial.Fragments.TabGoogle;
import com.puerto.libre.shopial.Fragments.TabInstagram;
import com.puerto.libre.shopial.Fragments.TabPinterest;
import com.puerto.libre.shopial.Fragments.TabProfile;
import com.puerto.libre.shopial.Fragments.TabTwitter;
import java.util.List;

/**
 * Creado por Deimer Villa on 3/5/2016.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private int page_count;
    private List<String> tab_titles;

    public TabPagerAdapter(FragmentManager fm, int pages, List<String> tabs) {
        super(fm);
        this.page_count = pages;
        this.tab_titles = tabs;
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        String provider = tab_titles.get(position);
        switch (provider){
            case "Profile":
                f = TabProfile.newInstance();
                break;
            case "instagram":
                f = TabInstagram.newInstance();
                break;
            case "facebook":
                f = TabFacebook.newInstance();
                break;
            case "google":
                f = TabGoogle.newInstance();
                break;
            case "twitter":
                f = TabTwitter.newInstance();
                break;
            case "pinterest":
                f = TabPinterest.newInstance();
                break;
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles.get(position);
    }

}

