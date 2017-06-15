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
import android.widget.Toast;

import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumForBrend;
import by.lykashenko.demon.mirparfumanew.Adapters.Child;
import by.lykashenko.demon.mirparfumanew.Adapters.SectionHeader;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.BrendList;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetParfumForBrend;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by Admin on 05.06.17.
 */

public class Search extends Fragment implements BrendList.OnLoadBrendList, GetParfumForBrend.OnLoadParfumList {

    private IndexFastScrollRecyclerView brendSearch;
    private ArrayList<Integer> mSectionPositions;
    private EditText searchNameBrendu;
    private ArrayList<Brendu> arrayBrendu, brenduRead;
    private TestAdapter mAdapter;
    private FragmentParfumList parfumList;
    private String sql_load_brendu;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vFragment = inflater.inflate(R.layout.fragment_search, null);

        sql_load_brendu = "SELECT brendu.id,brendu.pagetitle, count(val.contentid) FROM modx_site_tmplvar_contentvalues as val,(SELECT con.id,con.pagetitle FROM modx_site_content as con WHERE con.parent = 854)as brendu WHERE brendu.pagetitle LIKE val.value GROUP BY brendu.pagetitle ORDER BY brendu.pagetitle ASC";

        parfumList = new FragmentParfumList();
        searchNameBrendu = (EditText) vFragment.findViewById(R.id.brendsearch);
        final ImageView clearSearch = (ImageView) vFragment.findViewById(R.id.imageViewClear);
        clearSearch.setVisibility(View.INVISIBLE);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNameBrendu.setText("");
            }
        });
//        brenduRead = arrayBrendu;

        searchNameBrendu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s == null) {
                    clearSearch.setVisibility(View.INVISIBLE);
                } else {
                    clearSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) clearSearch.setVisibility(View.INVISIBLE);
                else clearSearch.setVisibility(View.VISIBLE);


                Changeadapter(brenduRead, s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        // downloads list with brend name from site
//        String sql_load_brendu = "SELECT brendu.id,brendu.pagetitle, count(val.contentid) FROM modx_site_tmplvar_contentvalues as val,(SELECT con.id,con.pagetitle FROM modx_site_content as con WHERE con.parent = 854)as brendu WHERE brendu.pagetitle LIKE val.value GROUP BY brendu.pagetitle ORDER BY brendu.pagetitle ASC";

        brendSearch = (IndexFastScrollRecyclerView) vFragment.findViewById(R.id.brendSearch);

        BrendList brendList = new BrendList(getActivity());
        brendList.registerOnLoadBrendList(this);
        brendList.load(sql_load_brendu);
        return vFragment;
    }

    private void Changeadapter(ArrayList<Brendu> array, CharSequence s) {

        ArrayList<Brendu> brenduNew = new ArrayList<>();
        String string = s.toString();

        if (array.size() == 0) {

            if (string.equals("")) {
                Changeadapter(arrayBrendu, s);
            } else {
                Log.i(MainActivity.LOG_TAG, "нет брендов");
                Brendu noBrend = new Brendu();
                noBrend.setId("0");
                noBrend.setPagetitle(getResources().getString(R.string.noBrendu));
                noBrend.setAlias("");
                noBrend.setCount(0);
                noBrend.setValue("");
                ArrayList<Brendu> no_Brend = new ArrayList<>();
                no_Brend.add(noBrend);
                mAdapter = new TestAdapter(no_Brend);

            }
        } else {

            if (string.equals("")) {
                List<SectionHeader> listSection = LoadListBrendWithSection(arrayBrendu);
                AdapterSectionBrendu mAdapter = new AdapterSectionBrendu(getContext(), listSection);
                brendSearch.setIndexbarWidth(48);
                brendSearch.setAdapter(mAdapter);
                brenduRead = arrayBrendu;

            } else {
                Log.i(MainActivity.LOG_TAG, "Есть данные для проверки");
                for (Brendu brend : array) {
                    String string1 = null;
                    if (string.length() > brend.getPagetitle().length()) {
                        break;
                    } else {
                        string1 = brend.getPagetitle().toLowerCase().substring(0, string.length());
                        if (string1.equals(string.toLowerCase())) {
                            brenduNew.add(brend);
                        }
                    }
                }
                if (brenduNew.size() == 1) {
                    Log.i(MainActivity.LOG_TAG, "brendnew size => " + brenduNew.size());
                    String idBrend = brenduNew.get(0).getId();
                    Log.i(MainActivity.LOG_TAG, "id brend => " + idBrend);
                    String sqlQueryGetParfum = "SELECT con1.contentid, con1.value" +
                            " FROM modx_site_tmplvar_contentvalues as con1," +
                            "(SELECT val.contentid FROM modx_site_content as con, modx_site_tmplvar_contentvalues as val" +
                            " where con.id=" + idBrend + " and con.pagetitle LIKE val.value) as val1" +
                            " WHERE val1.contentid=con1.contentid and tmplvarid=65 ORDER BY con1.value ASC";
                    Log.i(MainActivity.LOG_TAG, "string sql => " + sqlQueryGetParfum);
                    GetParfumForBrend getParfum = new GetParfumForBrend(getActivity());
                    getParfum.registerOnLoadParfumList(this);
                    getParfum.load(sqlQueryGetParfum, brenduNew);

                } else {
                    mAdapter = new TestAdapter(brenduNew);
//                    List<SectionHeader> listSection1 = LoadListBrendWithSection(brenduNew);
//                    AdapterSectionBrendu mAdapter = new AdapterSectionBrendu(getContext(), listSection1);
//                    brendSearch.setLayoutManager(new LinearLayoutManager(getContext()));
//                    brendSearch.setIndexbarWidth(0);
                    brendSearch.setAdapter(mAdapter);
//                    brenduRead = brenduNew;
                }
            }
        }

    }

    @Override
    public void onLoadParfumList(ArrayList<ParfumForBrend> parfumForBrends, ArrayList<Brendu> brend) {
        ArrayList<Brendu> output = new ArrayList<>();
        output.add(brend.get(0));
        Log.i(MainActivity.LOG_TAG, "parfum size => " + parfumForBrends.size());
        for (ParfumForBrend parfum : parfumForBrends) {
            Brendu brendOne = new Brendu();
            brendOne.setId(parfum.getContentid());
            brendOne.setPagetitle(parfum.getValue());
            brendOne.setAlias("");
            brendOne.setCount(0);
            brendOne.setValue("");
            output.add(brendOne);
        }

        mAdapter = new TestAdapter(output);
        brendSearch.setIndexbarWidth(0);
        brendSearch.setAdapter(mAdapter);
    }

    @Override
    public void onLoadBrendList(ArrayList<Brendu> brendu) {
        arrayBrendu = brendu;
        brenduRead = brendu;
        List<SectionHeader> listSection = LoadListBrendWithSection(brendu);
        brendSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterSectionBrendu mAdapter = new AdapterSectionBrendu(getContext(), listSection);
        brendSearch.setAdapter(mAdapter);
    }

    private List<SectionHeader> LoadListBrendWithSection(ArrayList<Brendu> brendu) {
        List<SectionHeader> listSection = new ArrayList<>();
        for (int i = 0; i < brendu.size(); i++) {

            String section = brendu.get(i).getPagetitle().toUpperCase().substring(0, 1);
            List<Child> childList = new ArrayList<>();
            for (int y = i; y < brendu.size(); y++) {

                String firstLiter = brendu.get(y).getPagetitle().toUpperCase().substring(0, 1);
                if (section.equals(firstLiter)) {
                    childList.add(new Child(brendu.get(y).getId(), brendu.get(y).getPagetitle(), brendu.get(y).getCount().toString()));
                } else {
                    listSection.add(new SectionHeader(childList, section));
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
            View view = LayoutInflater.from(context).inflate(R.layout.brend_searc, viewGroup, false);

            return new AdapterSectionBrendu.ChildViewHolder(view);
        }

        @Override
        public void onBindSectionViewHolder(AdapterSectionBrendu.SectionViewHolder sectionViewHolder, int i, SectionHeader sectionHeader) {
            sectionViewHolder.section.setText(sectionHeader.getSectinText());
        }

        @Override
        public void onBindChildViewHolder(AdapterSectionBrendu.ChildViewHolder childViewHolder, int i, int i1, final Child child) {
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

                    Toast.makeText(context, "pressed id brend => " + child.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public Object[] getSections() {
            List<String> sections = new ArrayList<>(26);
            mSectionPositions = new ArrayList<>(26);
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

    private class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
        private ArrayList<Brendu> array;

        private TestAdapter(ArrayList<Brendu> array) {
            this.array = array;
        }

//        }

        @Override
        public TestAdapter.TestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brend_searc, viewGroup, false);
            TestAdapter.TestViewHolder testViewHolder = new TestAdapter.TestViewHolder(v);


            return testViewHolder;
        }

        @Override
        public void onBindViewHolder(TestAdapter.TestViewHolder testViewHolder, final int i) {
            final Integer position = i;


//            testViewHolder.nameBrend.setText(getKey(i));
//            testViewHolder.countBrend.setText(array.get(getKey(i)).size());


            testViewHolder.nameBrend.setText(array.get(i).getPagetitle());
            Integer count = array.get(i).getCount();
            if (count == 0) {
                testViewHolder.countBrend.setVisibility(View.INVISIBLE);
            } else {
                String countString = "(" + count.toString() + ")";
                testViewHolder.countBrend.setText(countString);
            }

            testViewHolder.cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (array.get(position).getCount() != 0) {
                        //след. строку заменить на вызов fragment

                        bundle.putString("id", array.get(i).getId());
                        bundle.putString("name", array.get(i).getPagetitle());
                        bundle.putInt("state", 1);


                    }else{
                        bundle.putString("id", array.get(i).getId());
                        bundle.putString("name", array.get(i).getPagetitle());
                        bundle.putInt("state", 2);
                    }
                    loadBrendInfo.onLoadBrendInfo(bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
//            return book.size();
            Integer size = array.size();


            return size;
        }

        public class TestViewHolder extends RecyclerView.ViewHolder {
            TextView nameBrend;
            TextView countBrend;
            ImageView image;
            CardView cd;

            public TestViewHolder(View itemView) {

                super(itemView);
                nameBrend = (TextView) itemView.findViewById(R.id.textName);
                countBrend = (TextView) itemView.findViewById(R.id.countBrendu);
                image = (ImageView) itemView.findViewById(R.id.imageBrenduNext);
                cd = (CardView) itemView.findViewById(R.id.cardSearchBrendu);

            }
        }
    }

    public interface LoadBrendInfo {
        void onLoadBrendInfo(Bundle bundle);
    }

    private LoadBrendInfo loadBrendInfo;

    public void registerLoadBrendInfo(LoadBrendInfo loadBrendInfo) {
        this.loadBrendInfo = loadBrendInfo;
    }

}
