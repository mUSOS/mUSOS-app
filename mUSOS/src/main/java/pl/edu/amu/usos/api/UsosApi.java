package pl.edu.amu.usos.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pl.edu.amu.usos.api.model.University;

public class UsosApi extends DefaultApi10a {

    private final Set<String> mScopes;
    private final University mUniversity;

    public UsosApi(University university) {
        mUniversity = university;
        mScopes = Collections.emptySet();
    }

    public UsosApi(University university, Set<String> scopes) {
        mUniversity = university;
        mScopes = scopes;
    }

    @Override
    public String getRequestTokenEndpoint() {
        String tokenUrl = getRequestTokenUrl();
        return mScopes.isEmpty() ? tokenUrl : tokenUrl + "?scopes=" + scopesAsString();
    }

    private String scopesAsString() {
        StringBuilder builder = new StringBuilder();
        for (String scope : mScopes) {
            builder.append("|" + scope);
        }
        return builder.substring(1);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return getBaseUrl() + "/services/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return getBaseUrl() + "/services/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    private String getBaseUrl() {
        return mUniversity.url;
    }

    private String getRequestTokenUrl() {
        return getBaseUrl() + "/services/oauth/request_token";
    }

    public static UsosApi withScopes(University university, String... scopes) {
        Set<String> scopeSet = new HashSet<String>(Arrays.asList(scopes));
        return new UsosApi(university, scopeSet);
    }
}
