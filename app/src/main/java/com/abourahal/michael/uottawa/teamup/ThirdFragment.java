package com.abourahal.michael.uottawa.teamup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by hocke on 2017-07-16.
 */

public class ThirdFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.third_layout,container,false);

        View aboutPage = new AboutPage(myView.getContext())
                .isRTL(false)
                .setImage(R.mipmap.logo)
                .setDescription("TeamUp allows users to organize and schedule sports meetups with their friends or with people nearby.")
                .addItem(new Element().setTitle("Version 1"))
                .addGitHub("TeamupP/TeamUp2")
                .create();
        return aboutPage;
    }
}
