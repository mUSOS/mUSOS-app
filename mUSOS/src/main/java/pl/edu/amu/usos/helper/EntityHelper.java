package pl.edu.amu.usos.helper;

import android.content.Context;

import java.util.Calendar;

import pl.edu.amu.usos.api.model.Course;
import pl.edu.amu.usos.content.SingleCourse;

public class EntityHelper {

    public static SingleCourse getCourseModel(Context context, Course course) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(course.startTime);
        int mode = getMode(course.frequency);
        return new SingleCourse(context, course.getCourseName(), calendar.get(Calendar.DAY_OF_WEEK),
                mode, course.startTime.getTime(), course.endTime.getTime(),
                course.courseId, course.buildingName.pl,
                course.buildingId, course.roomNumber, course.roomId);
    }

    public static int getMode(String frequency) {
        if ("every_fortnight_odd".equals(frequency)) {
            return SingleCourse.EVERY_ODD_WEEK;
        } else if ("every_fortnight_even".equals(frequency)) {
            return SingleCourse.EVERY_EVEN_WEEK;
        } else {
            return SingleCourse.EVERY_WEEK;
        }
    }

    public static String getFrequencyString(int mode) {
        switch (mode) {
            case SingleCourse.EVERY_ODD_WEEK:
                return "Tygodnie nieparzyste";
            case SingleCourse.EVERY_EVEN_WEEK:
                return "Tygodnie parzyste";
            default:
                return "Co tydzień";
        }
    }
}
