package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.lykashenko.demon.mirparfumanew.Adapters.AdapterNews;
import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 22.04.2017.
 */

public class InfoDialogFragment extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoActionBar);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.dialog_info, container, false);

        Toolbar toolbarInfo = v.findViewById(R.id.toolbar_info);


        toolbarInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        RecyclerView viewNews = v.findViewById(R.id.recyclerViewInfo);
        viewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterNews adapter = new AdapterNews(getContext());
        viewNews.setAdapter(adapter);

//        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        };

        return v;
    }

}
