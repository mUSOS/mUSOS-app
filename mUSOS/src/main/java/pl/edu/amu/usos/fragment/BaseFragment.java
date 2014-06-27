package pl.edu.amu.usos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.scribe.model.Token;

import pl.edu.amu.usos.LoginActivity;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.AppPreferences;

public class BaseFragment extends Fragment {

    protected AppPreferences mAppPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppPrefs = new AppPreferences(getActivity());
    }

    public void checkForToken() {
        Token token = mAppPrefs.getAccessToken();
        if (token == null) {
            Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
