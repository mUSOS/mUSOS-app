package pl.edu.amu.usos.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.util.List;

import pl.edu.amu.usos.api.StudentApi;
import pl.edu.amu.usos.api.model.ResponseTeacher;
import pl.edu.amu.usos.api.model.TeacherSearchItem;

public class SearchTeacherTask extends AsyncTaskLoader<List<TeacherSearchItem>> {

    private StudentApi mStudentApi;
    private String mQuery;

    public SearchTeacherTask(Context context, String query) {
        super(context);
        mStudentApi = new StudentApi(context);
        mQuery = query;
    }

    @Override
    public List<TeacherSearchItem> loadInBackground() {
        OAuthRequest request = mStudentApi.getFindingTeacherRequest(mQuery);

        try {
            final Response response = mStudentApi.execute(request);
            if (response != null && response.getCode() == HttpStatus.SC_OK) {
                ResponseTeacher teachers = mStudentApi.getGson().fromJson(response.getBody(),
                        ResponseTeacher.class);
                return teachers.items;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
