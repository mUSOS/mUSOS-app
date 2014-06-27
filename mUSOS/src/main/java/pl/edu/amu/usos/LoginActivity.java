package pl.edu.amu.usos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.edu.amu.usos.adapter.UniversityAdapter;
import pl.edu.amu.usos.api.LoginTask;
import pl.edu.amu.usos.api.model.University;
import pl.edu.amu.usos.content.AppPreferences;

public class LoginActivity extends FragmentActivity {

    @OnClick(R.id.login_btn)
    void onLoginClick() {
        int position = mSpinner.getSelectedItemPosition();
        University university = (University) mAdapter.getItem(position);
        mAppPreferences.edit().setUniversityId(university.id).commit();
        new LoginTask(this).execute();
    }

    @InjectView(R.id.university_spinner)
    Spinner mSpinner;

    private UniversityAdapter mAdapter;
    private AppPreferences mAppPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);

        mAppPreferences = new AppPreferences(this);
        mAdapter = new UniversityAdapter(this);
        mSpinner.setAdapter(mAdapter);
    }

}
