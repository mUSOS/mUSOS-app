package pl.edu.amu.usos.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;

import pl.edu.amu.usos.api.model.University;

public class OAuthHelper {

    public static OAuthService getService(University university) {
        return new ServiceBuilder()
                .provider(UsosApi.withScopes(university, "studies", "offline_access"))
                .apiKey(university.consumerKey)
                .apiSecret(university.consumerSecret)
                .signatureType(SignatureType.QueryString)
                .callback("musos://callback")
                .build();
    }
}
