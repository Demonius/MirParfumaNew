package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import by.lykashenko.demon.mirparfumanew.Adapters.AdapterViewForOtzuvu;
import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by User on 01.11.2017.
 */

public class OtzuvuDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoActionBar);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.dialog_otzuvu, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_otzuvu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        RecyclerView listOtzuvu = (RecyclerView) v.findViewById(R.id.recyclerViewOtzuvu);
        listOtzuvu.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterViewForOtzuvu adapter = new AdapterViewForOtzuvu(getContext());
        listOtzuvu.setAdapter(adapter);


        return v;
    }
}
