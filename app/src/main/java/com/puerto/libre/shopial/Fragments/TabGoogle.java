package com.puerto.libre.shopial.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.puerto.libre.shopial.R;

/**
 * Creado por Deimer Villa on 3/5/2016.
 */
public class TabGoogle extends Fragment {

    public static TabGoogle newInstance() {
        return new TabGoogle();
    }

    public TabGoogle(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google, container, false);
    }

}
