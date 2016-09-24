package com.aedvalson.classtracker;

/**
 * Created by a_edv on 9/22/2016.
 */
public class _Assessment {
    private long assessmentId;
    public String code;
    public String name;
    public long courseId;
    public String description;
    public String dateTime;

    public _Assessment(long id) {
        assessmentId = id;
    }
}
