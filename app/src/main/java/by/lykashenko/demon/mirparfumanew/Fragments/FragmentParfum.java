package by.lykashenko.demon.mirparfumanew.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import at.blogc.android.views.ExpandableTextView;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Parfum;
import by.lykashenko.demon.mirparfumanew.Adapters.CenaParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountOtzuvuParfum;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetFullInfoOfParfum;
import by.lykashenko.demon.mirparfumanew.Table.*;
import by.lykashenko.demon.mirparfumanew.Table.Trash;

import static by.lykashenko.demon.mirparfumanew.BrendActivity.titleToolbar;
import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;
import static by.lykashenko.demon.mirparfumanew.MainActivity.message_add_favorites;
import static by.lykashenko.demon.mirparfumanew.StartActivity.name_receiver;
import static java.lang.Math.round;

/**
 * Created by demon on 09.06.2017.
 */

public class FragmentParfum extends Fragment implements CountOtzuvuParfum.CountLoad {
    private TextView cenaForMl;
    private TextView cenaParfum;
    private RadioGroup radioChoiseMl;
    private ExpandableTextView aboutParfum;
    private ArrayList<CenaParfum> cenaParfums = new ArrayList<>();
    private TextView character_info, brend_character, brend_aromat, brend_type, brend_for, brend_year, brend_semeistvo, brend_first_note, brend_contry;
    private String brend_text;
    private TextView text_about, otzuvu_parfum;
    private Integer state_favorites = 0;
    private String[] imageList;
    private CarouselView imageParfum;
    private String name;
    private String name_parfum;
    private TextView nameParfum;
    private Integer rattingParfum;
    private String id, count_otzuvu;

    public interface LoadListOtzuvu {
        void onLoadListOtzuvu(String id, String count);
    }

    private LoadListOtzuvu loadListOtzuvu;

    public void registerLoadOtzuvu(LoadListOtzuvu loadListOtzuvu) {
        this.loadListOtzuvu = loadListOtzuvu;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parfum, container, false);
        id = getArguments().getString("id");
        Log.d(LOG_TAG, "id_Parfum => " + id);
        name = getArguments().getString("name");
        String nameparfum = "";
        String[] name_list = name.split(" ");
        Pattern pattern = Pattern.compile("[а-яА-ЯёЁ]" + "*");
        for (int i = 0; i < name_list.length; i++) {
            if (!pattern.matcher(name_list[i]).matches()) {
                if (!name_list[i].equals(" ")) {
                    nameparfum = nameparfum + " " + name_list[i];
                }
            }
        }


        titleToolbar.setText(nameparfum);

        imageParfum = (CarouselView) v.findViewById(R.id.carouselViewParfum);

        imageParfum.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int i, ImageView imageView) {
                imageView.setMaxHeight((int) round(imageParfum.getHeight() * 0.8));
                Picasso.with(getContext()).load(imageList[i]).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        });

        nameParfum = (TextView) v.findViewById(R.id.nameParfumCard);
        nameParfum.setText(nameparfum);

        cenaForMl = (TextView) v.findViewById(R.id.cenaOtParfum);
        cenaParfum = (TextView) v.findViewById(R.id.cenaParfum);

        radioChoiseMl = (RadioGroup) v.findViewById(R.id.choise_ml);

        ViewStub viewStubCharacter1 = (ViewStub) v.findViewById(R.id.character);
        viewStubCharacter1.inflate();

        ViewStub viewStubAboutParfum = (ViewStub) v.findViewById(R.id.view_stub_about);
        viewStubAboutParfum.inflate();

        character_info = (TextView) v.findViewById(R.id.character_info);
        brend_character = (TextView) v.findViewById(R.id.brend_character);
        brend_aromat = (TextView) v.findViewById(R.id.brend_aromat);
        brend_type = (TextView) v.findViewById(R.id.brend_type);
        brend_for = (TextView) v.findViewById(R.id.brend_for);
        brend_year = (TextView) v.findViewById(R.id.brend_year);
        brend_semeistvo = (TextView) v.findViewById(R.id.brend_semeistvo);
        brend_first_note = (TextView) v.findViewById(R.id.brend_first_nota);
        brend_contry = (TextView) v.findViewById(R.id.brend_contry);
        text_about = (TextView) v.findViewById(R.id.about_parfum);

        aboutParfum = (ExpandableTextView) v.findViewById(R.id.aboutParfum);

        otzuvu_parfum = (TextView) v.findViewById(R.id.otzuvu_parfum);
        String sql = "SELECT count(1) FROM modx_tickets_comments where thread = " + id + " ORDER BY createdon DESC";
        CountOtzuvuParfum countOtzuvuParfum = new CountOtzuvuParfum();
        countOtzuvuParfum.registerCountLoad(this);
        countOtzuvuParfum.load(sql);
        LinearLayout otzuvu_click = (LinearLayout) v.findViewById(R.id.otzuv_layout);
        otzuvu_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListOtzuvu.onLoadListOtzuvu(id, count_otzuvu);
                Log.d(LOG_TAG, "id => " + id + "| count => " + count_otzuvu);
            }
        });

        rattingParfum = getArguments().getInt("ratting");
        final RatingBar ratingParfum = (RatingBar) v.findViewById(R.id.ratting_parfum);
        ratingParfum.setNumStars(5);
        ratingParfum.isIndicator();
        ratingParfum.setRating((float) rattingParfum);
        //share
        Button buttonShare = (Button) v.findViewById(R.id.button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Поделиться в соц. сетях", Toast.LENGTH_SHORT).show();
            }
        });

//добавлено в избранные или нет

        Boolean check = CheckParfumFavorites(id);

        final LinearLayout add_favorites = (LinearLayout) v.findViewById(R.id.layout_add_favorites);
        if (!check) {
            add_favorites.setBackgroundColor(getResources().getColor(R.color.background_add_favorites));
        } else {
            add_favorites.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }
        add_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveAndroid.beginTransaction();

                if (!CheckParfumFavorites(id)) {

                    CenaParfum addcena = null;

                    try {
                        addcena = cenaParfums.get(radioChoiseMl.getCheckedRadioButtonId());
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    String cenaFor;
                    String cena_parfum;
                    if (addcena != null) {
                        cenaFor = addcena.getTitle();
                        cena_parfum = addcena.getPrice();
                    } else {
                        cenaFor = "0";
                        cena_parfum = getResources().getString(R.string.no_parfum);
                    }
                    //name
                    //imageList[0]
//                    rattingParfum

                    Favorites favorites = new Favorites();
                    favorites.id_parfum = id;
                    favorites.name_parfum = name;
                    favorites.image_parfum = imageList[0];
                    favorites.ratting_parfum = rattingParfum;
                    favorites.cena_for = cenaFor;
                    favorites.cena_parfum = cena_parfum;
                    favorites.save();


                    add_favorites.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {

                    new Delete().from(Favorites.class).where("id_parfum = ?", id).execute();
                    add_favorites.setBackgroundColor(getResources().getColor(R.color.background_add_favorites));
                }

                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();

                Intent serviceStartedIntent = new Intent(message_add_favorites);
                serviceStartedIntent.putExtra("update", 2);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(serviceStartedIntent);
            }
        });

        final LinearLayout layoutAddTrash = (LinearLayout) v.findViewById(R.id.layout_add_trash);
        if (checkTrash(id)) {
            Log.d(LOG_TAG, "true");
            layoutAddTrash.setBackgroundResource(R.color.colorFonPrimary);
        } else {
            Log.d(LOG_TAG, "false");
            layoutAddTrash.setBackgroundResource(R.color.colorPrimary);
        }
        layoutAddTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkTrash(id)) {
                    Log.d(LOG_TAG, "click false");
                    layoutAddTrash.setBackgroundColor(getResources().getColor(R.color.colorFonPrimary));
                    CenaParfum addcena = null;

                    try {
                        addcena = cenaParfums.get(radioChoiseMl.getCheckedRadioButtonId());
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    String cenaFor;
                    String cena_parfum;
                    if (addcena != null) {
                        cenaFor = addcena.getTitle();
                        cena_parfum = addcena.getPrice();
                    } else {
                        cenaFor = "0";
                        cena_parfum = getResources().getString(R.string.no_parfum);
                    }

                    Trash trash = new Trash();
                    trash.id_parfum = id;
                    trash.name_parfum = name;
                    trash.image_parfum = imageList[0];
                    trash.ratting_parfum = Integer.toString(rattingParfum);
                    trash.cena_for = cenaFor;
                    trash.cena_parfum = cena_parfum;
                    trash.count_parfum = 1;
                    trash.save();

                    Intent serviceStartedIntent = new Intent(message_add_favorites);
                    serviceStartedIntent.putExtra("update", 3);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(serviceStartedIntent);
                }

            }
        });


        Toast.makeText(getContext(), "id parfum => " + id, Toast.LENGTH_SHORT).show();

        String sql_query_info_parfum = "SELECT tmplvarid,value FROM modx_site_tmplvar_contentvalues where contentid=" +
                id + " and tmplvarid IN (1,11,65,66,67,68,69,70,73,74,76)";

        GetFullInfoOfParfum getFullInfo = new GetFullInfoOfParfum(getContext());


        getFullInfo.regestrationLoadInfo(new GetFullInfoOfParfum.LoadInfo() {
            @Override
            public void onLoadInfo(ArrayList<Parfum> parfum) {
                String text_about_parfum = "";
                for (Parfum oneParfum : parfum) {

                    switch (oneParfum.getTmplvarid()) {
                        case "1":
                            String image = MainActivity.URL + oneParfum.getValue();
                            imageList = new String[]{image};
                            imageParfum.setPageCount(imageList.length);
                            break;
                        case "11":
                            String brend = oneParfum.getValue();
                            brend_character.setText(getResources().getString(R.string.brend) + " " + brend);
                            break;

                        case "65":
                            String aromat = getResources().getString(R.string.aromat) + " " + oneParfum.getValue();
                            brend_aromat.setText(aromat);
                            break;

                        case "66":
                            String type = getResources().getString(R.string.type) + " " + oneParfum.getValue();
                            brend_type.setText(type);
                            break;

                        case "67":
                            String for_ = getResources().getString(R.string.for_) + " " + oneParfum.getValue();
                            brend_for.setText(for_);
                            break;

                        case "68":
                            String year = getResources().getString(R.string.year) + " " + oneParfum.getValue();
                            brend_year.setText(year);
                            break;

                        case "69":
                            String input = oneParfum.getValue();
//                            Log.d(LOG_TAG, "stroka semeistvo => "+input);
                            String output = input.replace("||", ", ");
                            String semeistvo = getResources().getString(R.string.semeistvo) + " " + output;
                            brend_semeistvo.setText(semeistvo);
                            break;

                        case "70":
                            String first = oneParfum.getValue();
//                            Log.d(LOG_TAG, "stroka first => "+first);
                            String output2 = first.replace("||", ", ");

                            String first_note = getResources().getString(R.string.first_note) + " " + output2;
                            brend_first_note.setText(first_note);
                            break;
                        case "73":
                            String text = getResources().getString(R.string.country) + " " + oneParfum.getValue();
                            brend_contry.setText(text);
                            break;
                        case "74":
                            text_about_parfum = Html.fromHtml(oneParfum.getValue()).toString();
                            break;
                        case "76":
//                            Log.d(LOG_TAG, "string => " + oneParfum.getValue());

                            cenaParfums = getCenaParfum(oneParfum.getValue());
                            for (CenaParfum cena : cenaParfums) {

                                RadioButton button = new RadioButton(getContext());
                                button.setText(cena.getTitle());
                                button.setTextSize(12);
                                button.setMinLines(2);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    button.setBackground(getResources().getDrawable(R.drawable.mybuttonchoise));
                                } else {
                                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttonchoise));
                                }
                                button.setButtonDrawable(android.R.color.transparent);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                }
                                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
                                layoutParams.setMargins(10, 20, 10, 20);
                                button.setLayoutParams(layoutParams);
                                button.setId(cena.getmIGXId() - 1);

                                radioChoiseMl.addView(button);

                            }
                            if (radioChoiseMl.getChildCount() > 0) {
                                radioChoiseMl.check(radioChoiseMl.getChildAt(0).getId());
                                String str = getResources().getString(R.string.cenaYou) + " " + cenaParfums.get(0).getTitle();
                                cenaForMl.setText(str);
                                str = cenaParfums.get(0).getPrice() + " " + getResources().getString(R.string.cena);
                                cenaParfum.setText(str);


                                radioChoiseMl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                        Log.d(LOG_TAG, "checked id => " + checkedId);
//                                        Log.d(LOG_TAG, "count group => " + group.getChildCount());
//                                        Log.d(LOG_TAG, "count button => " + cenaParfums.size());

                                        for (int i = 0; i < radioChoiseMl.getChildCount(); i++) {
                                            if ((i) == checkedId) {
                                                String str = getResources().getString(R.string.cenaYou) + " " + cenaParfums.get(i).getTitle();
                                                cenaForMl.setText(str);
                                                str = cenaParfums.get(i).getPrice() + " " + getResources().getString(R.string.cena);
                                                cenaParfum.setText(str);
                                            }
                                        }
                                    }
                                });
                            }
                            break;

                    }


                }
                if (cenaParfums.size() == 0) {
                    String str = getResources().getString(R.string.no_parfum);
                    cenaParfum.setText(str);
                    Button button_this = new Button(getContext());
                    button_this.setEnabled(false);
                    button_this.setText(R.string.button_time);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    button_this.setAllCaps(false);
                    button_this.setLayoutParams(params);
                    button_this.setId(0);
                    radioChoiseMl.addView(button_this);
                }
                Log.d(LOG_TAG, "size text => " + text_about_parfum.length());
                if (text_about_parfum.length() == 0) {
                    text_about.setText(R.string.aboutParfum);
                    aboutParfum.setText(R.string.no_about);
                } else {
                    text_about.setText(R.string.aboutParfum);
                    aboutParfum.setText(text_about_parfum);
                }
            }
        });

        aboutParfum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutParfum.isExpanded()) {
                    aboutParfum.collapse();

                } else {
                    aboutParfum.expand();

                }
            }
        });


        getFullInfo.load(sql_query_info_parfum);
        return v;
    }

    private boolean checkTrash(String id) {
        ActiveAndroid.beginTransaction();
        List<by.lykashenko.demon.mirparfumanew.Table.Trash> trash = new Select().from(Trash.class).where("id_parfum =?", id).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        Log.d(LOG_TAG, "есть в корзине " + trash.size());
        if (trash.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean CheckParfumFavorites(String id) {

        ActiveAndroid.beginTransaction();
        List<Model> items = new Select().from(Favorites.class).where("id_parfum = ?", id).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        if (items.size() > 0) {
            return true;
        } else return false;

    }

    private ArrayList<CenaParfum> getCenaParfum(String value) {

        ArrayList<CenaParfum> output = new ArrayList<>();

        JSONArray json = null;
        try {
            json = new JSONArray(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            for (int i = 0; i < json.length(); i++) {
                CenaParfum cenaParfum = new CenaParfum();
                Integer id = json.getJSONObject(i).getInt("MIGX_id");
                cenaParfum.setmIGXId(id);
                String price = json.getJSONObject(i).getString("price");
                price = (price.equals("")) ? "0" : price;
                cenaParfum.setPrice(price);
                String oldprice = json.getJSONObject(i).getString("oldprice");
                oldprice = (oldprice.equals("") ? "0" : oldprice);
                cenaParfum.setOldprice(oldprice);
                String priceFor = json.getJSONObject(i).getString("title");
                cenaParfum.setTitle(priceFor);
                output.add(cenaParfum);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;

    }

    public void onDestroy() {
        super.onDestroy();
        radioChoiseMl.clearCheck();
        radioChoiseMl.destroyDrawingCache();
    }


    @Override
    public void onCountLoad(final String count) {
        count_otzuvu = count;
        String text;
        if (count.equals("0")) {
            text = getResources().getString(R.string.no_count);
        } else {
            text = getResources().getString(R.string.otzuvu) + " (" + count + ")";
        }
        otzuvu_parfum.setText(text);


    }
}
