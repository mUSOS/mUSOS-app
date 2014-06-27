package pl.edu.amu.usos.api;

import android.content.Context;
import android.net.Uri;

import com.google.common.base.Joiner;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.Date;

import pl.edu.amu.usos.adapter.UniversityAdapter;
import pl.edu.amu.usos.api.model.University;
import pl.edu.amu.usos.content.AppPreferences;

public class StudentApi extends AbsUsosApi {

    private University mUniversity;

    public StudentApi(Context context) {
        super(context);
        int id = new AppPreferences(context).getUniversityId();
        mUniversity = UniversityAdapter.getUniversity(id);
    }

    public static final String[] DEFAULT_FIELDS = {"start_time", "end_time", "name"};

    public static final String[] GROUP_FIELDS = {"course_unit_id", "group_number", "class_type",
            "class_type_id", "group_url", "course_id", "course_name", "course_homepage_url",
            "course_fac_id", "course_lang_id", "term_id", "lecturers", "participants"};

    public OAuthRequest getTimeTableRequest(Date startDate) {
        return getTimeTableRequest(startDate, 7, DEFAULT_FIELDS);
    }

    public OAuthRequest getTimeTableRequest(Date startDate, int days, String[] fields) {
        Joiner joiner = Joiner.on("|").skipNulls();
        final String joinedFields = joiner.join(fields);
        String formattedDate = getDateFormat().format(startDate);
        Uri uri = Uri.parse(mUniversity.getServiceUrl() + "/tt/user").buildUpon()
                .appendQueryParameter("start", formattedDate)
                .appendQueryParameter("days", String.valueOf(days))
                .appendQueryParameter("fields", joinedFields)
                .build();

        return new OAuthRequest(Verb.GET, uri.toString());
    }

    public OAuthRequest getGroupsRequest() {
        Joiner joiner = Joiner.on("|").skipNulls();
        final String joinedFields = joiner.join(GROUP_FIELDS);
        Uri uri = Uri.parse(mUniversity.getServiceUrl() + "/groups/participant").buildUpon()
                .appendQueryParameter("fields", joinedFields)
                .appendQueryParameter("active_terms", "true")
                .build();

        return new OAuthRequest(Verb.GET, uri.toString());
    }

    public OAuthRequest getGroupParticipantsRequest(String unitId, String groupNumber) {
        Uri uri = Uri.parse(mUniversity.getServiceUrl() + "/groups/group").buildUpon()
                .appendQueryParameter("fields", "participants")
                .appendQueryParameter("course_unit_id", unitId)
                .appendQueryParameter("group_number", groupNumber)
                .build();

        return new OAuthRequest(Verb.GET, uri.toString());
    }

    public OAuthRequest getFindingTeacherRequest(String query) {
        Uri uri = Uri.parse(mUniversity.getServiceUrl() + "/users/search2").buildUpon()
                .appendQueryParameter("among", "current_teachers|staff")
                .appendQueryParameter("lang", "pl")
                .appendQueryParameter("num", "15")
                .appendQueryParameter("query", query)
                .build();
        return new OAuthRequest(Verb.GET, uri.toString());
    }

    public OAuthRequest getTeacherDetailRequest(String id) {
        Uri uri = Uri.parse(mUniversity.getServiceUrl() + "/users/user").buildUpon()
                .appendQueryParameter("user_id", id)
                .appendQueryParameter("fields", "id|first_name|last_name|room|homepage_url|photo_urls" +
                        "|employment_positions|profile_url")
                .build();

        return new OAuthRequest(Verb.GET, uri.toString());
    }

}
