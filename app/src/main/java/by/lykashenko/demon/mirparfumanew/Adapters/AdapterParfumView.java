package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetParfumInfoForId;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetRattingPrfum;
import by.lykashenko.demon.mirparfumanew.Table.BrenduAll;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;
import by.lykashenko.demon.mirparfumanew.Table.Parfums;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;
import static by.lykashenko.demon.mirparfumanew.MainActivity.message_add_favorites;

/**
 * Created by Admin on 13.06.17.
 */

public class AdapterParfumView extends RecyclerView.Adapter<AdapterParfumView.ParfumViewHolder> implements GetParfumInfoForId.LoadData, GetRattingPrfum.OnGetRattingParfum {

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public void onGetRattingParfum(HashMap<String, Integer> answer) {
        rattingParfum = answer;
        if (OFFSET == 0) notifyDataSetChanged();
        else updateNewData.onUpdateNewData(OFFSET, getItemCount() - OFFSET);
    }

    public interface UpdateNewData {
        void onUpdateNewData(Integer fromPosition, Integer count);
    }

    private UpdateNewData updateNewData;

    public void registerUpdateNewData(UpdateNewData updateNewData) {
        this.updateNewData = updateNewData;
    }

    @Override
    public void loadData(Boolean loadData) {
        if (loadData) {
            loadDataToView();
            isLoading = false;

        }
    }

    private void loadDataToView() {
        Log.d(LOG_TAG, "Ofsset =>> " + OFFSET);

        StringBuilder listId = new StringBuilder();
        for (int i = OFFSET; i < ((LIMIT + OFFSET > idParfum.size()) ? idParfum.size() : LIMIT + OFFSET); i++) {

            if (listId.length() == 0) listId.append(idParfum.get(i).parfum_id);
            else listId.append(",").append(idParfum.get(i).parfum_id);
            listParfum.add(Parfums.getParfumsForId(idParfum.get(i).parfum_id));
        }

        String sql = "SELECT thread, avg(SUBSTRING(properties,16,1)) as rating FROM modx_tickets_comments where thread in(" + listId.toString() + ") group by thread";

        GetRattingPrfum rattingPrfum = new GetRattingPrfum();
        rattingPrfum.registerGetRattingParfum(this);
        rattingPrfum.load(sql);

        Log.d(LOG_TAG, "Count add =>> " + getItemCount());


    }

    public interface ClickParfum {
        void onClickParfum(Bundle bundle);
    }

    private ClickParfum clickParfum;

    public void registerClickParfum(ClickParfum clickParfum) {
        this.clickParfum = clickParfum;
    }


    private List<BrenduAll> idParfum;
    private Context context;
    private Integer state;
    private String sex;
    private Integer LIMIT = 10;
    private Integer OFFSET = 0;
    private Boolean isLoading = false;
    private HashMap<String, Integer> rattingParfum;
    private ArrayList<HashMap<String, String>> listParfum;


    public AdapterParfumView(List<BrenduAll> idParfum, Context context, Integer state, String sex) {
        this.idParfum = idParfum;
        this.context = context;
        this.state = state;
        this.sex = sex;

        rattingParfum = new HashMap<>();
        listParfum = new ArrayList<>();
        loadDataForParfum(OFFSET);


    }

    private void loadDataForParfum(Integer offset) {
        if (idParfum.size() > (offset)) {
            OFFSET = offset;
            StringBuilder listId = new StringBuilder();
            for (int i = offset; i < (((LIMIT + offset) > idParfum.size()) ? idParfum.size() : LIMIT + OFFSET); i++) {
                if (listId.length() == 0) {
                    if (!Parfums.isExists(idParfum.get(i).parfum_id))
                        listId.append(idParfum.get(i).parfum_id);
                } else {
                    if (!Parfums.isExists(idParfum.get(i).parfum_id))
                        listId.append(",").append(idParfum.get(i).parfum_id);
                }
            }

            isLoading = true;
            Log.i(LOG_TAG, "list ID ===>" + listId.toString());
            if (listId.toString().length() > 0) {
                String sql = "SELECT id,tmplvarid,contentid,value " +
                        "FROM modx_site_tmplvar_contentvalues " +
                        "where contentid in (" + listId.toString() + ") and tmplvarid in (1,50,58,59,65,66,67,68,69,70,71,72,73,74,76,88,89) " +
                        "order by contentid,tmplvarid ASC";

                Log.i(LOG_TAG, "sql request ===>" + sql);
                GetParfumInfoForId getParfumInfoForId = new GetParfumInfoForId();
                getParfumInfoForId.registerLoadData(this);
                getParfumInfoForId.load(sql);

            } else {
                loadData(true);
            }

        }


    }

    @Override
    public void onViewAttachedToWindow(AdapterParfumView.ParfumViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        Integer position = holder.getLayoutPosition();
        Log.d(LOG_TAG, "position from load =>> " + position + ", count elements =>>> " + getItemCount() + ", offset => " + OFFSET + ", Limit ->> " + LIMIT);
        if (OFFSET + LIMIT <= getItemCount()) {
            if ((position == getItemCount() - 1) && (!isLoading)) {
                loadDataForParfum(getItemCount());
            }
        } else {
            Log.d(LOG_TAG, "offset + limit => " + (OFFSET + LIMIT));
        }
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
    public AdapterParfumView.ParfumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (state == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum_grid, parent, false);
        }
        return new ParfumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterParfumView.ParfumViewHolder holder, final int position) {

        //new, key =58

        final HashMap<String, String> info = listParfum.get(position);
        String url_image_parfum = MainActivity.URL + info.get("1");
        Picasso.with(context).load(url_image_parfum).into(holder.imageParfum);

        String price = info.get("50");
        Log.d(LOG_TAG, "price =>" + price);
        StringBuilder textPrice = new StringBuilder();
        if (price == null) textPrice.append(context.getResources().getString(R.string.no_parfum));
        else
            textPrice.append(context.getResources().getString(R.string.from))
                    .append(" ")
                    .append(price)
                    .append(" ")
                    .append(context.getResources().getString(R.string.rubblei));
        holder.priceParfum.setText(textPrice.toString());

        final String id_parfum = idParfum.get(position).parfum_id;
        holder.nameParfum.setText(idParfum.get(position).parfum_name.substring(15, idParfum.get(position).parfum_name.length()).trim());
        final Float ratting = (rattingParfum.get(id_parfum) != null) ? (float) rattingParfum.get(id_parfum) : (float) 0;
        holder.ratingParfum.setRating(ratting);

        if (Favorites.isFavorites(idParfum.get(position).parfum_id)) {
            holder.fState.setVisibility(View.VISIBLE);
            holder.fState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Favorites.deleteFromFavorites(idParfum.get(position).parfum_id);
                    Intent serviceStartedIntent = new Intent(message_add_favorites);
                    serviceStartedIntent.putExtra("update", 2);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(serviceStartedIntent);
                }
            });
        } else {
            holder.fState.setVisibility(View.INVISIBLE);
        }


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("brend", idParfum.get(position).brend_name);
                bundle.putString("id", idParfum.get(position).parfum_id);
                bundle.putString("name", idParfum.get(position).parfum_name.substring(15, idParfum.get(position).parfum_name.length()).trim());
                bundle.putFloat("ratting", ratting);
                bundle.putSerializable("info", info);

                clickParfum.onClickParfum(bundle);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listParfum.size();
    }

}
