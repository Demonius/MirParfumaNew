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
import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;
import by.lykashenko.demon.mirparfumanew.Table.Type;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 28.06.17.
 */

public class DialogChoiseType extends DialogFragment {

    private ListView listBrendu;

    public interface CloseChoiseType {
        void onCloseChoiseType(String count);
    }

    private CloseChoiseType closeChoiseType;

    public void register(CloseChoiseType closeChoiseType) {
        this.closeChoiseType = closeChoiseType;
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCancelable(false);
  //      getDialog().setTitle(getResources().getString(R.string.choiseType));
        View v = inflater.inflate(R.layout.dialog_choise_brendu, container, false);

        TextView title = (TextView) v.findViewById(R.id.spisok);
        title.setText(getResources().getString(R.string.choiseType));

        listBrendu = (ListView) v.findViewById(R.id.brenduList);

        ActiveAndroid.beginTransaction();

        final List<Type> listType = new Select().from(Type.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        String[] types = new String[listType.size()];

        for (int i = 0; i < listType.size(); i++) {
            types[i]=listType.get(i).value;
        }

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, types);

        listBrendu.setAdapter(adapter);
        listBrendu.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < listType.size(); i++) {
            Type type = listType.get(i);
            listBrendu.setItemChecked(i,type.check_click);
        }

        listBrendu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(LOG_TAG, "нажатие на item => " + position);

                ActiveAndroid.beginTransaction();
                Type typeOne = new Select().from(Type.class).where("value like ?", listType.get(position).value).executeSingle();
                typeOne.check_click = listBrendu.isItemChecked(position);
                typeOne.save();
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
                    if (listType.size() == countChecked) {
                        count = getResources().getString(R.string.all);
                    } else {
                        count = getResources().getString(R.string.choise) + " " + Integer.toString(countChecked) + " " + getResources().getString(R.string.types);
                    }
                    closeChoiseType.onCloseChoiseType(count);
                    dismiss();
                }
            }
        });

        ImageButton deselect = (ImageButton) v.findViewById(R.id.button_deselect_all);

        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < listType.size(); i++) {
                    listBrendu.setItemChecked(i,false);
                }
            }
        });

        ImageButton select = (ImageButton) v.findViewById(R.id.button_select_all);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listType.size(); i++) {
                    listBrendu.setItemChecked(i,true);
                }
            }
        });

        return v;
    }
}
