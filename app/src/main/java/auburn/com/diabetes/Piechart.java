package auburn.com.diabetes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import data.ReadingDatabaseHandler;
import model.Readinginfo;

public class Piechart extends AppCompatActivity {
    private static double normalRangeUpper = 100;
    private static double normalRangeLower = 70;
    private PieChart pie;
    private ReadingDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeChart();
    }

    private void initializeChart() {
        db = new ReadingDatabaseHandler(this);
        ArrayList<Readinginfo> allreadings = db.getReadingList();
        if (allreadings == null) {
            return;
        }
        pie = (PieChart)findViewById(R.id.pie);
        pie.setDescription("Overall Blood Glucose");
        ArrayList<Entry> piedataset = new ArrayList<Entry>();
        float high = 0;
        float normal = 0;
        float low = 0;
        for (int i = 0; i < allreadings.size(); i++) {
            double value = allreadings.get(i).getGlucose();
            if (value < normalRangeLower) {
                low = low + 1;
            }else if (value >= normalRangeLower && value < normalRangeUpper) {
                normal = normal + 1;
            }else if (normalRangeUpper >= 100) {
                high = high + 1;
            }
        }
        low = low / allreadings.size();
        normal = normal / allreadings.size();
        high = high / allreadings.size();
        List<String> labels = new ArrayList<String>();

        if(low != 0.0){
            piedataset.add(new Entry(low,0));
            labels.add("Low Range: " + String.format("%.2f", low*100) + "%");
        }
        if(normal != 0.0) {
            piedataset.add(new Entry(normal, 1));
            labels.add("Normal Range: " + String.format("%.2f", normal*100) + "%");
        }
        if (high != 0.0){
            piedataset.add(new Entry(high, 2));
            labels.add("High Range: " + String.format("%.2f", high*100) + "%");
        }


        int[] colors = {ColorTemplate.VORDIPLOM_COLORS[3],ColorTemplate.VORDIPLOM_COLORS[0],ColorTemplate.VORDIPLOM_COLORS[4]};
        PieDataSet dataset = new PieDataSet(piedataset," of Blood Glucose");
        dataset.setValueTextSize(15f);
        dataset.setDrawValues(false);
        dataset.setColors(colors);
        dataset.setSliceSpace(3f);
        PieData piedata = new PieData(labels,dataset);
        pie.setDrawHoleEnabled(false);
        pie.animateX(2000);
        pie.animateY(2000);
        pie.setData(piedata);
    }

}
