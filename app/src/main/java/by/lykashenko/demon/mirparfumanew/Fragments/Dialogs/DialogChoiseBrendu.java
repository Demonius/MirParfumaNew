package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 *
 * Created by Admin on 20.06.17.
 */

public class DialogChoiseBrendu extends DialogFragment {

    public interface CloseBrendu {
        void onCloseBrendu(String count);
    }

    private CloseBrendu closeBrendu;

    public void registerBrendu(CloseBrendu closeBrendu) {
        this.closeBrendu = closeBrendu;
    }

    private ListView list;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        try {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "error => " + e.getMessage());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCancelable(false);
        View v = inflater.inflate(R.layout.dialog_choise_brendu, container, false);
        TextView title = (TextView) v.findViewById(R.id.spisok);
        title.setText(getResources().getString(R.string.choiseCountryTitle));

        list = (ListView) v.findViewById(R.id.brenduList);
        ActiveAndroid.beginTransaction();

        final List<BrenduCount> listData = new Select().from(BrenduCount.class).execute();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        final String[] arrayData = new String[listData.size()];

        for (int i = 0; i < listData.size(); i++) {
            arrayData[i] = listData.get(i).name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, arrayData);

        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < listData.size(); i++) {
            list.setItemChecked(i, listData.get(i).check);
        }

        ImageButton deselect = (ImageButton) v.findViewById(R.id.button_deselect_all);
        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < listData.size(); i++) {
                    list.setItemChecked(i, false);
                }
            }
        });

        ImageButton select = (ImageButton) v.findViewById(R.id.button_select_all);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listData.size(); i++) {
                    list.setItemChecked(i, true);
                }
            }
        });

        final TextView closeButton = (TextView) v.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer countChecked = list.getCheckedItemCount();

                for (int i = 0; i < listData.size(); i++) {
//					Log.i(LOG_TAG, "position " + i + " checked =>" + list.getCheckedItemPositions().get(i));
                    if (list.getCheckedItemPositions().get(i)) {
                        ActiveAndroid.beginTransaction();
                        BrenduCount oneElement = new Select()
                                .from(BrenduCount.class)
                                .where("name_brend like ?", listData.get(i).name)
                                .executeSingle();
                        oneElement.check = true;
                        oneElement.save();
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
                    }
                }

                String count = "";
                if (countChecked == 0) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_choise_for), Toast.LENGTH_LONG).show();
                } else {
                    if (listData.size() == countChecked) {
                        count = getResources().getString(R.string.all);
                    } else {
                        count = getResources().getString(R.string.choise) + " " + Integer.toString(countChecked) + " " + getResources().getString(R.string.elements);
                    }
                }
                closeBrendu.onCloseBrendu(count);
                dismiss();
            }
        });
        return v;
    }
}
