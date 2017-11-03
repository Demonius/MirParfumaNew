package by.lykashenko.demon.mirparfumanew.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.Adapters.Child;
import by.lykashenko.demon.mirparfumanew.Adapters.SectionHeader;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.BrenduAll;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 05.07.17.
 */

public class Search extends Fragment {

    public interface LoadBrendInfo {
        void onLoadBrendInfo(Bundle bundle);
    }

    private LoadBrendInfo loadBrendInfo;

    public void registerLoadBrendInfo(LoadBrendInfo loadBrendInfo) {
        this.loadBrendInfo = loadBrendInfo;
    }

    private IndexFastScrollRecyclerView brendSearch;
    private EditText searchNameBrendu;
    private ArrayList<Integer> mSectionPositions;
    private View vFragment;
    private AdapterSectionBrendu mAdapter;
    //    private List<BrenduAll> brenduAlls;
    private List<BrenduCount> brenduAlls;
    private List<SectionHeader> listBrenduSection;
    private List<SectionHeader> list;
    private Integer state_brendu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vFragment = inflater.inflate(R.layout.fragment_search, null);

        searchNameBrendu = (EditText) vFragment.findViewById(R.id.brendsearch);
        final ImageView clearSearch = (ImageView) vFragment.findViewById(R.id.imageViewClear);
        clearSearch.setVisibility(View.INVISIBLE);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNameBrendu.setText("");
            }
        });
        searchNameBrendu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ((s.length() > before) && (s.length() > 1)) {
                    ChangeAdapter(list, s.toString());
                } else {
                    ChangeAdapter(listBrenduSection, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clearSearch.setVisibility(View.INVISIBLE);
                } else {
                    clearSearch.setVisibility(View.VISIBLE);

                }
            }
        });


        state_brendu = getArguments().getInt(MainActivity.CLASS_BRENDU);
        Log.d(LOG_TAG, "sex=> " + state_brendu);

        ActiveAndroid.beginTransaction();
        ActiveAndroid.setLoggingEnabled(true);
        String sql = "";
        List<BrenduAll> sexBrenduList = new ArrayList<>();
        switch (state_brendu) {
            case 0:
                sql = "Select _id,brend_id as id_brend,brend_name as name_brend,count(parfum_name) as count, check_podbor  as checkedPodbor from brenduall group by brend_id order by brend_name ASC";
                break;
            case 1:
                sql = "Select _id,brend_id as id_brend,brend_name as name_brend,count(parfum_name) as count, check_podbor  as checkedPodbor from brenduall where sex like \'мужчин\' group by brend_id order by brend_name ASC";
                break;
            case 2:
                sql = "Select _id,brend_id as id_brend,brend_name as name_brend,count(parfum_name) as count, check_podbor  as checkedPodbor from brenduall where sex like \'женщин\' group by brend_id order by brend_name ASC";
                break;
            case 3:
                sql = "Select _id,brend_id as id_brend,brend_name as name_brend,count(parfum_name) as count, check_podbor  as checkedPodbor from brenduall where sex like \'унисекс\' group by brend_id order by brend_name ASC";
                break;
        }

        List<BrenduCount> listCount = SQLiteUtils.rawQuery(BrenduCount.class, sql, null);

        Log.d(LOG_TAG, "brendu size =>>>>> " + listCount.size());
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        listBrenduSection = LoadListBrendWithSection(listCount);

        brendSearch = (IndexFastScrollRecyclerView) vFragment.findViewById(R.id.brendSearch);
        brendSearch.setIndexTextSize(10);
        brendSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AdapterSectionBrendu(getContext(), listBrenduSection);
        brendSearch.setAdapter(mAdapter);

        return vFragment;
    }

    private void ChangeAdapter(List<SectionHeader> listSectionNow, String s) {

        if (!s.equals("")) {
            list = new ArrayList<>();
            for (SectionHeader section : listSectionNow
                    ) {
                List<Child> newChild = new ArrayList<>();
                for (Child child : section.getChildItems()
                        ) {

                    String symbol = child.getName().toLowerCase().substring(0, s.length());
//                Log.d(LOG_TAG,"symbol=> "+symbol);
                    if (symbol.equals(s.toLowerCase())) {
//                        Log.d(LOG_TAG, "search => " + child.getName());
                        newChild.add(child);
                    }
                }
                if (newChild.size() > 0) {
                    list.add(new SectionHeader(newChild, section.getSectinText()));
                }

            }
//            Log.d(LOG_TAG, "size new list => " + list.size());
            if (list.size() != 0) {
                mAdapter = new AdapterSectionBrendu(getContext(), list);
                brendSearch.setAdapter(mAdapter);
                if ((list.size() == 1) && (list.get(0).getChildItems().size() == 1)) {
                    List<BrenduAll> listParfumNew = new ArrayList<>();
                    switch (state_brendu) {
                        case 0:
                            listParfumNew = new Select().from(BrenduAll.class).where("brend_id = ?", list.get(0).getChildItems().get(0).getId()).execute();
                            break;
                        case 1:
                            listParfumNew = new Select().from(BrenduAll.class).where("brend_id = ?", list.get(0).getChildItems().get(0).getId()).where("sex like ?", "мужской").execute();
                            break;
                        case 2:
                            listParfumNew = new Select().from(BrenduAll.class).where("brend_id = ?", list.get(0).getChildItems().get(0).getId()).where("sex like ?", "женский").execute();
                            break;
                        case 3:
                            listParfumNew = new Select().from(BrenduAll.class).where("brend_id = ?", list.get(0).getChildItems().get(0).getId()).where("sex like ?", "унисекс").execute();
                            break;
                    }
                    AdapterOneBrend adapterOneBrend = new AdapterOneBrend(list, listParfumNew);
                    brendSearch.setAdapter(adapterOneBrend);
                }
            } else { //size=0
                Child newchild = new Child("0", getResources().getString(R.string.noBrendu), "0");
                List<Child> newListChild = new ArrayList<>();
                newListChild.add(newchild);
                SectionHeader newSection = new SectionHeader(newListChild, "0");
                List<SectionHeader> listNull = new ArrayList<>();
                listNull.add(newSection);
                List<BrenduAll> listParfumNew = new ArrayList<>();
                AdapterOneBrend adapterOneBrend = new AdapterOneBrend(listNull, listParfumNew);
                brendSearch.setAdapter(adapterOneBrend);

            }

        } else {
            mAdapter = new AdapterSectionBrendu(getContext(), listSectionNow);
            brendSearch.setAdapter(mAdapter);
        }

    }

    //Алфавитный список
    private List<SectionHeader> LoadListBrendWithSection(List<BrenduCount> brendu) {
        List<SectionHeader> listSection = new ArrayList<>();
        for (int i = 0; i < brendu.size(); i++) {
            String section = brendu.get(i).name.toUpperCase().substring(0, 1);
//            Log.d(LOG_TAG,"bukvu brendu ->> "+ section);
            List<Child> childList = new ArrayList<>();
            for (int y = i; y < brendu.size(); y++) {
                String firstLiter = brendu.get(y).name.toUpperCase().substring(0, 1);
//                Log.d(LOG_TAG,"first liter brend ->> "+ firstLiter);

                if (section.equals(firstLiter)) {
                    childList.add(new Child(brendu.get(y).id_brend, brendu.get(y).name, Integer.toString(brendu.get(y).count)));
                    if (brendu.size()-y == 1){
                        listSection.add(new SectionHeader(childList, section));
//                        Log.d(LOG_TAG,"bukvu brendu ->> "+ section+ " child count => "+childList.size());

                    }
                } else {
                    listSection.add(new SectionHeader(childList, section));
//                    Log.d(LOG_TAG,"bukvu brendu ->> "+ section+ " child count => "+childList.size());
                    i = y - 1;
                    break;
                }
            }
        }
        return listSection;
    }

    private class AdapterSectionBrendu extends SectionRecyclerViewAdapter<SectionHeader, Child, AdapterSectionBrendu.SectionViewHolder, AdapterSectionBrendu.ChildViewHolder> implements SectionIndexer {

        private Context context;
        private List<SectionHeader> list;

        public AdapterSectionBrendu(Context context, List<SectionHeader> sectionItemList) {
            super(context, sectionItemList);
            this.context = context;
            list = sectionItemList;
        }

        @Override
        public AdapterSectionBrendu.SectionViewHolder onCreateSectionViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.brend_search_section, viewGroup, false);
            return new AdapterSectionBrendu.SectionViewHolder(view);
        }

        @Override
        public AdapterSectionBrendu.ChildViewHolder onCreateChildViewHolder(final ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.brend_search_child, viewGroup, false);

            return new AdapterSectionBrendu.ChildViewHolder(view);
        }

        @Override
        public void onBindSectionViewHolder(AdapterSectionBrendu.SectionViewHolder sectionViewHolder, int i, SectionHeader sectionHeader) {
            sectionViewHolder.section.setText(sectionHeader.getSectinText());
        }

        @Override
        public void onBindChildViewHolder(AdapterSectionBrendu.ChildViewHolder childViewHolder, int i, int i1, final Child child) {
            if (child.getCount().equals("0")) {
//                childViewHolder.nameBrend.setText(child.getName());
//                childViewHolder.cd.setEnabled(false);
            } else {
                childViewHolder.nameBrend.setText(child.getName());
                String textCount = "(" + child.getCount() + ")";
                childViewHolder.countBrend.setText(textCount);
                childViewHolder.cd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("id", child.getId());
                        bundle.putString("name", child.getName());
                        bundle.putInt("state", 1);
                        loadBrendInfo.onLoadBrendInfo(bundle);
                    }
                });
            }
        }

        @Override
        public Object[] getSections() {
            List<String> sections = new ArrayList<>(list.size());
            mSectionPositions = new ArrayList<>(list.size());
            Integer position = 0;
            for (int i = 0, size = list.size(); i < size; i++) {

                String section = list.get(i).getSectinText().toUpperCase().substring(0, 1);
                if (!sections.contains(section)) {
                    sections.add(section);
                    if (i > 0) {
                        position = position + list.get(i - 1).getChildItems().size();
                    }
                    mSectionPositions.add(position + i);
                }
            }
            return sections.toArray(new String[0]);
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            Integer resultPosition;
            if (sectionIndex == 0) {
                resultPosition = mSectionPositions.get(sectionIndex);
            } else {
                resultPosition = mSectionPositions.get(sectionIndex);

            }
            return resultPosition;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

//        public void setFilter(String s) {
//
//                List<SectionHeader> newList = new ArrayList<>();
//                for (SectionHeader section : list
//                        ) {
//                    List<Child> newChild = new ArrayList<>();
//                    for (Child child : section.getChildItems()
//                            ) {
//                        String symbols = child.getName().toLowerCase().substring(0, s.length());
//                        if (symbols.equals(s.toLowerCase())) {
//                            newChild.add(child);
//                        }
//                    }
//                    if (newChild.size() > 0)
//                        newList.add(new SectionHeader(newChild, section.getSectinText()));
//                }
//                if (newList.size()>0){
//                    list = newList;
//                }
//
//        }

        public class SectionViewHolder extends RecyclerView.ViewHolder {

            TextView section;

            public SectionViewHolder(View itemView) {
                super(itemView);

                section = (TextView) itemView.findViewById(R.id.textsection);
            }
        }

        public class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView nameBrend;
            TextView countBrend;
            ImageView image;
            CardView cd;

            public ChildViewHolder(View itemView) {
                super(itemView);
                nameBrend = (TextView) itemView.findViewById(R.id.textName);
                countBrend = (TextView) itemView.findViewById(R.id.countBrendu);
                image = (ImageView) itemView.findViewById(R.id.imageBrenduNext);
                cd = (CardView) itemView.findViewById(R.id.cardSearchBrendu);

            }
        }


    }

    private class AdapterOneBrend extends RecyclerView.Adapter<AdapterOneBrend.OneBrendViewHolder> {

        private List<SectionHeader> brendu;
        private List<BrenduAll> parfum;

        public AdapterOneBrend(List<SectionHeader> brendu, List<BrenduAll> parfum) {
            this.brendu = brendu;
            this.parfum = parfum;
        }

        @Override
        public OneBrendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brend_search_child, parent, false);
            OneBrendViewHolder viewHolder = new OneBrendViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(OneBrendViewHolder holder, int position) {

            if (position == 0) {
                holder.nameBrend.setText(brendu.get(0).getChildItems().get(0).getName());
                if (brendu.get(0).getChildItems().get(0).getCount().equals("0")) {
                    holder.countBrend.setText("");
                    holder.countBrend.setWidth(holder.cd.getWidth());
                    holder.image.setVisibility(View.INVISIBLE);
                    holder.cd.setCardElevation(0);
                    holder.cd.setRadius(0);
                    holder.cd.setPreventCornerOverlap(false);
                } else {
                    holder.countBrend.setText(brendu.get(0).getChildItems().get(0).getCount());

                    holder.cd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", brendu.get(0).getChildItems().get(0).getId());
                            bundle.putString("name", brendu.get(0).getChildItems().get(0).getName());
                            bundle.putInt("state", 1);
                            loadBrendInfo.onLoadBrendInfo(bundle);
                        }
                    });
                }
            } else {
                holder.nameBrend.setText(parfum.get(position - 1).parfum_name);
                holder.countBrend.setText("");
            }

        }

        @Override
        public int getItemCount() {
            return parfum.size() + 1;
        }

        public class OneBrendViewHolder extends RecyclerView.ViewHolder {
            TextView nameBrend;
            TextView countBrend;
            ImageView image;
            CardView cd;

            public OneBrendViewHolder(View itemView) {
                super(itemView);
                nameBrend = (TextView) itemView.findViewById(R.id.textName);
                countBrend = (TextView) itemView.findViewById(R.id.countBrendu);
                image = (ImageView) itemView.findViewById(R.id.imageBrenduNext);
                cd = (CardView) itemView.findViewById(R.id.cardSearchBrendu);
            }
        }
    }
}