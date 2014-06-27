package pl.edu.amu.usos.content;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleGroup extends SugarRecord<SingleGroup> {

    public String groupName;
    public String groupType;
    public String students;

    public SingleGroup(Context context) {
        super(context);
    }

    public SingleGroup(Context context, String groupName, String groupType,
                       String students) {
        super(context);
        this.groupName = groupName;
        this.groupType = groupType;
        this.students = students;
    }

    public List<String> getStudents() {
        return Arrays.asList(this.students.split(";"));
    }

    public String getType() {
        return "(" + groupType + ")";
    }
}
