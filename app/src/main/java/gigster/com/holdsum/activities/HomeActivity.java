package gigster.com.holdsum.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.BorrowFragment;
import gigster.com.holdsum.fragments.DashboardFragment;
import gigster.com.holdsum.fragments.ProfileFragment;
import gigster.com.holdsum.fragments.StatsFragment;
import gigster.com.holdsum.helper.DataManager;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.enums.Mode;
import gigster.com.holdsum.presenters.HomePresenter;
import gigster.com.holdsum.presenters.core.NoPresenter;
import gigster.com.holdsum.presenters.core.UsesPresenter;

@UsesPresenter(HomePresenter.class)
public class HomeActivity extends HoldsumActivity {

    public static final int[] borrowTabTitles = {R.string.dashboard, R.string.borrow, R.string.stats, R.string.profile};
    public static final int[] borrowTabDrawables = {R.drawable.ic_marketplace, R.drawable.ic_transfer, R.drawable.ic_stats, R.drawable.ic_profile};

    private TabLayout tabLayout;
    private MyFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "HomeActivity.onCreate");
        setContentView(R.layout.activity_home);
        tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTabs();
    }

    private void updateTabs() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(null);
            tab.setCustomView(fragmentPagerAdapter.getTabView(i));
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public static final int TAB_COUNT = 4;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DashboardFragment();
                case 1:
                    return new BorrowFragment();
                case 2:
                    return new StatsFragment();
                case 3:
                    return new ProfileFragment();
            }
            throw new IllegalArgumentException("Trying to show an imaginary fragment");
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.tab_item, null);
            TextView tv = (TextView) v.findViewById(R.id.tab_text);
            ImageView imageView = (ImageView) v.findViewById(R.id.icon);
            tv.setText(borrowTabTitles[position]);
            imageView.setImageResource(borrowTabDrawables[position]);
            return v;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
