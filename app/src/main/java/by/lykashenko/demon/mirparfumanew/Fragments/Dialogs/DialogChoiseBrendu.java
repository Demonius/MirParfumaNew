package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 20.06.17.
 */

public class DialogChoiseBrendu extends DialogFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_NoActionBar);
    }

    public interface CloseChoiseBrendu {
        void onCloseChoiseBrendu(String count);
    }

    private CloseChoiseBrendu closeChoiseBrendu;

    public void register(CloseChoiseBrendu closeChoiseBrendu) {
        this.closeChoiseBrendu = closeChoiseBrendu;
    }

    private List<BrenduCount> listData;
    private ListView listBrendu;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCancelable(false);
 //       getDialog().setTitle(getResources().getString(R.string.choiseBrendu));
        
        View v = inflater.inflate(R.layout.dialog_choise_brendu, container, false);


        TextView title = (TextView) v.findViewById(R.id.spisok);
        title.setText(getResources().getString(R.string.choiseBrendu));
        
        listBrendu = (ListView) v.findViewById(R.id.brenduList);

        ActiveAndroid.beginTransaction();

        listData = new Select().from(BrenduCount.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
//        BrenduListAdapter adapter = new BrenduListAdapter(listData,getContext());

        String[] names = new String[listData.size()];

        for (int i = 0; i < listData.size(); i++) {
            BrenduCount brendOne = listData.get(i);

            names[i] = brendOne.name + " (" + Integer.toString(brendOne.count) + ")";
        }

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, names);

        listBrendu.setAdapter(adapter);
        listBrendu.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < listData.size(); i++) {
            BrenduCount brendOne = listData.get(i);
            listBrendu.setItemChecked(i,brendOne.check);
        }
        listBrendu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "нажатие на item => " + position);

                ActiveAndroid.beginTransaction();
                BrenduCount brendOne = new Select().from(BrenduCount.class).where("id_brend = ?", listData.get(position).id_brend).executeSingle();
                brendOne.check = listBrendu.isItemChecked(position);
                brendOne.save();
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView closeButton = (TextView) v.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer countChecked = listBrendu.getCheckedItemCount();

                String count ="";
                if (countChecked==0){
                    Toast.makeText(getContext(),getResources().getString(R.string.no_choise_brend), Toast.LENGTH_LONG).show();
                }else {
                    if (listData.size() == countChecked) {
                        count = getResources().getString(R.string.all);
                    } else {
                        count = getResources().getString(R.string.choise) + " " + Integer.toString(countChecked) + " " + getResources().getString(R.string.brends);
                    }
                    closeChoiseBrendu.onCloseChoiseBrendu(count);
                    dismiss();
                }
            }
        });

        ImageButton deselect = (ImageButton) v.findViewById(R.id.button_deselect_all);

        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < listData.size(); i++) {
                    listBrendu.setItemChecked(i,false);
                }
            }
        });

        ImageButton select = (ImageButton) v.findViewById(R.id.button_select_all);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listData.size(); i++) {
                    listBrendu.setItemChecked(i,true);
                }
            }
        });

        return v;
    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        ActiveAndroid.beginTransaction();
//        List<BrenduCount> brendu = new Select().from(BrenduCount.class).where("checkedPodbor = ?",true).execute();
//        ActiveAndroid.setTransactionSuccessful();
//        ActiveAndroid.endTransaction();
//        Log.e(LOG_TAG,"size checked brend => "+brendu.size());
//        Log.e(LOG_TAG,"size  brend => "+listData.size());
//
//        String count;
//        if (listData.size()==brendu.size()){
//            count = getResources().getString(R.string.all);
//        }else{
//            count =getResources().getString(R.string.choise)+" "+Integer.toString(brendu.size())+" "+getResources().getString(R.string.brends);
//        }
//        closeChoiseBrendu.onCloseChoiseBrendu(count);
//    }



}
