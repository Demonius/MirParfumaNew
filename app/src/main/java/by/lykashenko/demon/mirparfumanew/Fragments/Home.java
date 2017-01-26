package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 20.01.2017.
 */

public class Home extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View vFragment = inflater.inflate(R.layout.frgament_home, null);
        return vFragment;
    }
}
