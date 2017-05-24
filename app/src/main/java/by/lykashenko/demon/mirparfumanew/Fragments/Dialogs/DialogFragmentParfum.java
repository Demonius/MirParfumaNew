package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 17.05.2017.
 */

public class DialogFragmentParfum extends DialogFragment {
    private String id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_NoActionBar);
    }

        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState){

            Dialog dialog = super.onCreateDialog(savedInstanceState);


            return dialog;
        }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_parfum_for_brend, container, false);

        Toolbar toolbarAbout = (Toolbar) v.findViewById(R.id.toolbarAbout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarAbout);
        id = getArguments().getString("id");
        final String SQL_PARFUM_FROM_BREND = "SELECT val.contentid FROM modx_site_content as con, modx_site_tmplvar_contentvalues as val where con.id="+id
                +"and con.pagetitle LIKE val.value order by contentid ASC";
        String nameBrendList = getArguments().getString("name");

        ImageView image_back = (ImageView) v.findViewById(R.id.image_back_parfumlist);
        TextView nameBrend = (TextView) v.findViewById(R.id.nameBrend);

        nameBrend.setText(nameBrendList);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;


    }


}
