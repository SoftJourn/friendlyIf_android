package com.igorko.accesibleif.fragments;

/**
 * Created by Igorko on 21.10.2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.igorko.accesibleif.R;

public class HowItsWorkFagment extends Fragment{

    public static HowItsWorkFagment newInstance() {
        HowItsWorkFagment howItsWorkFagment = new HowItsWorkFagment();
        return howItsWorkFagment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.how_its_work_fragment, container, false);
        return view;
    }
}
