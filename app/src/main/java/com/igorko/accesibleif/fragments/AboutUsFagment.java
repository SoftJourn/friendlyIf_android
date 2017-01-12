package com.igorko.accesibleif.fragments;

/**
 * Created by Igorko on 21.10.2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.utils.DialogUtils;
import com.igorko.accesibleif.utils.Navigaton;

public class AboutUsFagment extends Fragment implements View.OnClickListener{

    private TextView mAppVersionTxt;
    private TextView mWebSite;
    private TextView mDevMailTxt;
    private ImageView mDevMailImage;
    private TextView mFillerMailTxt;
    private ImageView mFillerMailImage;

    public static AboutUsFagment newInstance() {
        AboutUsFagment aboutUsFagment = new AboutUsFagment();
        return aboutUsFagment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_us_fragment, container, false);
        mAppVersionTxt = (TextView) view.findViewById(R.id.app_version);
        mWebSite =(TextView) view.findViewById(R.id.website);
        mDevMailTxt = (TextView) view.findViewById(R.id.developer_email_txt);
        mDevMailImage = (ImageView) view.findViewById(R.id.developer_email_img);
        mFillerMailTxt = (TextView) view.findViewById(R.id.data_filler_email_txt);
        mFillerMailImage = (ImageView) view.findViewById(R.id.data_filler_email_img);

        mAppVersionTxt.setText(AppContext.getInstance().getAppVersion(getActivity()));
        mWebSite.setOnClickListener(this);
        mDevMailTxt.setOnClickListener(this);
        mDevMailImage.setOnClickListener(this);
        mFillerMailTxt.setOnClickListener(this);
        mFillerMailImage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.website:
                DialogUtils.showGotoOpenstreetmapSiteAlert(getActivity());
                break;
            case R.id.developer_email_txt:
                Navigaton.sendEmailToDeveloper(getActivity());
                break;
            case R.id.developer_email_img:
                Navigaton.sendEmailToDeveloper(getActivity());
                break;
            case R.id.data_filler_email_txt:
                Navigaton.sendEmailToDataFiller(getActivity());
                break;
            case R.id.data_filler_email_img:
                Navigaton.sendEmailToDataFiller(getActivity());
                break;
        }
    }
}
