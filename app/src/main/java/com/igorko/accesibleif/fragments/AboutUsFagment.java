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
    private TextView mFillerOneMailTxt;
    private TextView mFillerTwoMailTxt;
    private ImageView mFillerOneMailImage;
    private ImageView mFillerTwoMailImage;

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
        mFillerOneMailTxt = (TextView) view.findViewById(R.id.data_filler_email_txt_1);
        mFillerOneMailImage = (ImageView) view.findViewById(R.id.data_filler_email_img_1);
        mFillerTwoMailTxt = (TextView) view.findViewById(R.id.data_filler_email_txt_2);
        mFillerTwoMailImage = (ImageView) view.findViewById(R.id.data_filler_email_img_2);

        mAppVersionTxt.setText(AppContext.getInstance().getAppVersion(getActivity()));
        mWebSite.setOnClickListener(this);
        mDevMailTxt.setOnClickListener(this);
        mDevMailImage.setOnClickListener(this);

        mFillerOneMailTxt.setOnClickListener(this);
        mFillerOneMailImage.setOnClickListener(this);
        mFillerTwoMailTxt.setOnClickListener(this);
        mFillerTwoMailImage.setOnClickListener(this);

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
            case R.id.data_filler_email_txt_1:
                Navigaton.sendEmailToDataFillerOne(getActivity());
                break;
            case R.id.data_filler_email_img_1:
                Navigaton.sendEmailToDataFillerOne(getActivity());
                break;
            case R.id.data_filler_email_txt_2:
                Navigaton.sendEmailToDataFillerTwo(getActivity());
                break;
            case R.id.data_filler_email_img_2:
                Navigaton.sendEmailToDataFillerTwo(getActivity());
                break;
        }
    }
}
