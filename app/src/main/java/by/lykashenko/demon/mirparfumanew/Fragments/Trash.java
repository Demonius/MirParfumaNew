package by.lykashenko.demon.mirparfumanew.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 08.02.2017.
 */

public class Trash extends Fragment {


    public interface GoToCatalog {
        void onGoToCatalog(Integer state);
    }

    private GoToCatalog goToCatalog;

    public void registerGoToCatalog(GoToCatalog goToCatalog) {
        this.goToCatalog = goToCatalog;
    }

    private View vFragment;
    private List<by.lykashenko.demon.mirparfumanew.Table.Trash> trashList;
    private TextView text_symma;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//получение списка корзины
        ActiveAndroid.beginTransaction();
        trashList = new Select().from(by.lykashenko.demon.mirparfumanew.Table.Trash.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();


        if (!trashList.isEmpty()) {//проверка на надичие данных
            //есть записи
            vFragment = inflater.inflate(R.layout.fragment_trash, null);

            RecyclerView list_trash = (RecyclerView) vFragment.findViewById(R.id.rw_trash_list);
            text_symma = (TextView) vFragment.findViewById(R.id.symmaParfum);

            list_trash.setHasFixedSize(true);
            LinearLayoutManager lm = new LinearLayoutManager(getContext());
            list_trash.setLayoutManager(lm);
            AdapterTrash adapter = new AdapterTrash(trashList);
            list_trash.setAdapter(adapter);
            update(trashList,0);
            final RadioButton radioSam = (RadioButton) vFragment.findViewById(R.id.samZabery);
            final RadioButton radioCyrer = (RadioButton) vFragment.findViewById(R.id.dostavkapoMinsk);
            LinearLayout layoutDostavka = (LinearLayout) vFragment.findViewById(R.id.layoutDostavka);
            LinearLayout layoutSam = (LinearLayout) vFragment.findViewById(R.id.layoutSam);

            layoutDostavka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!radioCyrer.isChecked()){
                        radioCyrer.setChecked(true);
                        radioSam.setChecked(false);
                    }
                }
            });

            layoutSam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!radioSam.isChecked()){
                        radioCyrer.setChecked(false);
                        radioSam.setChecked(true);
                    }
                }
            });


            radioSam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        radioCyrer.setChecked(false);
                    }
                }
            });
            radioCyrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        radioSam.setChecked(false);
                    }
                }
            });

            Button addZakaz = (Button) vFragment.findViewById(R.id.pred_zakaz);
            addZakaz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Добавление заказа", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
//нет данных
            vFragment = inflater.inflate(R.layout.fragment_favorite_empty, null);

            ImageView imageViewTrash = (ImageView) vFragment.findViewById(R.id.imageEmpty);
            imageViewTrash.setImageResource(R.drawable.empty_korzina);

            TextView textViewTop = (TextView) vFragment.findViewById(R.id.textViewTopEmpty);
            textViewTop.setText(R.string.empty_trash);

            TextView textViewBotom = (TextView) vFragment.findViewById(R.id.textViewBotomEmpty);
            textViewBotom.setText(R.string.text_empty_trash);

            Button btnGo = (Button) vFragment.findViewById(R.id.btnEmpty);
            btnGo.setText(R.string.button_favorite);

            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCatalog.onGoToCatalog(1);
                }
            });
        }
        return vFragment;
    }

    private class AdapterTrash extends RecyclerView.Adapter<AdapterTrash.TrashViewHolder> {

        private List<by.lykashenko.demon.mirparfumanew.Table.Trash> trashList1;
        private String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        public AdapterTrash(List<by.lykashenko.demon.mirparfumanew.Table.Trash> trashList1) {
            this.trashList1 = trashList1;
        }

        @Override
        public TrashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_trash, parent, false);
            return new TrashViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TrashViewHolder holder, final int position) {
            holder.nameTrash.setText(trashList1.get(position).name_parfum);
            holder.ratingTrash.setRating(Float.parseFloat(trashList1.get(position).ratting_parfum));
            String text = "Цена за " + trashList1.get(position).cena_for;
            holder.cenaForTrash.setText(text);
            text = trashList1.get(position).cena_parfum + " р.";
            holder.cenaTrash.setText(text);
            String url_image_brend = trashList1.get(position).image_parfum;
            Picasso.with(getContext()).load(url_image_brend).into(holder.imageTrash);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.colvoTrash.setAdapter(adapter);
            final Integer pos = position;
            holder.colvoTrash.setSelection(0);
            holder.colvoTrash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    UpdateSymma(position + 1, pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Удалено из корзины", Toast.LENGTH_SHORT).show();
                    ActiveAndroid.beginTransaction();
                    new Delete().from(by.lykashenko.demon.mirparfumanew.Table.Trash.class)
                            .where("id_parfum = " + trashList1.get(pos).id_parfum).execute();
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();
                    goToCatalog.onGoToCatalog(2);
                }
            });
        }

        @Override
        public int getItemCount() {
            return trashList1.size();
        }

        public class TrashViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageTrash;
            private TextView nameTrash;
            private RatingBar ratingTrash;
            private TextView cenaForTrash;
            private TextView cenaTrash;
            private Spinner colvoTrash;
            private ImageButton deleteItem;


            public TrashViewHolder(View itemView) {
                super(itemView);
                imageTrash = (ImageView) itemView.findViewById(R.id.imageParfumTrash);
                nameTrash = (TextView) itemView.findViewById(R.id.nameParfumTrash);
                ratingTrash = (RatingBar) itemView.findViewById(R.id.ratingBarTrash);
                cenaForTrash = (TextView) itemView.findViewById(R.id.cenaForTrash);
                cenaTrash = (TextView) itemView.findViewById(R.id.priceParfumMinTrash);
                colvoTrash = (Spinner) itemView.findViewById(R.id.spinnerColTrash);
                deleteItem = (ImageButton) itemView.findViewById(R.id.deleteTrashItem);
                ratingTrash.setIsIndicator(false);
                ratingTrash.setNumStars(5);


            }
        }
    }

    private void UpdateSymma(int colvo, Integer position) {

        ActiveAndroid.beginTransaction();
        by.lykashenko.demon.mirparfumanew.Table.Trash trashOne = new Select().
                from(by.lykashenko.demon.mirparfumanew.Table.Trash.class).
                where("id_parfum = ?", trashList.get(position).id_parfum).executeSingle();
        trashOne.count_parfum = colvo;
        trashOne.save();
        trashList.get(position).count_parfum = colvo;

        update(trashList,0);

    }

    private void update(List<by.lykashenko.demon.mirparfumanew.Table.Trash> trashList,Integer plus) {
//        text_symma
        Float symma = (float) 0;
        for (by.lykashenko.demon.mirparfumanew.Table.Trash trash : trashList
                ) {
            Float cena = Float.parseFloat(trash.cena_parfum);
            Integer colvo = trash.count_parfum;
            symma = symma + cena * colvo + plus;
        }
        String cena = getResources().getString(R.string.vsego) + ": " + Float.toString(symma) + " " + getResources().getString(R.string.cena);
        text_symma.setText(cena);
    }
}
