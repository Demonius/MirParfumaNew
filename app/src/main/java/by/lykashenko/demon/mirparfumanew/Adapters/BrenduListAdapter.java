package by.lykashenko.demon.mirparfumanew.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;

/**
 * Created by Admin on 20.06.17.
 */

public class BrenduListAdapter extends BaseAdapter {

    List<BrenduCount> listBrendu;
    Context context;
    LayoutInflater lInflater;

    public BrenduListAdapter(List<BrenduCount> listBrendu, Context context) {
        this.listBrendu = listBrendu;
        this.context = context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listBrendu.size();
    }

    @Override
    public BrenduCount getItem(int position) {
        return listBrendu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item, parent, false);
        }

        BrenduCount brend = getItem(position);

        String text = brend.name + " (" + brend.count + ")";
        ((TextView) convertView.findViewById(R.id.textItem)).setText(text);

        final CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBrend);
        check.setChecked(brend.check);

//        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                ActiveAndroid.beginTransaction();
//                BrenduCount brendOne = new Select().from(BrenduCount.class).where("id_brend = ?", listBrendu.get(position).id_brend).executeSingle();
//                brendOne.check = isChecked;
//                brendOne.save();
//                ActiveAndroid.setTransactionSuccessful();
//                ActiveAndroid.endTransaction();
//
//                listBrendu.set(position, new BrenduCount(listBrendu.get(position).id_brend, listBrendu.get(position).name, listBrendu.get(position).count, isChecked));
//            }
//        });


        return convertView;
    }
}
