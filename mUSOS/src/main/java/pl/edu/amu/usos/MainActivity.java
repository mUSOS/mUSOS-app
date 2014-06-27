package pl.edu.amu.usos;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.adapter.MenuAdapter;
import pl.edu.amu.usos.content.AppPreferences;
import pl.edu.amu.usos.content.Reminder;
import pl.edu.amu.usos.content.SingleCourse;
import pl.edu.amu.usos.content.SingleGroup;
import pl.edu.amu.usos.fragment.DashboardFragment;
import pl.edu.amu.usos.fragment.GroupFragment;
import pl.edu.amu.usos.fragment.MyPreferenceFragment;
import pl.edu.amu.usos.fragment.ReminderFragment;
import pl.edu.amu.usos.fragment.ScheduleFragment;
import pl.edu.amu.usos.fragment.SearchTeacherFragment;
import pl.edu.amu.usos.fragment.SubjectListFragment;
import pl.edu.amu.usos.fragment.WmiFragment;
import pl.edu.amu.usos.helper.IntentHelper;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
        SearchDialog.SearchDialogInputListener {

    public String mSearchQuery;
    private long mLastBackPress;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AppPreferences mAppPrefs;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer)
    ListView mDrawerList;

    public static MainActivity fromActivity(Activity activity) {
        return (MainActivity) activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.inject(this);

        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }

        mAppPrefs = new AppPreferences(this);

        if (!mAppPrefs.hasAccessToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.app_name);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        MenuAdapter adapter = new MenuAdapter(this);
        mDrawerList.setAdapter(adapter);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerList.setOnItemClickListener(this);

        if (savedInstanceState == null) {
            manageIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        manageIntent(intent);
    }

    private void manageIntent(Intent intent) {
        final String action = intent.getAction();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = getFrament(action);
        ft.replace(R.id.main_content, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        final int count = fm.getBackStackEntryCount();
        final boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (!isDrawerOpen && count == 0) {
            mDrawerLayout.openDrawer(mDrawerList);
        } else if (isDrawerOpen && count == 0) {
            if (System.currentTimeMillis() - mLastBackPress < 2000) {
                finish();
            } else {
                Toast.makeText(this, R.string.back_button_toast, Toast.LENGTH_SHORT).show();
                mLastBackPress = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    private Fragment getFrament(String action) {
        if (Intent.ACTION_MAIN.equals(action)
                || AppConsts.DASHBOARD_ACTION.equals(action)) {
            return DashboardFragment.newInstance();
        } else if (AppConsts.REMINDER_ACTION.equals(action)) {
            return ReminderFragment.newInstance();
        } else if (AppConsts.SCHEDULE_ACTION.equals(action)) {
            return ScheduleFragment.newInstance();
        } else if (AppConsts.SEARCH_ACTION.equals(action)) {
            return SearchTeacherFragment.newInstance(mSearchQuery);
        } else if (AppConsts.SUBJECT_LIST_ACTION.equals(action)) {
            return SubjectListFragment.newInstance();
        } else if (AppConsts.GROUPS_ACTION.equals(action)) {
            return GroupFragment.newInstance();
        } else if (AppConsts.WMI_ACTION.equals(action)) {
            return WmiFragment.newInstance();
        } else if (AppConsts.PREFERENCE_ACTION.equals(action)) {
            return MyPreferenceFragment.newInstance();
        }


        throw new RuntimeException("Unknown fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTokenValidation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void checkTokenValidation() {
        if (!mAppPrefs.hasAccessToken()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.action_search:
                    SearchDialog search = SearchDialog.newInstance();
                    search.show(getSupportFragmentManager(), "Szukaj");
                    return true;
                case R.id.action_settings:
                    startActivity(IntentHelper.getSettingsIntent());
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case MenuAdapter.DASHBOARD:
                Intent dashboard = IntentHelper.getDashboardIntent();
                startActivity(dashboard);
                break;
            case MenuAdapter.SCHEDULE:
                Intent schedule = IntentHelper.getScheduleIntent();
                startActivity(schedule);
                break;
            case MenuAdapter.REMINDER:
                Intent reminder = IntentHelper.getReminderIntent();
                startActivity(reminder);
                break;
            case MenuAdapter.SUBJECTS:
                Intent subjects = IntentHelper.getSubjectListIntent();
                startActivity(subjects);
                break;
            case MenuAdapter.GROUPS:
                Intent groups = IntentHelper.getGroupsIntent();
                startActivity(groups);
                break;
            case MenuAdapter.SETTINGS:
                Intent settings = IntentHelper.getSettingsIntent();
                startActivity(settings);
                break;
            case MenuAdapter.LOGOUT:
                mAppPrefs.edit().clear().commit();
                clearDatabase();
                checkTokenValidation();
                break;
            default:
                break;

        }
        mDrawerLayout.closeDrawers();
    }

    private void clearDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Reminder.deleteAll(Reminder.class);
                SingleCourse.deleteAll(SingleCourse.class);
                SingleGroup.deleteAll(SingleGroup.class);
            }
        }).start();
    }

    @Override
    public void onFinishEditSearchDialog(String inputText) {
        mSearchQuery = inputText;
        Intent search = IntentHelper.getSearchIntent();
        startActivity(search);
    }

}
