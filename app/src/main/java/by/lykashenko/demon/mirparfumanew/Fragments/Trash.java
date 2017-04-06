package by.lykashenko.demon.mirparfumanew.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 08.02.2017.
 */

public class Trash extends Fragment {

    private Button btnGo;
    private TextView textViewTop, textViewBotom;
    private ImageView imageViewTrash;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vFragment = inflater.inflate(R.layout.fragment_favorite_empty, null);

        imageViewTrash = (ImageView) vFragment.findViewById(R.id.imageEmpty);
        imageViewTrash.setImageResource(R.drawable.empty_korzina);

        textViewTop = (TextView) vFragment.findViewById(R.id.textViewTopEmpty);
        textViewTop.setText(R.string.empty_trash);

        textViewBotom = (TextView) vFragment.findViewById(R.id.textViewBotomEmpty);
        textViewBotom.setText(R.string.text_empty_trash);

        btnGo = (Button) vFragment.findViewById(R.id.btnEmpty);
        btnGo.setText(R.string.button_favorite);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Переход в каталог", Toast.LENGTH_LONG).show();
            }
        });

        return vFragment;
    }
}
