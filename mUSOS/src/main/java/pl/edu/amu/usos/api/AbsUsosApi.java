package pl.edu.amu.usos.api;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.edu.amu.usos.adapter.UniversityAdapter;
import pl.edu.amu.usos.content.AppPreferences;

public abstract class AbsUsosApi {

    protected OAuthService mService;
    protected Token mToken;
    protected AppPreferences mAppPreferences;
    private Gson mGson;

    private DateFormat mDateFormat;

    public AbsUsosApi(Context context) {
        mAppPreferences = new AppPreferences(context);
        int id = mAppPreferences.getUniversityId();
        mService = OAuthHelper.getService(UniversityAdapter.getUniversity(id));
        mToken = mAppPreferences.getAccessToken();
        initGson();
    }

    private void initGson() {
        mGson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public Response execute(OAuthRequest request) {
        mService.signRequest(mToken, request);
        Response response = request.send();

        if (response == null || response.getCode() == HttpStatus.SC_UNAUTHORIZED) {
            mAppPreferences.edit().setAccessToken(null).commit();
            return null;
        }

        return response;
    }

    public Gson getGson() {
        return mGson;
    }

    public DateFormat getDateFormat() {
        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        return mDateFormat;
    }
}
