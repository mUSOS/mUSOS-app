package pl.edu.amu.usos.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.Token;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pl.edu.amu.usos.api.model.Course;

import static com.google.common.base.Preconditions.checkNotNull;

public class AppPreferences {

    private static final String PREFERENCE_NAME = "app_prefs";

    public static class Editor {

        private SharedPreferences mSharedPreferences;
        private final SharedPreferences.Editor mEditor;

        public Editor(SharedPreferences sharedPreferences) {
            mSharedPreferences = sharedPreferences;
            mEditor = sharedPreferences.edit();
        }

        public Editor setTokenResponse(Token token) {
            checkNotNull(token);
            mEditor.putString(token.getToken(), token.getRawResponse());
            return this;
        }

        public Editor removeTokenResponse(String token) {
            mEditor.remove(token);
            return this;
        }

        public Editor setAccessToken(Token token) {
            String rawResponse = token == null ? null : token.getRawResponse();
            mEditor.putString(ACCESS_TOKEN, rawResponse);
            return this;
        }

        public Editor setTimeTableSync(long time) {
            mEditor.putLong(TIME_TABLE_REFRESH, time);
            return this;
        }

        public Editor setGroupSync(long time) {
            mEditor.putLong(GROUPS_REFRESH, time);
            return this;
        }

        public Editor clear() {
            mEditor.clear();
            return this;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public Editor putCalendarId(String id) {
            final Set<String> ids = mSharedPreferences.getStringSet(CALENDAR_IDS, new HashSet<String>());
            if (!ids.contains(id)) {
                ids.add(id);
            }

            mSharedPreferences.edit().putStringSet(CALENDAR_IDS, ids).commit();
            return this;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public Editor removeCalendarId(String id) {
            final Set<String> ids = mSharedPreferences.getStringSet(CALENDAR_IDS, new HashSet<String>());
            if (ids.contains(id)) {
                ids.remove(id);
            }

            mSharedPreferences.edit().putStringSet(CALENDAR_IDS, ids).commit();
            return this;
        }

        public Editor setUniversityId(int id) {
            mEditor.putInt(UNIVERSITY_ID, id);
            return this;
        }

        public void commit() {
            mEditor.commit();
        }

        public Editor putCourse(Course course) {
            mEditor.putString(COURSE_NAME, course.getCourseName());
            mEditor.putLong(COURSE_START, course.startTime.getTime());
            mEditor.putLong(COURSE_END, course.endTime.getTime());
            return this;
        }
    }

    private static final String ACCESS_TOKEN = "access_token";
    private static final String TIME_TABLE_REFRESH = "time_table_refresh";
    private static final String GROUPS_REFRESH = "group_refresh";
    private static final String CALENDAR_IDS = "calendar_ids";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_START = "course_start";
    private static final String COURSE_END = "course_end";
    private static final String UNIVERSITY_ID = "university_id";

    private SharedPreferences mPreferences;

    public AppPreferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
    }

    public Editor edit() {
        return new Editor(mPreferences);
    }

    public Token getAccessToken() {
        String rawResponse = mPreferences.getString(ACCESS_TOKEN, null);
        if (rawResponse != null) {
            return new TokenExtractorImpl().extract(rawResponse);
        }

        return null;
    }

    public String getTokenResponse(String token) {
        return mPreferences.getString(token, null);
    }

    public boolean hasAccessToken() {
        return getAccessToken() != null;
    }

    public boolean needTimeTableRefresh() {
        long lastSync = mPreferences.getLong(TIME_TABLE_REFRESH, 0);
        long time = System.currentTimeMillis();
        return time - lastSync > 7 * 24 * 60 * 60 * 1000L;
    }

    public boolean needGroupRefresh() {
        long lastSync = mPreferences.getLong(GROUPS_REFRESH, 0);
        return System.currentTimeMillis() > lastSync;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getCalendarIds() {
        return mPreferences.getStringSet(CALENDAR_IDS, new HashSet<String>());
    }

    public boolean isIdEnable(String id) {
        final Set<String> ids = getCalendarIds();
        return ids.contains(id);
    }

    public boolean isCalendarEnable() {
        return getCalendarIds().size() > 0;
    }

    public boolean needCourseRefresh() {
        return System.currentTimeMillis() > mPreferences.getLong(COURSE_START, 0);
    }

    public String getCourseName() {
        return mPreferences.getString(COURSE_NAME, null);
    }

    public Date getCourseStart() {
        return new Date(mPreferences.getLong(COURSE_START, 0));
    }

    public Date getCourseEnd() {
        return new Date(mPreferences.getLong(COURSE_END, 0));
    }

    public int getUniversityId() {
        return mPreferences.getInt(UNIVERSITY_ID, 0);
    }
}
