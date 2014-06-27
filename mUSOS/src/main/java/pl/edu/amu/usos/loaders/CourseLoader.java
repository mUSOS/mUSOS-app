package pl.edu.amu.usos.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import pl.edu.amu.usos.api.StudentApi;
import pl.edu.amu.usos.api.model.Course;

public class CourseLoader extends AsyncTaskLoader<List<Course>> {

    private List<Course> mCourses;
    private final Date mDate;
    private StudentApi mStudentApi;

    public CourseLoader(Context context) {
        super(context);
        mStudentApi = new StudentApi(context);
        mDate = new Date();
    }

    public CourseLoader(Context context, Date date) {
        super(context);
        mStudentApi = new StudentApi(context);
        mDate = date;
    }

    @Override
    public List<Course> loadInBackground() {
        OAuthRequest request = mStudentApi.getTimeTableRequest(mDate);
        Response response = mStudentApi.execute(request);
        if (response != null && response.getCode() == HttpStatus.SC_OK) {
            Type listType = new TypeToken<List<Course>>() {}.getType();
            return mStudentApi.getGson().fromJson(response.getBody(), listType);
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        if (mCourses != null) {
            super.deliverResult(mCourses);
        }
    }

    @Override
    public void deliverResult(List<Course> data) {
        mCourses = data;
        super.deliverResult(data);
    }
}
