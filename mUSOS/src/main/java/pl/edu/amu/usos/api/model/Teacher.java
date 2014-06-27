package pl.edu.amu.usos.api.model;

import java.util.List;

public class Teacher {

    public static class Position {
        public InnerPosition position;
    }

    public static class InnerPosition {
        public Name name;
        public String id;
    }

    public String firstName;
    public String lastName;
    public String room;
    public String id;
    public String profileUrl;
    public List<Position> employmentPositions;

}
