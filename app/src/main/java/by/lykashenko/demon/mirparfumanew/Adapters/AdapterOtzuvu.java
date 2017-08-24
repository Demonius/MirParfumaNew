package by.lykashenko.demon.mirparfumanew.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.R;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 03.07.17.
 */

public class AdapterOtzuvu extends RecyclerView.Adapter<AdapterOtzuvu.OtzuvuViewHolder> {

    private ArrayList<Otzuvu> listOtzuvu;

    public AdapterOtzuvu(ArrayList<Otzuvu> listOtzuvu) {
        this.listOtzuvu=listOtzuvu;
    }

    @Override
    public OtzuvuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_otzuvu, parent, false);
        OtzuvuViewHolder viewHolder = new OtzuvuViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OtzuvuViewHolder holder, int position) {

        holder.name.setText(listOtzuvu.get(position).getName());
        holder.create_date.setText(listOtzuvu.get(position).getCreated_date());
        holder.text_otzuv.setText(listOtzuvu.get(position).getText());
        holder.ratingBar.setRating(Float.parseFloat(listOtzuvu.get(position).getRatting()));
//        Log.d(LOG_TAG,"ratting otzuvu => "+listOtzuvu.get(position).getRatting());
    }

    @Override
    public int getItemCount() {
        return listOtzuvu.size();
    }

    public class OtzuvuViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView create_date;
        private TextView text_otzuv;
        private RatingBar ratingBar;


        public OtzuvuViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            create_date = (TextView) itemView.findViewById(R.id.date_create);
            text_otzuv = (TextView) itemView.findViewById(R.id.otzuv);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratting_star);
            ratingBar.setNumStars(5);
            ratingBar.setRating(0);
            ratingBar.setIsIndicator(true);

        }
    }
}
