package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetListOtzuv;

/**
 * Created by User on 01.11.2017.
 */

public class AdapterViewForOtzuvu extends RecyclerView.Adapter<AdapterViewForOtzuvu.ViewHolder> implements GetListOtzuv.InterfaceCallbackLoadOtzuvu {

    private ArrayList<OtzuvuForMirParfuma> listOtzuvu = new ArrayList<>();
    private Context context;
    private Integer LIMIT = 20;
    private Integer OFFSET = 0;
    private Boolean isLoading = false;

    public AdapterViewForOtzuvu(Context context) {
        this.context = context;
        if (getItemCount() == 0) {
            loadOtzuvu(OFFSET);
        }
    }

    private void loadOtzuvu(Integer offset) {

        String sql_query = "SELECT id,text,name,createdon,createdby,rating " +
                "FROM `modx_tickets_comments` " +
                "where thread = 27969 and " +
                "published =1 " +
                "ORDER BY `modx_tickets_comments`.`createdon` DESC LIMIT " + Integer.toString(LIMIT) + " OFFSET " + Integer.toString(offset);

        isLoading = true;
        GetListOtzuv getListOtzuvu = new GetListOtzuv(sql_query);
        getListOtzuvu.registerCallbackLoadOtzuvu(this);
        getListOtzuvu.load();


    }

    @Override
    public void onViewAttachedToWindow(AdapterViewForOtzuvu.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        Integer position = holder.getLayoutPosition();

        if ((position == getItemCount() - 2) && (!isLoading)) {
            loadOtzuvu(getItemCount());
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterViewForOtzuvu.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_otzuv, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Integer createdBy = listOtzuvu.get(position).getCreate_by();
        String textOtzuv = listOtzuvu.get(position).getText_otzuva();

        if (createdBy == 3) {

            holder.layout.setMaxHeight(0);
            holder.textAdministration.setText(textOtzuv.trim());

        } else {

            holder.nameClient.setText(listOtzuvu.get(position).getName_client());
            Date date = null;
            SimpleDateFormat dateIn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                 date = dateIn.parse(listOtzuvu.get(position).getCreate_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat dateOut = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());


            holder.createdDate.setText(dateOut.format(date));
            holder.rating.setRating(listOtzuvu.get(position).getRating());
            String answer = "ОТВЕТ:";
            Integer k = textOtzuv.indexOf(answer);
            if (k == -1) {
                holder.textOtzuv.setText(textOtzuv);
                holder.textAdministration.setHeight(0);
            } else {
                String textClient = textOtzuv.substring(0, k).trim();
                String textAdministrator = textOtzuv.substring(k+answer.length(),textOtzuv.length()).trim();
                holder.textOtzuv.setText(textClient);
                holder.textAdministration.setText(textAdministrator);
            }
        }


    }

    @Override
    public int getItemCount() {
        return listOtzuvu.size();
    }

    @Override
    public void CallbackLoadOtzuvu(ArrayList<OtzuvuForMirParfuma> listOtzuvu) {
        Integer position = listOtzuvu.size() - 1;
        this.listOtzuvu.addAll(listOtzuvu);
        notifyItemRangeChanged(position, LIMIT);
        isLoading = false;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutOtzuvu;
        TextView nameClient;
        TextView createdDate;
        TextView textOtzuv;
        TextView textAdministration;
        ConstraintLayout layout;
        RatingBar rating;

        ViewHolder(View itemView) {
            super(itemView);

            layoutOtzuvu = (LinearLayout) itemView.findViewById(R.id.layout_otzuvu);
            nameClient = (TextView) itemView.findViewById(R.id.name_client);
            createdDate = (TextView) itemView.findViewById(R.id.date_otzuv);
            textOtzuv = (TextView) itemView.findViewById(R.id.text_otzuv);
            textAdministration = (TextView) itemView.findViewById(R.id.answer_text_otzuv);
            layout = (ConstraintLayout) itemView.findViewById(R.id.otzuv_client_layput);
            rating = (RatingBar) itemView.findViewById(R.id.ratting_otzuv);
        }
    }
}
