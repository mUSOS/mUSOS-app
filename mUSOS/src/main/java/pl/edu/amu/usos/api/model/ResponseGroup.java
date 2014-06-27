package pl.edu.amu.usos.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGroup {

    public static class Groups {
        @SerializedName("2014/SL")
        public List<GroupDetail> groupDetail;
    }

    public static class GroupDetail {
        public Name courseName;
        public Name classType;
        public List<Participant> participants;

        public String getName() {
            return courseName.pl;
        }

        public String getType() {
            return classType.pl;
        }
    }

    public static class Participant {
        public String firstName;
        public String lastName;
        public String id;
    }

    public Groups groups;
}
