package com.salestracker.sales.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.salestracker.sales.R;
import com.salestracker.sales.fragments.ProductsImageFragment;
import com.salestracker.sales.widgets.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ViewPager viewPagerProductDetail;
    private CirclePageIndicator cpiProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) findViewById(R.id.appBarTitle)).setText("Product Details");

        setUpViewPager();
    }

    private void setUpViewPager() {
        cpiProductImage = (CirclePageIndicator) findViewById(R.id.cpiProductImage);
        viewPagerProductDetail = (ViewPager) findViewById(R.id.viewPagerProductDetail);

        viewPagerProductDetail.setOffscreenPageLimit(2);

        Adapter adapter = new Adapter(getFragmentManager());

        ProductsImageFragment image1Fragment = new ProductsImageFragment();
        adapter.addFragment(image1Fragment, "Image 1");

        ProductsImageFragment image2Fragment = new ProductsImageFragment();
        adapter.addFragment(image2Fragment, "Image 2");

        ProductsImageFragment image3Fragment = new ProductsImageFragment();
        adapter.addFragment(image3Fragment, "Image 3");

        ProductsImageFragment image4Fragment = new ProductsImageFragment();
        adapter.addFragment(image4Fragment, "Image 4");

        ProductsImageFragment image5Fragment = new ProductsImageFragment();
        adapter.addFragment(image5Fragment, "Image 5");

        viewPagerProductDetail.setAdapter(adapter);
        cpiProductImage.setViewPager(viewPagerProductDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Adapter extends android.support.v13.app.FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
