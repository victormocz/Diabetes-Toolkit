package auburn.com.diabetes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.ReadingDatabaseHandler;
import model.Readinginfo;

/**
 * Created by victorm on 4/17/16.
 */
public class Reading extends Fragment {
    private FloatingActionButton addfab;
    private ListView readinglist;
    private ArrayList<Readinginfo> readings;
    private ReadingDatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_reading, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        addfab = (FloatingActionButton) getActivity().findViewById(R.id.addfab);
        readinglist = (ListView) getActivity().findViewById(R.id.allreadinglist);
        db = new ReadingDatabaseHandler(getActivity());
        readings = db.getReadingList();
        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddReading.class));
            }
        });
        ReadingAdapter ra = new ReadingAdapter(getContext(), R.layout.reading_row, readings);

        readinglist.setAdapter(ra);
        readinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReadingDetail.class);
                intent.putExtra("id", readings.get(position).getId());
                intent.putExtra("name", readings.get(position).getName());
                intent.putExtra("glucose", readings.get(position).getGlucose());
                intent.putExtra("date", readings.get(position).getDate());
                intent.putExtra("note", readings.get(position).getNote());

                startActivity(intent);
            }
        });
    }

    private class ReadingAdapter extends ArrayAdapter {
        public ReadingAdapter(Context context, int resource, List<Readinginfo> objects) {
            super(context, resource, objects);
            this.inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        }

        private LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return readings.size();
        }

        @Override
        public Object getItem(int position) {
            return readings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            view = inflater.inflate(R.layout.reading_row, null);
            TextView name = (TextView) view.findViewById(R.id.readingname);
            TextView date = (TextView) view.findViewById(R.id.readingdate);
            TextView glucose = (TextView) view.findViewById(R.id.glucose);
            ImageView thumbnail = (ImageView) view.findViewById(R.id.readingimage);
            name.setText(readings.get(position).getName());
            glucose.setText(Double.toString(readings.get(position).getGlucose()) + " mg/dL");
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            SimpleDateFormat simpleformat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
            String datedata = simpleformat.format(new Date(readings.get(position).getDate()).getTime());
            date.setText(datedata);

            return view;
        }
    }
}
