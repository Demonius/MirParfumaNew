package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.os.Bundle;
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

/**
 * Created by Admin on 13.06.17.
 */

public class AdapterParfumView extends RecyclerView.Adapter<AdapterParfumView.ParfumViewHolder> {

    public interface ClickParfum{
        void onClickParfum(Bundle bundle);
    }

    private ClickParfum clickParfum;

    public void registerClickParfum(ClickParfum clickParfum){
        this.clickParfum=clickParfum;
    }

    private ArrayList<ParfumCollection> parfumCollections;
    private Context context;
    private Integer state;

    public AdapterParfumView(ArrayList<ParfumCollection> parfumCollections, Context context, Integer state) {
        this.parfumCollections = parfumCollections;
        this.context=context;
        this.state=state;

    }

    public class ParfumViewHolder extends RecyclerView.ViewHolder {

        ImageView imageParfum;
        TextView nameParfum, priceParfum, priceFor;
        RatingBar ratingParfum;
        CardView cv;

        public ParfumViewHolder(View itemView) {

            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardViewParfum);
            imageParfum = (ImageView) itemView.findViewById(R.id.imageParfumCatalog);
            nameParfum = (TextView) itemView.findViewById(R.id.nameParfumCatalog);
            priceFor = (TextView) itemView.findViewById(R.id.cenaFor);
            priceParfum = (TextView) itemView.findViewById(R.id.priceParfumMin);
            ratingParfum = (RatingBar) itemView.findViewById(R.id.ratingBarParfum);
            ratingParfum.setNumStars(5);
            ratingParfum.isIndicator();

        }
    }


    @Override
    public AdapterParfumView.ParfumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (state == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum_grid, parent, false);
        }
        AdapterParfumView.ParfumViewHolder parfumViewHolder = new AdapterParfumView.ParfumViewHolder(v);
        return parfumViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterParfumView.ParfumViewHolder holder, final int position) {
        String url_image_brend = parfumCollections.get(position).getImageParfum();
        Picasso.with(context).load(url_image_brend).into(holder.imageParfum);
        holder.nameParfum.setText(parfumCollections.get(position).getNameParfum());
        String priceParfum = parfumCollections.get(position).getCenaParfum();
        if(priceParfum.equals(context.getResources().getString(R.string.no_parfum))){
            holder.priceParfum.setTextSize(22);
        }else{
            holder.priceParfum.setTextSize(25);

        }
        holder.priceParfum.setText(priceParfum);
        holder.priceFor.setText(parfumCollections.get(position).getCenaFor());
        holder.ratingParfum.setRating((float) parfumCollections.get(position).getReatingParfum());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", parfumCollections.get(position).getIdParfum());
                bundle.putString("name", parfumCollections.get(position).getNameParfum());
//                    bundle.putString("image", parfumCollections.get(position).getImageParfum());
//                    bundle.putString("idBrend", id);
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
