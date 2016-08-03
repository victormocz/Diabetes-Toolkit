package auburn.com.diabetes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Bookinfo;

/**
 * Created by victorm on 4/13/16.
 */
public class Education extends Fragment {
    private ListView readinglist;
    private List<Bookinfo> books = new ArrayList<Bookinfo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_education, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeComponent();

    }

    private void initializeComponent() {
        readinglist = (ListView) getView().findViewById(R.id.readinglist);
        ReadingAdapter ra = new ReadingAdapter();
        readinglist.setAdapter(ra);
        readinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BookDetail.class);
                intent.putExtra("url", books.get(position).getUrl());
                startActivity(intent);
            }
        });
        books.add(new Bookinfo("Basics", 1));
        books.add(new Bookinfo("Diagnosis", 1));
        books.add(new Bookinfo("Diagnosis", 0));
        books.add(new Bookinfo("Type 1 Diabetes", 0));
        books.add(new Bookinfo("Type 2 Diabetes", 0));
        books.add(new Bookinfo("Prediabetes", 0));
        books.add(new Bookinfo("A1CeAGG", 0));
        books.add(new Bookinfo("Factors Affecting Blood Glucose", 0));
        books.add(new Bookinfo("Medications for Type 2 Diabetes", 0));
        books.add(new Bookinfo("Gestational Diabetes", 0));
        books.add(new Bookinfo("Alcohol", 0));
        books.add(new Bookinfo("Healthy Food Choices", 0));
        books.add(new Bookinfo("Cutting Back on Salt", 0));
        books.add(new Bookinfo("Budget Friendly Cooking", 0));
        books.add(new Bookinfo("Diabetes & Depression", 0));
        books.add(new Bookinfo("Diabetes & Stress", 0));
        books.add(new Bookinfo("Diabetes & Well Being", 0));

    }

    private class ReadingAdapter extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            view = inflater.inflate(R.layout.education_row, null);
            TextView content = (TextView) view.findViewById(R.id.content);
            TextView type = (TextView) view.findViewById(R.id.type);
            ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            content.setText(books.get(position).getTitle());
            if (books.get(position).getType() == 1) {
                type.setText("HTML Document");
                thumbnail.setImageResource(R.drawable.ic_html);
            } else {
                type.setText("PDF Document");
                thumbnail.setImageResource(R.drawable.ic_pdf);
            }
            return view;
        }
    }
}
