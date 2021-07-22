package sg.edu.np.tracknshare.models;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;

public class Bargraph {

    public void setChart(ArrayList<BarEntry> data, BarChart barChart,int color){
        BarDataSet barDataSet  = new BarDataSet(data,null);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barDataSet.setColor(color);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);barChart.getAxisRight().setEnabled(false);
        barChart.setDescription(null);
        barChart.setScaleEnabled(false);
        barChart.animateY(900);

    }
}
