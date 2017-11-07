package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.News;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetListNews;

/**
 * Created by User on 31.10.2017.
 */

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> implements GetListNews.OnLoadListNews {

    private Context context;
    private ArrayList<News> listNews = new ArrayList<>();
    private Integer LIMIT = 20;
    private Integer OFFSET = 0;
    private Boolean isLoading = false;

    public AdapterNews(Context context) {
        this.context = context;
        if (getItemCount() == 0) {
            LoadListNews(OFFSET);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterNews.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String link_image = MainActivity.URL + listNews.get(position).getImage_news();
        Picasso.with(context).load(link_image).into(holder.image);

        SpannableString text_title =new SpannableString(listNews.get(position).getTitle_news());
        text_title.setSpan(new UnderlineSpan(),0,text_title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.title.setText(text_title);

        holder.text.setText(listNews.get(position).getText_news());

        holder.card_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse(MainActivity.URL+listNews.get(position).getUri());
                Intent openLinkIntent =new Intent(Intent.ACTION_VIEW, address);
                context.startActivity(openLinkIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Integer size;
        if (listNews != null) {
            size = listNews.size();
        } else {
            size = 0;
        }
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView text;
        TextView date;
        CardView card_news;

        ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image_news);
            title = (TextView) itemView.findViewById(R.id.title_news);
            text = (TextView) itemView.findViewById(R.id.text_preview_news);
            card_news = (CardView) itemView.findViewById(R.id.card_news);

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();



            text.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        Integer position = holder.getLayoutPosition();

        if ((position == getItemCount() - 2) && (!isLoading)) {
            LoadListNews(getItemCount());
        }
    }

    private void LoadListNews(Integer offset) {
        String SQl_query = "SELECT con.id,pagetitle as title,introtext as text,createdon as date,uri,val.value as image " +
                "FROM modx_site_content as con, modx_site_tmplvar_contentvalues as val " +
                "WHERE con.parent=3 and con.published=1 and val.contentid=con.id and val.tmplvarid=1 " +
                "ORDER BY createdon DESC " +
                "LIMIT 20 " +
                "OFFSET " + Integer.toString(offset);
        isLoading = true;
        GetListNews listNews = new GetListNews(SQl_query);
        listNews.registerCallback(this);
        listNews.load();
    }

    @Override
    public void onLoadListNews(ArrayList<News> listNews) {
        Integer positionLast = getItemCount() - 1;
        this.listNews.addAll(listNews);
        notifyItemRangeChanged(positionLast, 20);
        isLoading = false;
    }
}
