package auburn.com.diabetes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.ReadingDatabaseHandler;
import model.Readinginfo;

public class Linechart extends AppCompatActivity {
    private LineChart overallchart;
    private ReadingDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeLineChart();
    }

    private void initializeLineChart() {
        db = new ReadingDatabaseHandler(this);

        ArrayList<Readinginfo> allreadings = db.getReadingList();
        if (allreadings == null) {
            return;
        }

        overallchart = (LineChart) findViewById(R.id.chart);
        overallchart.setDescription("Daily Blood Glucose");

        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        SimpleDateFormat simpleformat = new SimpleDateFormat("MM/dd HH:mm");
        float avg = 0.0f;
        float upper = 0.0f;
        float lower = 0.0f;
        for (int i = 0; i < allreadings.size(); i++) {
            float value = (float)allreadings.get(i).getGlucose();
            valsComp1.add(new Entry(value,i));
            if (value > upper) {
                upper = value;
            }
            if (value < lower) {
                lower = value;
            }
            avg += value;
            xVals.add(simpleformat.format(new Date(allreadings.get(i).getDate())));
        }


        avg = avg / allreadings.size();
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Glucose status");
        setComp1.setColor(Color.rgb(140, 234, 255));
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        setComp1.setDrawCircles(true);
        setComp1.setDrawCircleHole(false);
        setComp1.setCircleColor(Color.rgb(140, 234, 255));
        setComp1.setDrawFilled(true);
        setComp1.setFillAlpha(2000);
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        YAxis leftAxis = overallchart.getAxisLeft();
        YAxis rightAxis = overallchart.getAxisRight();
        XAxis bottomAxis = overallchart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        rightAxis.setEnabled(false);
        leftAxis.setAxisMaxValue(250);
        leftAxis.setMinWidth(0);
        LimitLine avgline = new LimitLine(avg,"Average Glucose");
        avgline.enableDashedLine(8, 3, 2);
        avgline.setLineColor(Color.GREEN);
        avgline.setLineWidth(1f);
        avgline.setTextSize(10f);
        leftAxis.addLimitLine(avgline);

        LimitLine upperline = new LimitLine(upper,"Upper Bond");
        upperline.enableDashedLine(8, 3, 2);
        upperline.setLineColor(Color.RED);
        upperline.setLineWidth(1f);
        upperline.setTextSize(10f);
        leftAxis.addLimitLine(upperline);

        LimitLine lowerline = new LimitLine(lower,"Lower Bond");
        lowerline.enableDashedLine(8, 3, 2);
        lowerline.setLineColor(Color.BLUE);
        lowerline.setLineWidth(1f);
        lowerline.setTextSize(10f);
        leftAxis.addLimitLine(lowerline);

        overallchart.animateX(2000);
        overallchart.animateY(2000);

        overallchart.setData(data);
        overallchart.invalidate(); // refresh
    }

}
