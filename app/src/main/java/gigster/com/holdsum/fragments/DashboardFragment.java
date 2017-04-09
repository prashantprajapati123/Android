package gigster.com.holdsum.fragments;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.DashboardDataBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.Utils;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.presenters.DashboardPresenter;
import gigster.com.holdsum.presenters.core.UsesPresenter;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * A simple {@link Fragment} subclass.
 */
@UsesPresenter(DashboardPresenter.class)
public class DashboardFragment extends HoldsumFragment<DashboardPresenter> {

    private PastAdapter pastItemAdapter;
    private java.util.List<LoanRequest> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i("Fragment", "DashboardFragment.onCreateView");
        // Inflate the layout for this fragment
        mData = mPresenter.getData();
        List<LoanRequest> recentLoans = mData.size() > 6 ? mData.subList(0, 6) : mData;
        DashboardDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        binding.pastPaymentList.setHasFixedSize(true);
        LinearLayoutManager pastPaymentManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.pastPaymentList.setLayoutManager(pastPaymentManager);
        binding.pastPaymentList.addItemDecoration(new VerticalSpaceItemDecoration((int) getResources().getDimension(R.dimen.recyclerview_spacing)));
        pastItemAdapter = new PastAdapter(recentLoans != null && recentLoans.size() > 0 ? recentLoans.subList(1, recentLoans.size()) : recentLoans);
        binding.pastPaymentList.setAdapter(pastItemAdapter);

        LoanRequest current = mData != null && mData.size() > 0 ? mData.get(0) : new LoanRequest();
            int days = 0;
            try {
                days = mData != null && mData.size() > 0 ? (int) TimeUnit.MILLISECONDS.toDays(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(current.repayment_date).getTime() - new java.util.Date().getTime()) : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        Map<String, String> parts = new LinkedHashMap<>();
        parts.put("Due in ", "#747474");
        parts.put(String.valueOf(days), "#0096a9");
        parts.put(" days", "#747474");
        binding.dueIn.setText(Utils.colorText(parts));
        binding.amount.setText(mData != null ? "$" + current.amount : "");
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        return binding.getRoot();
    }

    static class PastViewHolder extends RecyclerView.ViewHolder {

        TextView paymentAmount;
        TextView dueDate;
        TextView dueIn;

        public PastViewHolder(View itemView) {
            super(itemView);
            paymentAmount = (TextView) itemView.findViewById(R.id.payment_amount);
            dueDate = (TextView) itemView.findViewById(R.id.due_date);
            dueIn = (TextView) itemView.findViewById(R.id.due_in);
        }
    }

    static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            }
        }
    }

    class PastAdapter extends RecyclerView.Adapter<PastViewHolder> {

        //TODO some datatype that holds past item information
        List<LoanRequest> items;

        public PastAdapter(List<LoanRequest> items) {
            this.items = items;
        }

        @Override
        public PastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_loan_item, parent, false);
            return new PastViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PastViewHolder holder, int position) {
            LoanRequest loan = items.get(position);
            holder.paymentAmount.setText("Payment $" + loan.amount);
            holder.dueDate.setText("Due " + loan.repayment_date);
            Map<String, String> parts = new LinkedHashMap<>();
            int days = 0;
            try {
                days = (int)TimeUnit.MILLISECONDS.toDays(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(loan.repayment_date).getTime() - new java.util.Date().getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            parts.put("Due in ", "#747474");
            parts.put(String.valueOf(days), "#0096a9");
            parts.put(" days", "#747474");
            holder.dueIn.setText(Utils.colorText(parts));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
