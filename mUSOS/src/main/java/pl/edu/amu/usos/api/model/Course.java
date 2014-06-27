package pl.edu.amu.usos.api.model;

import java.util.Date;

public class Course {

    public Date startTime;
    public Date endTime;
    public Name name;
    public String courseId;
    public Name buildingName;
    public long buildingId;
    public String roomNumber;
    public long roomId;
    public String frequency;
    public String unitId;
    public String classtypeId;

    public String getCourseName() {
        return name.pl;
    }

    public String getCourseId() {
        return courseId + "_" + unitId + "_" + classtypeId;
    }

}
