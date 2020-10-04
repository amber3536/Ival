package com.hfad.ival_revisited;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class StatsDetailFragment extends Fragment {
    private View view;
    private DBHelper dbHelper;
    private String position;
    private TextView totalPercent;
    private Button monthBtn;
    private Button yearBtn;
    private Button weekBtn;
    private Description description;
    private BarChart chart;
    private BarData data;
    private ArrayList dataSets;
    private XAxis xAxis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats_detail, container, false);
        Log.i("statsDetail", "onCreateView: arrived");
        dbHelper = new DBHelper(view.getContext());

        chart = (BarChart) view.findViewById(R.id.chart);
        monthBtn = view.findViewById(R.id.stats_month_btn);
        yearBtn = view.findViewById(R.id.stats_year_btn);
        weekBtn = view.findViewById(R.id.stats_week_btn);
        totalPercent = view.findViewById(R.id.stats_detail_total_percent);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getString("position", "");
            Log.i("statsDetail", "onCreateView: " + position);
        }
        //BarData data = new BarData(getXAxisValues(), getDataSet());
        dataSets = new ArrayList();
        xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        weekDisplay();

        YAxis yAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis yAxis1 = chart.getAxis(YAxis.AxisDependency.RIGHT);
       // yAxis.setStartAtZero(true);
        //yAxis1.setStartAtZero(true);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(1f);
        yAxis1.setAxisMinimum(0f);
        yAxis1.setAxisMaximum(1f);
       // chart.setVisibleYRangeMaximum(.9f, YAxis.AxisDependency.RIGHT);
//      yAxis.setSpaceMax(.1f);
//        yAxis1.setSpaceMax(.1f);
//        yAxis.setSpaceTop(1);
//        yAxis1.setSpaceTop(1);

//        DisplayMetrics ds = new DisplayMetrics();
//        ((QuickstartActivity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(ds);
//        int width = ds.widthPixels;
//        int height = ds.heightPixels;


//        YAxis yAxis = chart.getAxisRight();
//        YAxis yAxis1 = chart.getAxisLeft();
//        yAxis1.mAxisMinimum = 0;
//        yAxis.mAxisMinimum = 0;

        //description = new Description();
       // description.setPosition(width-50, height-175);



//        description.setTextSize(15f);
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "permanent_marker_reg.ttf");
//        description.setTypeface(tf);
        chart.getDescription().setEnabled(false);
        //chart.setDescription(description);
        chart.getLegend().setEnabled(false);

       // chart.animateXY(2000, 2000);
        chart.invalidate();

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chart.clearValues();
                chart.notifyDataSetChanged();
                chart.invalidate();
                weekDisplay();
                chart.invalidate();
            }
        });

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeDataSet(data);
                chart.clearValues();
                //chart.setData(null);
                chart.notifyDataSetChanged();
                chart.invalidate();
                BarData data1 = new BarData(getMonthDataSet());
                chart.setData(data1);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues2()));
                chart.invalidate();

                int month = LocalDate.now().getMonthValue();
                int totalMonthMade = dbHelper.totalMonthShotsMade(position, month);
                int totalMonthMissed = dbHelper.totalMonthShotsMissed(position, month);
                setTotalPercentDisplay(position, totalMonthMade, totalMonthMissed);
            }
        });

        yearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeDataSet(data);
                chart.clearValues();
                //chart.setData(null);
                chart.notifyDataSetChanged();
                chart.invalidate();
                BarData data1 = new BarData(getYearDataSet());
                chart.setData(data1);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues3()));
                chart.invalidate();

                int year = LocalDate.now().getYear();
                int totalYearMade = dbHelper.totalYearShotsMade(position, year);
                int totalYearMissed = dbHelper.totalYearShotsMissed(position, year);
                setTotalPercentDisplay(position, totalYearMade, totalYearMissed);
            }
        });

        return view;
    }
    
    private void setTotalPercentDisplay(String pos, int totalMade, int totalMissed) {
        String str = getContext().getResources().getString(R.string.stats_detail_total_made, pos, totalMade, totalMade + totalMissed);
        totalPercent.setText(str);
    }

    private void weekDisplay() {
        data = new BarData(getWeekDataSet());
        chart.setData(data);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));

        int day = LocalDate.now().getDayOfYear();
        int totalMade = dbHelper.totalWeekShotsMade(position, day-6, day);
        int totalMissed = dbHelper.totalShotsMissed(position, day-6, day);
        setTotalPercentDisplay(position, totalMade, totalMissed);
    }

    private ArrayList getWeekDataSet() {

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
        barDataSet1.setDrawValues(false);
        barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
        //barDataSet1.setBarBorderWidth(30);
        //BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList getMonthDataSet() {

        ArrayList valueSet1 = new ArrayList();

        ArrayList<Integer> madeList = dbHelper.shotsMadeWithinLastMonth(position);
        ArrayList<Integer> missedList = dbHelper.shotsMissedWithinLastMonth(position);
        ArrayList<Float> list = new ArrayList<>();

        for (int i = 0; i < madeList.size(); i++) {
            if (madeList.get(i) > 0)
                list.add(((float) madeList.get(i)/(madeList.get(i)+missedList.get(i))));
            else
                list.add(0f);
        }

        Log.i("statsDetail", "getDataSet: " + list);
       // Log.i("statsDetail", "getDataSet: " + list.get(6));

        for (int i = 0; i < list.size(); i++) {
            BarEntry barEntry = new BarEntry(i, list.get(i));
            valueSet1.add(barEntry);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Past month");
        barDataSet1.setDrawValues(false);
        barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);

        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList getYearDataSet() {

        ArrayList valueSet1 = new ArrayList();

        ArrayList<Integer> madeList = dbHelper.shotsMadeWithinLastYear(position);
        ArrayList<Integer> missedList = dbHelper.shotsMissedWithinLastYear(position);
        ArrayList<Float> list = new ArrayList<>();

        for (int i = 0; i < madeList.size(); i++) {
            if (madeList.get(i) > 0)
                list.add(((float) madeList.get(i)/(madeList.get(i)+missedList.get(i))));
            else
                list.add(0f);
        }

        Log.i("statsDetail", "getDataSet: " + list);
       // Log.i("statsDetail", "getDataSet: " + list.get(6));

        for (int i = 0; i < list.size(); i++) {
            BarEntry barEntry = new BarEntry(i, list.get(i));
            valueSet1.add(barEntry);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Past year");
        barDataSet1.setDrawValues(false);
        barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);

        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxisArray = new ArrayList();
        LocalDate localDate = LocalDate.now();
        Log.i("statsDetail", "getXAxisValues: " + localDate.minusDays(0).getDayOfWeek());
        for (int i = 6; i >= 0; i--) {
           // xAxis.add(localDate.minusDays(i).getDayOfWeek());
            String day = localDate.minusDays(i).getDayOfWeek().toString();

            if (day.equals("MONDAY"))
                xAxisArray.add("MON");
            else if (day.equals("TUESDAY"))
                xAxisArray.add("TUES");
            else if (day.equals("WEDNESDAY"))
                xAxisArray.add("WED");
            else if (day.equals("THURSDAY"))
                xAxisArray.add("THUR");
            else if (day.equals("FRIDAY"))
                xAxisArray.add("FRI");
            else if (day.equals("SATURDAY"))
                xAxisArray.add("SAT");
            else
                xAxisArray.add("SUN");
        }

        xAxis.setLabelCount(7, false);

        return xAxisArray;
    }

    private ArrayList getXAxisValues2() {
        ArrayList xAxisArray = new ArrayList();
        LocalDate localDate = LocalDate.now();
        int currDay = localDate.getDayOfMonth();

        Log.i("statsDetail", "getXAxisValues: " + localDate.minusDays(0).getDayOfWeek());
        for (int i = 1; i <= currDay; i++) {
            // xAxis.add(localDate.minusDays(i).getDayOfWeek());
           // String day = localDate.minusDays(i).getDayOfWeek().toString();
            xAxisArray.add(Integer.toString(i));
        }


        if (currDay < 5) {
            xAxis.setLabelCount(currDay, false);
        }
        else
            xAxis.setLabelCount(5, false);


        Log.i("statsDetail", "getXAxisValues: " + xAxisArray);
        return xAxisArray;
    }

    private ArrayList getXAxisValues3() {
        ArrayList xAxisArray = new ArrayList();
        LocalDate localDate = LocalDate.now();
        Log.i("statsDetail", "getXAxisValues: " + localDate.minusDays(0).getDayOfWeek());
        int monthNum = localDate.getMonthValue();
        //int monthNum = 2;


        for (int i = monthNum-1; i >= 0; i--) {
            // xAxis.add(localDate.minusDays(i).getDayOfWeek());
            //String month = localDate.minusDays(i).getDayOfWeek().toString();
            String month = localDate.minusMonths(i).getMonth().toString();

            if (month.equals("JANUARY"))
                xAxisArray.add("JAN");
            else if (month.equals("FEBRUARY"))
                xAxisArray.add("FEB");
            else if (month.equals("MARCH"))
                xAxisArray.add("MAR");
            else if (month.equals("APRIL"))
                xAxisArray.add("APR");
            else if (month.equals("MAY"))
                xAxisArray.add("MAY");
            else if (month.equals("JUNE"))
                xAxisArray.add("JUN");
            else if (month.equals("JULY"))
                xAxisArray.add("JUL");
            else if (month.equals("AUGUST"))
                xAxisArray.add("AUG");
            else if (month.equals("SEPTEMBER"))
                xAxisArray.add("SEP");
            else if (month.equals("OCTOBER"))
                xAxisArray.add("OCT");
            else if (month.equals("NOVEMBER"))
                xAxisArray.add("NOV");
            else
                xAxisArray.add("DEC");
        }

        if (monthNum < 5) {

            xAxis.setLabelCount(monthNum, true);
        }
        else
          xAxis.setLabelCount(5, false);


        return xAxisArray;
    }
}
