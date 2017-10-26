package com.madushanka.imotorist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madushanka.imotorist.entities.GridAdapter;

import co.ceryle.fitgridview.FitGridView;

/**
 * Created by madushanka on 4/15/17.
 */

public class HomeFragment extends android.support.v4.app.Fragment {
    private FitGridView gridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);

        gridView = (FitGridView) v.findViewById(R.id.gridView);
        gridView.setFitGridAdapter(new GridAdapter(getActivity()));

        return v;

    }


}
