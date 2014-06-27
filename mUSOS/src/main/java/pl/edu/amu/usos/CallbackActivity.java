package pl.edu.amu.usos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import pl.edu.amu.usos.adapter.UniversityAdapter;
import pl.edu.amu.usos.api.OAuthHelper;
import pl.edu.amu.usos.api.model.University;
import pl.edu.amu.usos.content.AppPreferences;

public class CallbackActivity extends Activity {

    private University mUniversity;

    private class AccessTokenTask extends AsyncTask<Void, Void, Token> {

        private final Token mToken;
        private final String mVerifier;

        public AccessTokenTask(Token token, String verifier) {
            mToken = token;
            mVerifier = verifier;
        }

        @Override
        protected Token doInBackground(Void... params) {
            Verifier verifier = new Verifier(mVerifier);
            OAuthService service = OAuthHelper.getService(mUniversity);
            final Token accessToken = service.getAccessToken(mToken, verifier);

            return accessToken;
        }

        @Override
        protected void onPostExecute(Token token) {
            super.onPostExecute(token);
            mAppPrefs.edit()
                    .setAccessToken(token)
                    .commit();

            startActivity(new Intent(AppConsts.DASHBOARD_ACTION));
            finish();
        }
    }

    private AppPreferences mAppPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callback_layout);
        mAppPrefs = new AppPreferences(this);
        int id = mAppPrefs.getUniversityId();
        mUniversity = UniversityAdapter.getUniversity(id);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        String token = uri.getQueryParameter("oauth_token");
        String verifier = uri.getQueryParameter("oauth_verifier");

        final String rawResponse = mAppPrefs.getTokenResponse(token);
        Token requestToken = new TokenExtractorImpl().extract(rawResponse);

        new AccessTokenTask(requestToken, verifier).execute();
    }
}
