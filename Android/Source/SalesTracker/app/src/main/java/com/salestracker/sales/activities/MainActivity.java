package com.salestracker.sales.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.salestracker.sales.R;
import com.salestracker.sales.application.AppController;
import com.salestracker.sales.fragments.BusinessesListFragment;
import com.salestracker.sales.fragments.GoalListFragment;
import com.salestracker.sales.fragments.OrdersHistoryFragment;
import com.salestracker.sales.fragments.ProductsListFragment;
import com.salestracker.sales.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    // this handles current menu selection id
    private int mCurrentSelection;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        navigationView.getHeaderView(0).findViewById(R.id.viewNavHeaderRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        openHomeFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
            mCurrentSelection = R.id.nav_home;
            navigationView.setCheckedItem(mCurrentSelection);
            /*drawer.setSelection(0, false);*/

            //drawer.setSelection(0); // this selects home in drawer whenever we return to home.
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        drawer.closeDrawer(GravityCompat.START);

        if (mCurrentSelection == id)
            return true;

        /**
         * Mind to check order of the items added into the drawer.
         * The same order has been used here,
         */
        switch (id) {
            case R.id.nav_home:
                getFragmentManager().popBackStack();
                break;
            case R.id.nav_products:
                openFragment(new ProductsListFragment(), "Products");
                break;
            case R.id.nav_businesses:
                openFragment(new BusinessesListFragment(), "Businesses");
                break;
            case R.id.nav_orders:
                openFragment(new OrdersHistoryFragment(), "Orders");
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;
            default:
                break;
        }

        int logoutId = R.id.nav_logout;

        if (id != logoutId) {
            mCurrentSelection = id;
        }
        navigationView.setCheckedItem(mCurrentSelection);
        return false;
    }

    private void openHomeFragment() {
        mCurrentSelection = R.id.nav_home;
        Fragment fragment = new GoalListFragment();
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        String backStackName = fragment.getClass().getName();
        if (fragmentManager.getBackStackEntryCount() > 1) {
//            if (backStackName.equals(fragmentManager.getBackStackEntryAt(1).getName())) {
//                return;
//            }
            fragmentManager.popBackStackImmediate();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    private void showLogoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Add the buttons
        builder.setPositiveButton(R.string.label_logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                // logout User
                logoutUser();
            }
        });
        builder.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        builder.setTitle(R.string.logout_confirmation);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void logoutUser() {
        PreferenceUtils.getInstance(AppController.getInstance()).setIsUserLoggedIn(false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
