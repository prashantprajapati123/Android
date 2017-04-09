package gigster.com.holdsum.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.StatsDataBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.presenters.StatsPresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by Eshaan on 3/15/2016.
 */
@UsesPresenter(StatsPresenter.class)
public class StatsFragment extends HoldsumFragment<StatsPresenter> {

    private java.util.List<LoanRequest> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i("Fragment", "StatsFragment.onCreateView");
        // Inflate the layout for this fragment
        mData = mPresenter.getData();
        int total = 0;
        for (LoanRequest loan : mData) total += Double.parseDouble(loan.amount);
        StatsDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_stats, container, false);
        binding.amount.setText("$" + total);
        List<LoanRequest> recentLoans = mData.size() > 6 ? mData.subList(0, 6) : mData;
        CombinedData data = new CombinedData(getXAxisValues(recentLoans));
        data.setData(barData(recentLoans));
        data.setData(lineData(recentLoans));
        binding.chart.setData(data);
        YAxis y = binding.chart.getAxisLeft();
        y.setAxisMaxValue(600f);
        binding.chart.invalidate();
        return binding.getRoot();
    }

    public LineData lineData(List<LoanRequest> loans){
        ArrayList<Entry> line = new ArrayList<>();
        for (LoanRequest loan : loans)
            line.add(new Entry(8f, loans.indexOf(loan)));
        LineDataSet lineDataSet = new LineDataSet(line, "Interest");
        lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        LineData lineData = new LineData(getXAxisValues(loans),lineDataSet);
        return lineData;
    }

    public BarData barData(List<LoanRequest> loans){
        ArrayList<BarEntry> group1 = new ArrayList<>();
        for (LoanRequest loan : loans)
            group1.add(new BarEntry(Float.parseFloat(loan.amount), loans.indexOf(loan)));
        BarDataSet barDataSet = new BarDataSet(group1, "Principal");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(getXAxisValues(loans),barDataSet);
        return barData;
    }

    private ArrayList<String> getXAxisValues(List<LoanRequest> loans) {
        ArrayList<String> labels = new ArrayList<>();
        for (LoanRequest loan : loans) labels.add(loan.repayment_date);
        return labels;
    }
}
