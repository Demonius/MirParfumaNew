package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
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
 *
 * Created by demon on 09.04.2017.
 */

public class AboutDialogFragment extends DialogFragment{

    private TextView textAbout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_about_shop, container, false);
        Toolbar toolbarAbout = (Toolbar) v.findViewById(R.id.toolbarAbout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarAbout);
        setHasOptionsMenu(true);
//        toolbarAbout.setTitle(R.string.about);
//        toolbarAbout.setNavigationIcon(android.R.drawable.btn_minus);
//        toolbarAbout.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        textAbout = (TextView) v.findViewById(R.id.textAbout);
        String text_about = getResources().getString(R.string.about_shop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textAbout.setText(Html.fromHtml(text_about, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textAbout.setText(Html.fromHtml(text_about));
        }
        textAbout.setMovementMethod(new ScrollingMovementMethod());
//textAbout.setText(R.string.about_shop);

//        GetTextAbout getTextAbout = new GetTextAbout();
//        String sql_query = "SELECT content FROM modx_site_content where id=2";
//        getTextAbout.load(sql_query);


        return v;
    }

//    @Override
//    public void onLoad(String content) {

//    }
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    super.onCreateOptionsMenu(menu, inflater);
}
}
