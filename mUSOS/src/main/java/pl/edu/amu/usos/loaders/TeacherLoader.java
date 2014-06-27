package pl.edu.amu.usos.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import pl.edu.amu.usos.api.StudentApi;
import pl.edu.amu.usos.api.model.Teacher;

public class TeacherLoader extends AsyncTaskLoader<Teacher> {

    private final StudentApi mStudentApi;
    private final String mId;

    public TeacherLoader(Context context, String id) {
        super(context);
        mStudentApi = new StudentApi(context);
        mId = id;
    }

    @Override
    public Teacher loadInBackground() {
        OAuthRequest request = mStudentApi.getTeacherDetailRequest(mId);
        Response response = mStudentApi.execute(request);
        if (response != null && response.getCode() == HttpStatus.SC_OK) {
            Teacher teacher = mStudentApi.getGson().fromJson(response.getBody(), Teacher.class);
            return teacher;
        }
        return null;
    }
}
