package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;
import static by.lykashenko.demon.mirparfumanew.MainActivity.message_add_favorites;

/**
 * Created by User on 04.11.2017.
 */

public class AdapterParfumFavorite extends RecyclerView.Adapter<AdapterParfumFavorite.ParfumViewHolder> {

    public interface ClickParfum {
        void onClickParfum(Bundle bundle);
    }

    private ClickParfum clickParfum;

    public void registerClickParfum(ClickParfum clickParfum) {
        this.clickParfum = clickParfum;
    }

    private ArrayList<ParfumCollection> parfumCollections;
    private Context context;
    private Integer state;

    public AdapterParfumFavorite(ArrayList<ParfumCollection> parfumCollections, Context context, Integer state) {
        this.parfumCollections = parfumCollections;
        this.context = context;
        this.state = state;

    }


    public class ParfumViewHolder extends RecyclerView.ViewHolder {

        ImageView imageParfum;
        TextView nameParfum, priceParfum, priceFor;
        RatingBar ratingParfum;
        CardView cv;
        ImageView fState;

        public ParfumViewHolder(View itemView) {

            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardViewParfum);
            imageParfum = (ImageView) itemView.findViewById(R.id.imageParfumCatalog);
            nameParfum = (TextView) itemView.findViewById(R.id.nameParfumCatalog);
            priceFor = (TextView) itemView.findViewById(R.id.cenaFor);
            priceParfum = (TextView) itemView.findViewById(R.id.priceParfumMin);
            ratingParfum = (RatingBar) itemView.findViewById(R.id.ratingBarParfum);
            fState = (ImageView) itemView.findViewById(R.id.image_favorite_state);
            ratingParfum.setNumStars(5);
            ratingParfum.isIndicator();

        }
    }


    @Override
    public AdapterParfumFavorite.ParfumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (state == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum_grid, parent, false);
        }
        AdapterParfumFavorite.ParfumViewHolder parfumViewHolder = new AdapterParfumFavorite.ParfumViewHolder(v);
        return parfumViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterParfumFavorite.ParfumViewHolder holder, final int position) {

        String url_image_brend = parfumCollections.get(position).getImageParfum();
        Picasso.with(context).load(url_image_brend).into(holder.imageParfum);
        holder.nameParfum.setText(parfumCollections.get(position).getNameParfum());
        String priceParfum = parfumCollections.get(position).getCenaParfum();

        if (Favorites.isFavorites(parfumCollections.get(position).getIdParfum())) {
            holder.fState.setVisibility(View.VISIBLE);
            holder.fState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Favorites.deleteFromFavorites(parfumCollections.get(position).getIdParfum());
                    Intent serviceStartedIntent = new Intent(message_add_favorites);
                    serviceStartedIntent.putExtra("update", 2);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(serviceStartedIntent);
                }
            });
        } else {
            holder.fState.setVisibility(View.INVISIBLE);
        }


//        if(priceParfum.equals(context.getResources().getString(R.string.no_parfum))){
//            holder.priceParfum.setTextSize(17);
//        }else{
//            holder.priceParfum.setTextSize(17);
//
//        }
        holder.priceParfum.setText(priceParfum);
        if (parfumCollections.get(position).getCenaFor() != "0") {
            holder.priceFor.setText(parfumCollections.get(position).getCenaFor());
        }
        holder.ratingParfum.setRating((float) parfumCollections.get(position).getReatingParfum());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", parfumCollections.get(position).getIdParfum());
                bundle.putString("name", parfumCollections.get(position).getNameParfum());
                bundle.putInt("ratting", parfumCollections.get(position).getReatingParfum());

                clickParfum.onClickParfum(bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return parfumCollections.size();
    }

}
