package pl.edu.amu.usos.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.UniversityAdapter;
import pl.edu.amu.usos.api.model.University;
import pl.edu.amu.usos.content.AppPreferences;

public class LoginTask extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private AppPreferences mAppPrefs;
    private University mUniversity;

    public LoginTask(Context context) {
        mContext = context;
        mAppPrefs = new AppPreferences(context);
        mUniversity = UniversityAdapter.getUniversity(mAppPrefs.getUniversityId());
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            OAuthService service = OAuthHelper.getService(mUniversity);
            final Token requestToken = service.getRequestToken();
            final String authorizationUrl = service.getAuthorizationUrl(requestToken);

            mAppPrefs.edit()
                    .setTokenResponse(requestToken)
                    .commit();

            return authorizationUrl;
        } catch (Exception ignore) {
            // ignore
        }

        return null;
    }

    @Override
    protected void onPostExecute(String authorizationUrl) {
        super.onPostExecute(authorizationUrl);
        if (TextUtils.isEmpty(authorizationUrl)) {
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(authorizationUrl));
            mContext.startActivity(intent);
        }
    }
}
