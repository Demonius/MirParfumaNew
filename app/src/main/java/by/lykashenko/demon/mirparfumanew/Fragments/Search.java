package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.l4digital.fastscroll.FastScroller;

import by.lykashenko.demon.mirparfumanew.R;

/**
 * Created by demon on 08.02.2017.
 */

public class Search extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vFragment = inflater.inflate(R.layout.fragment_search, null);

        String[] book = {"A", "A","A","A","A","A","A","A","A","A","A"
        ,"B","B","B","B","B","B","B","B","B","B","B","B"
                ,"C","C","C","C","C","C","C","C","C","C"
                ,"D","D","D","D","D","D","D","D","D","D"};

        FastScrollRecyclerView brendSearch = (FastScrollRecyclerView) vFragment.findViewById(R.id.recyclerViewSearchBrend);
        brendSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        brendSearch.setAdapter(new TestAdapter(book));



        return vFragment;
    }

    private class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> implements FastScroller.SectionIndexer {
        private String[] book;
        private TestAdapter(String[] book) {
            this.book=book;
        }

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brend_searc,viewGroup,false);
            TestViewHolder testViewHolder = new TestViewHolder(v);

            return testViewHolder;
        }

        @Override
        public void onBindViewHolder(TestViewHolder testViewHolder, int i) {
            testViewHolder.text.setText(book[i]);
        }

        @Override
        public int getItemCount() {
            return book.length;
        }

        @Override
        public String getSectionText(int i) {
            return String.valueOf(book[i].charAt(0));
        }

        public class TestViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            public TestViewHolder(View itemView) {

                super(itemView);
                text = (TextView) itemView.findViewById(R.id.textName);
            }
        }
    }


}
