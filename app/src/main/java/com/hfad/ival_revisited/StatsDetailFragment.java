package com.hfad.ival_revisited;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class StatsDetailFragment extends Fragment {
    private View view;
    private DBHelper dbHelper;
    private String position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats_detail, container, false);
        Log.i("statsDetail", "onCreateView: arrived");
        dbHelper = new DBHelper(view.getContext());

        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getString("position", "");
            Log.i("statsDetail", "onCreateView: " + position);
        }
        //BarData data = new BarData(getXAxisValues(), getDataSet());
        BarData data = new BarData(getDataSet());
        chart.setData(data);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
        //chart.setDescription("My Chart");
       // chart.animateXY(2000, 2000);
        chart.invalidate();

        return view;
    }

    private ArrayList getDataSet() {
        ArrayList dataSets = null;

        ArrayList valueSet1 = new ArrayList();

        ArrayList<Integer> madeList = dbHelper.shotsMadeWithinLastWeek(position);
        ArrayList<Integer> missedList = dbHelper.shotsMissedWithinLastWeek(position);
        ArrayList<Float> list = new ArrayList<>();

        for (int i = 0; i < madeList.size(); i++) {
            if (madeList.get(i) > 0)
                list.add(((float) madeList.get(i)/(madeList.get(i)+missedList.get(i))));
            else
                list.add(0f);
        }

        //ArrayList<Integer> list = dbHelper.shotsMadeWithinLastWeek(position);
        Log.i("statsDetail", "getDataSet: " + list);
        Log.i("statsDetail", "getDataSet: " + list.get(6));

      BarEntry dayZero = new BarEntry(0, list.get(0));
      valueSet1.add(dayZero);
      BarEntry dayOne = new BarEntry(1, list.get(1));
      valueSet1.add(dayOne);
      BarEntry dayTwo = new BarEntry(2, list.get(2));
      valueSet1.add(dayTwo);
      BarEntry dayThree = new BarEntry(3, list.get(3));
      valueSet1.add(dayThree);
      BarEntry dayFour = new BarEntry(4, list.get(4));
      valueSet1.add(dayFour);
      BarEntry dayFive = new BarEntry(5, list.get(5));
      valueSet1.add(dayFive);
      BarEntry daySix = new BarEntry(6, list.get(6));
      valueSet1.add(daySix);

       // dbHelper.
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);

//        ArrayList valueSet2 = new ArrayList();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Past week");
        barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
        //barDataSet1.setBarBorderWidth(30);
        //BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        LocalDate localDate = LocalDate.now();
        Log.i("statsDetail", "getXAxisValues: " + localDate.minusDays(0).getDayOfWeek());
        for (int i = 6; i >= 0; i--) {
           // xAxis.add(localDate.minusDays(i).getDayOfWeek());
            String day = localDate.minusDays(i).getDayOfWeek().toString();

            if (day.equals("MONDAY"))
                xAxis.add("MON");
            else if (day.equals("TUESDAY"))
                xAxis.add("TUES");
            else if (day.equals("WEDNESDAY"))
                xAxis.add("WED");
            else if (day.equals("THURSDAY"))
                xAxis.add("THUR");
            else if (day.equals("FRIDAY"))
                xAxis.add("FRI");
            else if (day.equals("SATURDAY"))
                xAxis.add("SAT");
            else
                xAxis.add("SUN");
        }
//        xAxis.add("JAN");
//        xAxis.add("FEB");
//        xAxis.add("MAR");
//        xAxis.add("APR");
//        xAxis.add("MAY");
//        xAxis.add("JUN");
        return xAxis;
    }
}
