package pl.edu.amu.usos.loaders;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.amu.usos.api.StudentApi;
import pl.edu.amu.usos.api.model.Course;
import pl.edu.amu.usos.content.SingleCourse;
import pl.edu.amu.usos.helper.EntityHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class TimeTableTask extends AsyncTask<Void, Void, Void> {

    public interface OnTimeTableListener {

        public void onTimeTableLoaded();
    }

    private final Context mContext;
    private final StudentApi mStudentApi;
    private OnTimeTableListener mListener;

    public static final String[] FIELDS = new String[] {
            "start_time", "end_time", "name", "course_id",
            "building_name", "building_id", "room_number",
            "room_id", "frequency", "classtype_id", "unit_id"
    };

    public TimeTableTask(Context context, OnTimeTableListener listener) {
        mContext = context;
        mStudentApi = new StudentApi(context);
        mListener = checkNotNull(listener);
    }

    @Override
    protected Void doInBackground(Void... params) {
        SingleCourse.deleteAll(SingleCourse.class);

        Map<String, Course> records = new HashMap<String, Course>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        List<Course> courses = fetchTimeTable(calendar.getTimeInMillis());
        for (Course course : courses) {
            records.put(course.getCourseId(), course);
        }

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        courses = fetchTimeTable(calendar.getTimeInMillis());
        for (Course course : courses) {
            records.put(course.getCourseId(), course);
        }

        for (String key : records.keySet()) {
            EntityHelper.getCourseModel(mContext, records.get(key)).save();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mListener.onTimeTableLoaded();
    }

    private List<Course> fetchTimeTable(long time) {
        OAuthRequest request = mStudentApi.getTimeTableRequest(new Date(time), 5, FIELDS);
        Response response = mStudentApi.execute(request);
        if (response != null && response.getCode() == HttpStatus.SC_OK) {
            Type listType = new TypeToken<List<Course>>() {}.getType();
            return mStudentApi.getGson().fromJson(response.getBody(), listType);
        }

        return null;
    }
}
