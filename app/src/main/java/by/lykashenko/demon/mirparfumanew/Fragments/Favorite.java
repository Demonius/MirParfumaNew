package by.lykashenko.demon.mirparfumanew.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.Adapters.AdapterParfumView;
import by.lykashenko.demon.mirparfumanew.Adapters.ParfumCollection;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 *
 * Created by demon on 08.02.2017.
 */

public class Favorite extends Fragment implements AdapterParfumView.ClickParfum {

    @Override
    public void onClickParfum(Bundle bundle) {
        bundle.putInt("state", 2);
        goToCatalog.onGoToCatalog(2, bundle);
    }

    public interface GoToCatalog {
        void onGoToCatalog(Integer state, @Nullable Bundle bundle);
    }

    private GoToCatalog goToCatalog;

    public void registerGoToCatalog(GoToCatalog goToCatalog) {
        this.goToCatalog = goToCatalog;
    }

    private Button btnGo;
    private TextView textViewTop, textViewBotom;
    private ImageView imageViewFavorite;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActiveAndroid.beginTransaction();
        List<Favorites> favoritesList = new Select().from(Favorites.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        ArrayList<ParfumCollection> parfumList = new ArrayList<>();
        for (Favorites favorite : favoritesList
                ) {

            ParfumCollection parfum = new ParfumCollection();
            parfum.setIdParfum(favorite.id_parfum);
            parfum.setNameParfum(favorite.name_parfum);
            parfum.setReatingParfum(favorite.ratting_parfum);
            parfum.setImageParfum(favorite.image_parfum);
            parfum.setCenaFor("Ваша цена за "+favorite.cena_for);
            parfum.setCenaParfum("От "+favorite.cena_parfum+" р.");

            parfumList.add(parfum);
        }
        Log.e(LOG_TAG, "col-vo favorites =>" + favoritesList.size() + "; перезаписано => " + parfumList.size());
        View vFragment = null;
        if (favoritesList.isEmpty()) {

            vFragment = inflater.inflate(R.layout.fragment_favorite_empty, null);

            imageViewFavorite = (ImageView) vFragment.findViewById(R.id.imageEmpty);
            imageViewFavorite.setImageResource(R.drawable.empty_favorite);

            textViewTop = (TextView) vFragment.findViewById(R.id.textViewTopEmpty);
            textViewTop.setText(R.string.empty_favorite);

            textViewBotom = (TextView) vFragment.findViewById(R.id.textViewBotomEmpty);
            textViewBotom.setText(R.string.text_empty_favorite);

            btnGo = (Button) vFragment.findViewById(R.id.btnEmpty);
            btnGo.setText(R.string.button_favorite);

            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCatalog.onGoToCatalog(1, null);
//                Toast.makeText(getContext(), "Переход в каталог", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            vFragment = inflater.inflate(R.layout.fragment_favorite, null);

            RecyclerView listFavorite = (RecyclerView) vFragment.findViewById(R.id.rw_favorites_list);
            listFavorite.setHasFixedSize(true);
            LinearLayoutManager lv = new LinearLayoutManager(getContext());
            listFavorite.setLayoutManager(lv);
            AdapterParfumView adapter = new AdapterParfumView(parfumList, getContext(), 1);
            adapter.registerClickParfum(this);
            listFavorite.setAdapter(adapter);
        }

        return vFragment;
    }
}
