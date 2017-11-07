package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.TextAbout;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetTextAbout;

/**
 * Created by demon on 09.04.2017.
 */

public class AboutDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoActionBar);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_about_shop, container, false);
        Toolbar toolbarAbout = (Toolbar) v.findViewById(R.id.toolbar_shop_about);
//        toolbarAbout.inflateMenu(R.menu.main);
//        toolbarAbout.setNavigationIcon(R.drawable.arrow_left);
        toolbarAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
//        toolbarAbout.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case android.R.id.home:
//
//                        getDialog().dismiss();
//                        return true;
//                }
//                return false;
//            }
//        });

        TextView textAbout = (TextView) v.findViewById(R.id.textAboutFor);
        String text_about = getResources().getString(R.string.about_shop_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textAbout.setText(Html.fromHtml(text_about, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textAbout.setText(Html.fromHtml(text_about));
        }
        textAbout.setMovementMethod(new ScrollingMovementMethod());
        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//
//                getDialog().dismiss();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }
}
