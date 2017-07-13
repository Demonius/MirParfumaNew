package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.ForTable;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 13.07.17.
 */

public class DialogChoiseFor extends DialogFragment {

    public interface CloseChoiseFor {
        void onCloseChoiseFor(String for_sex);
    }

    private CloseChoiseFor closeChoiseFor;

    public void regesterCloseFor(CloseChoiseFor closeChoiseFor) {
        this.closeChoiseFor = closeChoiseFor;
    }

    private ListView list;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCancelable(false);
        View v = inflater.inflate(R.layout.dialog_choise_brendu, container, false);
        TextView title = (TextView) v.findViewById(R.id.spisok);
        title.setText(getResources().getString(R.string.choiseFor));

        list = (ListView) v.findViewById(R.id.brenduList);
        ActiveAndroid.beginTransaction();

        final List<ForTable> forTableTable = new Select().from(ForTable.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        final String[] forList = new String[forTableTable.size()];

        for (int i = 0; i < forTableTable.size(); i++) {
            forList[i] = forTableTable.get(i).value_for;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, forList);

        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < forTableTable.size(); i++) {
            ForTable aForTable = forTableTable.get(i);
            list.setItemChecked(i, aForTable.check_click);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.isItemChecked(position)){
                    list.setItemChecked(position, false);
                }else list.setItemChecked(position, true);
                ActiveAndroid.beginTransaction();
                ForTable forTableSaveCheck = new Select().from(ForTable.class).where("value like '"+ forTableTable.get(position).value_for+"'").executeSingle();
                forTableSaveCheck.check_click = list.isItemChecked(position);
                forTableSaveCheck.save();
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
            }
        });

//        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ActiveAndroid.beginTransaction();
//                ForTable forSaveCheck = new Select().from(ForTable.class).where("value like "+forTableTable.get(position).value_for).executeSingle();
//                Log.d(LOG_TAG,"count save => "+forSaveCheck.toString());
//                forSaveCheck.check_click = list.isItemChecked(position);
//                forSaveCheck.save();
//                ActiveAndroid.setTransactionSuccessful();
//                ActiveAndroid.endTransaction();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        ImageButton deselect = (ImageButton) v.findViewById(R.id.button_deselect_all);

        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < forTableTable.size(); i++) {
                    list.setItemChecked(i, false);
                }
            }
        });

        ImageButton select = (ImageButton) v.findViewById(R.id.button_select_all);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < forTableTable.size(); i++) {
                    list.setItemChecked(i, true);
                }
            }
        });

        final TextView closeButton = (TextView) v.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer countChecked = list.getCheckedItemCount();
                String count = "";
                ActiveAndroid.beginTransaction();
                List<ForTable> forTableSelected = new Select()
                        .from(ForTable.class)
                        .where("check_click = ?", true)
                        .execute();
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
                Log.d(LOG_TAG, " checked count => " + forTableSelected.size());

                if (forTableSelected.size() == 0) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_choise_for), Toast.LENGTH_LONG).show();
                } else {
                    if (forTableTable.size() == countChecked) {
                        count = getResources().getString(R.string.all);
                    } else {
                        String text = "";
                        for (int i = 0; i< forTableSelected.size(); i++) {

                            if (i==(forTableSelected.size()-1)){
                                text = text + forTableSelected.get(i).value_for;
                            }else
                            text = text + forTableSelected.get(i).value_for+", ";
                        }

//                        for (int i = 0; i < checkedItem.length; i++) {

//                            Log.d(LOG_TAG, " checked id count => " + checkedItem.length);
//                        }
//                        for (Long i:checkedItem
//                             ) {
//                            if (i==0){
//                                text = text+"женский";
//
//                            } else if (i==1){
//                                text = text+ ", мужской";
//                            } else if (i==2){
//                                text = text+ ", унисекс";
//                            }
//                        }

                        count = getResources().getString(R.string.choise) + " " + text;
                    }
                    closeChoiseFor.onCloseChoiseFor(count);
                    dismiss();
                }
            }
        });

        return v;
    }
}
