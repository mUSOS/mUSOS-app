package pl.edu.amu.usos.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.common.base.Joiner;

import org.apache.http.HttpStatus;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.usos.api.StudentApi;
import pl.edu.amu.usos.api.model.ResponseGroup;
import pl.edu.amu.usos.content.SingleGroup;

public class GroupsLoader extends AsyncTaskLoader<List<SingleGroup>> {

    private Context mContext;
    private List<SingleGroup> mGroups;
    private StudentApi mStudentApi;

    public GroupsLoader(Context context) {
        super(context);
        mContext = context;
        mStudentApi = new StudentApi(context);
    }

    @Override
    public List<SingleGroup> loadInBackground() {
        List<SingleGroup> records = SingleGroup.listAll(SingleGroup.class);
        if (records.size() > 0) {
            return records;
        }

        final OAuthRequest request = mStudentApi.getGroupsRequest();
        final Response response = mStudentApi.execute(request);
        if (response != null && response.getCode() == HttpStatus.SC_OK) {
            final ResponseGroup responseGroup = mStudentApi.getGson().fromJson(response.getBody(), ResponseGroup.class);
            SingleGroup.deleteAll(SingleGroup.class);
            return getGroups(responseGroup.groups);
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        if (mGroups != null) {
            super.deliverResult(mGroups);
        }
    }

    @Override
    public void deliverResult(List<SingleGroup> data) {
        mGroups = data;
        super.deliverResult(data);
    }

    public List<SingleGroup> getGroups(ResponseGroup.Groups data) {
        List<SingleGroup> result = new ArrayList<SingleGroup>();

        for (ResponseGroup.GroupDetail group : data.groupDetail) {
            List<String> students = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();
            for (ResponseGroup.Participant student : group.participants) {
                students.add(student.firstName + " " + student.lastName);
            }

            Joiner joiner = Joiner.on(";");
            String name = joiner.join(students);

            SingleGroup single = new SingleGroup(mContext, group.getName(), group.getType(), name);
            single.save();
            result.add(single);
        }

        return result;
    }
}
