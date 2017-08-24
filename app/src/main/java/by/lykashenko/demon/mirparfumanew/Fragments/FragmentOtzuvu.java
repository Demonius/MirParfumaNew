package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.AdapterOtzuvu;
import by.lykashenko.demon.mirparfumanew.Adapters.Otzuvu;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetOtzuvuForParfum;

import static by.lykashenko.demon.mirparfumanew.BrendActivity.titleToolbar;


/**
 * Created by Admin on 03.07.17.
 */

public class FragmentOtzuvu extends Fragment implements GetOtzuvuForParfum.GetOtzuvuList {

    private RecyclerView listOtzuvu;
    private FloatingActionButton addOtzuv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_otzuvu, container, false);

        listOtzuvu = (RecyclerView) v.findViewById(R.id.listOtzuvu);
        addOtzuv = (FloatingActionButton) v.findViewById(R.id.adOtzuvu);

        addOtzuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Добавить отзыв", Toast.LENGTH_SHORT).show();
            }
        });

        String id = getArguments().getString("id");
        String count = getArguments().getString("count");

        String title = count.equals("0") ? getResources().getString(R.string.no_otzuvu) : getResources().getString(R.string.otzuvu)+" ("+count+")";
        titleToolbar.setText(title);
        GetOtzuvu(id);

        return v;
    }

    private void GetOtzuvu(String id) {
        String sql_get_otzuvu = "SELECT name, text, rating, createdon " +
                "FROM modx_tickets_comments " +
                "where thread="+id+
                " ORDER BY createdon DESC";

        GetOtzuvuForParfum getOtzuvuForParfum = new GetOtzuvuForParfum();
        getOtzuvuForParfum.registerGetOtzuvuList(this);
        getOtzuvuForParfum.load(sql_get_otzuvu);
    }

    @Override
    public void onGetOtzuvuList(ArrayList<Otzuvu> otzuvu) {

        listOtzuvu.setHasFixedSize(true);
        LinearLayoutManager lv = new LinearLayoutManager(getContext());
        listOtzuvu.setLayoutManager(lv);

        AdapterOtzuvu adapter = new AdapterOtzuvu(otzuvu);
        listOtzuvu.setAdapter(adapter);

    }
}
