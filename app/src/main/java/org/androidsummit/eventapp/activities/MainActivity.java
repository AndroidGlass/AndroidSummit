package org.androidsummit.eventapp.activities;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import org.androidsummit.eventapp.fragments.NavigationDrawerFragment;
import org.androidsummit.eventapp.R;
import org.androidsummit.eventapp.fragments.about.AboutFragment;
import org.androidsummit.eventapp.fragments.people.SpeakersFragment;
import org.androidsummit.eventapp.fragments.schedule.MySummitDayFragment;
import org.androidsummit.eventapp.fragments.schedule.SummitDayFragment;
import org.androidsummit.eventapp.helpers.person.DateHelper;
import org.androidsummit.eventapp.interfaces.FragmentCallbacks;

import java.util.Date;

/**
 * Holds the main fragment and is the entry point of the application.
 */
public class MainActivity extends AppCompatActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks, FragmentCallbacks {

    public static final Date EVENT_DATE;
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static int mCurrentFragmentIndex = -1;

    private static int mRequestFragmentIndex = 0;

    private View mAppBarShadow;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    static {
        EVENT_DATE = DateHelper.getFormattedFullDateAndTime("09", "30", "2015");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        if (savedInstanceState == null) {
            mTitle = getTitle();
            if (mCurrentFragmentIndex != -1) {
                swapFragments(mCurrentFragmentIndex);
            }
        }

        //Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private View getAppBarShadow() {
        if (mAppBarShadow == null) {
            mAppBarShadow = findViewById(R.id.shadow);
        }
        return mAppBarShadow;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mCurrentFragmentIndex == -1) {
            //Initialize current fragment
            mCurrentFragmentIndex = position;
            swapFragments(mCurrentFragmentIndex);
        }
        mRequestFragmentIndex = position;
    }

    @Override
    public void onNavigationDrawerClosed() {
        //Only swap if requested fragment is different than current fragment
        if (mRequestFragmentIndex != mCurrentFragmentIndex) {
            swapFragments(mRequestFragmentIndex);
            //Update current fragment
            mCurrentFragmentIndex = mRequestFragmentIndex;
        }
    }

    private void swapFragments(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = SummitDayFragment.newInstance(position + 1, EVENT_DATE.getTime());
                break;
            case 1:
                fragment = MySummitDayFragment.newInstance(position + 1, EVENT_DATE.getTime());
                break;
            case 2:
                fragment = SpeakersFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = AboutFragment.newInstance(position + 1);
                break;
        }
        swapFragment(fragmentManager, fragment);
    }

    private void swapFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        } else {
            Log.wtf(TAG, "Illegal index for fragment");
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void updateTitle(int sectionNumber) {
        onSectionAttached(sectionNumber);
    }

    @Override
    public void setToolbarShadow(boolean showShadow) {
        if (showShadow) {
            getAppBarShadow().setVisibility(View.VISIBLE);
        } else {
            getAppBarShadow().setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragmentIndex != 0) {
            mNavigationDrawerFragment.setSelected(R.id.schedule);
            mRequestFragmentIndex = 0;
            mCurrentFragmentIndex = 0;
            swapFragments(mCurrentFragmentIndex);
        } else {
            super.onBackPressed();
        }
    }
}
