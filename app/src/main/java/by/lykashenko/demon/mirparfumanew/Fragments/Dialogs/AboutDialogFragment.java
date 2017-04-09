package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 09.04.2017.
 */

public class AboutDialogFragment extends DialogFragment {

 private Toolbar toolbarAbout;

 public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
  View v = inflater.inflate(R.layout.dialog_about_shop, null);
toolbarAbout = (Toolbar) v.findViewById(R.id.toolbarAbout);
  AppCompatActivity activity = (AppCompatActivity) getActivity();
  activity.setSupportActionBar(toolbarAbout);
//  activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


  return v;
 }
}
