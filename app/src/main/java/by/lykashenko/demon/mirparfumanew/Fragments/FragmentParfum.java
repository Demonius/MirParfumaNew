package by.lykashenko.demon.mirparfumanew.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
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
    private LinearLayout aboutParfum;
    private ArrayList<CenaParfum> cenaParfums = new ArrayList<>();
    private TextView character_info;
    private String brend_name;
    private TextView text_about, otzuvu_parfum;
    private Integer state_favorites = 0;
    private String[] imageList;
    private CarouselView imageParfum;
    private String name;
    private String name_parfum;
    private TextView nameParfum;
    private Float rattingParfum;
    private String id, count_otzuvu;

    private Boolean stateExpand = false;

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
        brend_name = getArguments().getString("brend");
        rattingParfum = getArguments().getFloat("ratting");
        HashMap<String, String> info = (HashMap<String, String>) getArguments().getSerializable("info");


        titleToolbar.setText(name);

        imageParfum = (CarouselView) v.findViewById(R.id.carouselImageParfum);


//name parfum
        nameParfum = (TextView) v.findViewById(R.id.nameParfumCard);
        nameParfum.setText(name);
//объём флакона
        cenaForMl = (TextView) v.findViewById(R.id.cenaOtParfum);
        //цена за объём
        cenaParfum = (TextView) v.findViewById(R.id.cenaParfum);
//группа переключателей для определения объёма
        radioChoiseMl = (RadioGroup) v.findViewById(R.id.choise_ml);
        //описание парфюма
        character_info = (TextView) v.findViewById(R.id.text_about_parfum);
        //подробное описание
        aboutParfum = (LinearLayout) v.findViewById(R.id.parfumCharacter);


//        brend_character = v.findViewById(R.id.brend_character);
//        brend_aromat = v.findViewById(R.id.brend_aromat);
//        brend_type = v.findViewById(R.id.brend_type);
//        brend_for = v.findViewById(R.id.brend_for);
//        brend_year = v.findViewById(R.id.brend_year);
//        brend_semeistvo = v.findViewById(R.id.brend_semeistvo);
//        brend_first_note = v.findViewById(R.id.brend_first_nota);
//        brend_contry = v.findViewById(R.id.brend_contry);
//        text_about = v.findViewById(R.id.about_parfum);
        //установка характеристик парфюма
        loadParfumCharacters(info);
//отзывы
        otzuvu_parfum = (TextView) v.findViewById(R.id.otzuvu_parfum);
//рейтинг
        final RatingBar ratingParfum = (RatingBar) v.findViewById(R.id.ratting_parfum);
        ratingParfum.setNumStars(5);
        ratingParfum.isIndicator();
        ratingParfum.setRating((float) rattingParfum);
        //share
        Button buttonShare = (Button) v.findViewById(R.id.button_share);
        buttonShare.setEnabled(false);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Поделиться в соц. сетях", Toast.LENGTH_SHORT).show();
            }
        });

//добавлено в избранные или нет

        final Boolean check = CheckParfumFavorites(id);

        final RadioButton add_favorites = (RadioButton) v.findViewById(R.id.layout_add_favorites);
        add_favorites.setButtonDrawable(android.R.color.transparent);
        add_favorites.setChecked(check);


        add_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ActiveAndroid.beginTransaction();

                if (isChecked){
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
                }else{
                    new Delete().from(Favorites.class).where("id_parfum = ?", id).execute();
                }

                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();

                Intent serviceStartedIntent = new Intent(message_add_favorites);
                serviceStartedIntent.putExtra("update", 2);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(serviceStartedIntent);
            }
        });

        final LinearLayout layoutAddTrash = (LinearLayout) v.findViewById(R.id.layout_add_trash);

        final TextView text_trash = (TextView) v.findViewById(R.id.text_trash);
        if (checkTrash(id)) {
            Log.d(LOG_TAG, "true");
//            layoutAddTrash.setBackgroundResource(R.color.colorFonPrimary);
            text_trash.setText(getResources().getString(R.string.added_to_trash));
        } else {
            Log.d(LOG_TAG, "false");
            text_trash.setText(getResources().getString(R.string.add_to_trash));
        }
        layoutAddTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkTrash(id)) {
                    Log.d(LOG_TAG, "click false");
//                    layoutAddTrash.setBackgroundColor(getResources().getColor(R.color.colorFonPrimary));
                    text_trash.setText(getResources().getString(R.string.add_to_trash));

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
                    ActiveAndroid.beginTransaction();
                    Trash trash = new Trash();
                    trash.id_parfum = id;
                    trash.name_parfum = name;
                    trash.image_parfum = imageList[0];
                    trash.ratting_parfum = Float.toString(rattingParfum);
                    trash.cena_for = cenaFor;
                    trash.cena_parfum = cena_parfum;
                    trash.count_parfum = 1;
                    trash.save();
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                    Intent serviceStartedIntent = new Intent(message_add_favorites);
                    serviceStartedIntent.putExtra("update", 3);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(serviceStartedIntent);
                }

            }
        });

//        String sql_query_info_parfum = "SELECT tmplvarid,value FROM modx_site_tmplvar_contentvalues where contentid=" +
//                id + " and tmplvarid IN (1,11,65,66,67,68,69,70,73,74,76)";
//
//        GetFullInfoOfParfum getFullInfo = new GetFullInfoOfParfum(getContext());


//        getFullInfo.regestrationLoadInfo(new GetFullInfoOfParfum.LoadInfo() {
//            @Override
//            public void onLoadInfo(ArrayList<Parfum> parfum) {
//                String text_about_parfum = "";
//                for (Parfum oneParfum : parfum) {
//
//                    switch (oneParfum.getTmplvarid()) {
//                        case "1":
//                            String image = MainActivity.URL + oneParfum.getValue();
//                            imageList = new String[]{image};
//                            imageParfum.setPageCount(imageList.length);
//                            break;
//                        case "11":
//                            String brend = oneParfum.getValue();
//                            brend_character.setText(getResources().getString(R.string.brend) + " " + brend);
//                            break;
//
//                        case "65":
//                            String aromat = getResources().getString(R.string.aromat) + " " + oneParfum.getValue();
//                            brend_aromat.setText(aromat);
//                            break;
//
//                        case "66":
//                            String type = getResources().getString(R.string.type) + " " + oneParfum.getValue();
//                            brend_type.setText(type);
//                            break;
//
//                        case "67":
//                            String for_ = getResources().getString(R.string.for_) + " " + oneParfum.getValue();
//                            brend_for.setText(for_);
//                            break;
//
//                        case "68":
//                            String year = getResources().getString(R.string.year) + " " + oneParfum.getValue();
//                            brend_year.setText(year);
//                            break;
//
//                        case "69":
//                            String input = oneParfum.getValue();
////                            Log.d(LOG_TAG, "stroka semeistvo => "+input);
//                            String output = input.replace("||", ", ");
//                            String semeistvo = getResources().getString(R.string.semeistvo) + " " + output;
//                            brend_semeistvo.setText(semeistvo);
//                            break;
//
//                        case "70":
//                            String first = oneParfum.getValue();
////                            Log.d(LOG_TAG, "stroka first => "+first);
//                            String output2 = first.replace("||", ", ");
//
//                            String first_note = getResources().getString(R.string.first_note) + " " + output2;
//                            brend_first_note.setText(first_note);
//                            break;
//                        case "73":
//                            String text = getResources().getString(R.string.country) + " " + oneParfum.getValue();
//                            brend_contry.setText(text);
//                            break;
//                        case "74":
//                            text_about_parfum = Html.fromHtml(oneParfum.getValue()).toString();
//                            break;
//                        case "76":
////                            Log.d(LOG_TAG, "string => " + oneParfum.getValue());
//
//                            cenaParfums = getCenaParfum(oneParfum.getValue());
//                            for (CenaParfum cena : cenaParfums) {
//
//                                RadioButton button = new RadioButton(getContext());
//                                button.setText(cena.getTitle());
//                                button.setTextSize(12);
//                                button.setMinLines(2);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    button.setBackground(getResources().getDrawable(R.drawable.mybuttonchoise));
//                                } else {
//                                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttonchoise));
//                                }
//                                button.setButtonDrawable(android.R.color.transparent);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                    button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                                }
//                                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
//                                layoutParams.setMargins(10, 20, 10, 20);
//                                button.setLayoutParams(layoutParams);
//                                button.setId(cena.getmIGXId() - 1);
//
//                                radioChoiseMl.addView(button);
//
//                            }
//                            if (radioChoiseMl.getChildCount() > 0) {
//                                radioChoiseMl.check(radioChoiseMl.getChildAt(0).getId());
//                                String str = getResources().getString(R.string.cenaYou) + " " + cenaParfums.get(0).getTitle();
//                                cenaForMl.setText(str);
//                                str = cenaParfums.get(0).getPrice() + " " + getResources().getString(R.string.cena);
//                                cenaParfum.setText(str);
//
//
//                                radioChoiseMl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                    @Override
//                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
////                                        Log.d(LOG_TAG, "checked id => " + checkedId);
////                                        Log.d(LOG_TAG, "count group => " + group.getChildCount());
////                                        Log.d(LOG_TAG, "count button => " + cenaParfums.size());
//
//                                        for (int i = 0; i < radioChoiseMl.getChildCount(); i++) {
//                                            if ((i) == checkedId) {
//                                                String str = getResources().getString(R.string.cenaYou) + " " + cenaParfums.get(i).getTitle();
//                                                cenaForMl.setText(str);
//                                                str = cenaParfums.get(i).getPrice() + " " + getResources().getString(R.string.cena);
//                                                cenaParfum.setText(str);
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                            break;
//
//                    }
//
//
//                }
//                if (cenaParfums.size() == 0) {
//                    String str = getResources().getString(R.string.no_parfum);
//                    cenaParfum.setText(str);
//                    Button button_this = new Button(getContext());
//                    button_this.setEnabled(false);
//                    button_this.setText(R.string.button_time);
//                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    button_this.setAllCaps(false);
//                    button_this.setLayoutParams(params);
//                    button_this.setId(0);
//                    radioChoiseMl.addView(button_this);
//                }
//                Log.d(LOG_TAG, "size text => " + text_about_parfum.length());
//                if (text_about_parfum.length() == 0) {
//                    text_about.setText(R.string.aboutParfum);
//                    aboutParfum.setText(R.string.no_about);
//                } else {
//                    text_about.setText(R.string.aboutParfum);
//                    aboutParfum.setText(text_about_parfum);
//                }
//            }
//        });
//
//        aboutParfum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (aboutParfum.isExpanded()) {
//                    aboutParfum.collapse();
//
//                } else {
//                    aboutParfum.expand();
//
//                }
//            }
//        });


//        getFullInfo.load(sql_query_info_parfum);
        return v;
    }

    private void loadParfumCharacters(final HashMap<String, String> info) {

        String imageUrl = MainActivity.URL + info.get("1");
        Log.d(LOG_TAG, "url image ->>> " + imageUrl);
        imageList = new String[]{imageUrl};
        imageParfum.setPageCount(imageList.length);
        imageParfum.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View view = getLayoutInflater().inflate(R.layout.corusel_view, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_courusel);
                Picasso.with(getContext()).load(imageList[position]).into(imageView);
                return view;
            }
        });

        cenaParfums = getCenaParfum(info.get("76"));

        if (cenaParfums.size() > 0) {

            for (CenaParfum cena : cenaParfums) {

                RadioButton button = new RadioButton(getContext());
                button.setText(cena.getTitle());
                button.setTextSize(10);
                button.setMinLines(2);
                button.setMaxLines(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    button.setBackground(getResources().getDrawable(R.drawable.mybuttonchoise));
                } else {
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttonchoise));
                }
                button.setButtonDrawable(android.R.color.transparent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.width_radiobutton), getResources().getDimensionPixelSize(R.dimen.height_radiobutton));
                layoutParams.setMargins(0, 0, 8, 0);
                button.setLayoutParams(layoutParams);
                button.setId(cena.getmIGXId() - 1);

                radioChoiseMl.addView(button);
            }
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

        } else {
            cenaForMl.setHeight(0);
            cenaParfum.setText(R.string.no_parfum);
        }

        final String textFullAbout = info.get("74");
        if (textFullAbout != null) {
            final String textPreview = textFullAbout.substring(0, 70) + " ... </br> <font color=\"red\">ещё</font>";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                character_info.setText(Html.fromHtml(textPreview, Html.FROM_HTML_MODE_LEGACY));
            } else {
                character_info.setText(Html.fromHtml(textPreview));
            }

            character_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stateExpand) {
                        stateExpand = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            character_info.setText(Html.fromHtml(textPreview, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            character_info.setText(Html.fromHtml(textPreview));
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        aboutParfum.setLayoutParams(params);
                        aboutParfum.removeAllViews();
                    } else {
                        stateExpand = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            character_info.setText(Html.fromHtml(textFullAbout, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            character_info.setText(Html.fromHtml(textFullAbout));
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        aboutParfum.setLayoutParams(params);
                        aboutParfum.setPadding(12, 16, 12, 16);

                        TextView characterText = new TextView(getContext());
                        characterText.setText(R.string.character);
                        characterText.setTextColor(getResources().getColor(android.R.color.black));
                        characterText.setTextSize(14);
                        characterText.setTypeface(null, Typeface.BOLD);
                        aboutParfum.addView(characterText);

                        if (brend_name != null) {
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textBrend = getResources().getString(R.string.brend) + " " + brend_name;
                            text.setText(textBrend);
                            aboutParfum.addView(character);
                        }

                        if (info.get("65") != null) {
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textAromat = getResources().getString(R.string.aromat) + " " + info.get("65");
                            text.setText(textAromat);
                            aboutParfum.addView(character);
                        }

                        if (info.get("66") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textType = getResources().getString(R.string.type) + " " + info.get("66");
                            text.setText(textType);
                            aboutParfum.addView(character);
                        }


                        if (info.get("67") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textFor = getResources().getString(R.string.for_) + " " + info.get("67");
                            text.setText(textFor);
                            aboutParfum.addView(character);
                        }
                        if (info.get("68") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textYear = getResources().getString(R.string.year) + " " + info.get("68");
                            text.setText(textYear);
                            aboutParfum.addView(character);
                        }
                        if (info.get("69") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textSemeistvo = getResources().getString(R.string.semeistvo) + " " + info.get("69").replace("||", ", ");
                            text.setText(textSemeistvo);
                            aboutParfum.addView(character);
                        }
                        if (info.get("70") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textNota = getResources().getString(R.string.first_note) + " " + info.get("70").replace("||", ", ");
                            text.setText(textNota);
                            aboutParfum.addView(character);
                        }

                        if (info.get("73") !=null){
                            View character = LayoutInflater.from(getContext()).inflate(R.layout.viewstub_layout_character, null);
                            TextView text = character.findViewById(R.id.text_info_parfum);
                            String textCountry = getResources().getString(R.string.country) + " " + info.get("73");
                            text.setText(textCountry);
                            aboutParfum.addView(character);
                        }

                    }
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            aboutParfum.setLayoutParams(params);
        } else {
            character_info.setText(R.string.no_info_about_parfum);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            aboutParfum.setLayoutParams(params);
        }


//        String sql = "SELECT count(1) FROM modx_tickets_comments where thread = " + id + " ORDER BY createdon DESC";
//        CountOtzuvuParfum countOtzuvuParfum = new CountOtzuvuParfum();
//        countOtzuvuParfum.registerCountLoad(this);
//        countOtzuvuParfum.load(sql);
//        LinearLayout otzuvu_click = v.findViewById(R.id.otzuv_layout);
//        otzuvu_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadListOtzuvu.onLoadListOtzuvu(id,count_otzuvu);
//                Log.d(LOG_TAG, "id parfum => " + id);
//            }
//        });

    }

    private boolean checkTrash(String id) {
        ActiveAndroid.beginTransaction();
        List<by.lykashenko.demon.mirparfumanew.Table.Trash> trash = new Select().from(Trash.class).where("id_parfum =?", id).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        Log.d(LOG_TAG, "есть в корзине " + trash.size());
        return trash.size() > 0;
    }

    private Boolean CheckParfumFavorites(String id) {

        ActiveAndroid.beginTransaction();
        List<Model> items = new Select().from(Favorites.class).where("id_parfum = ?", id).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return items.size() > 0;

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
