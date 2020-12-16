package com.winningticketproject.in.AppInfo;

import java.util.Comparator;

public class camparable_values implements Comparator<Course_Data>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Course_Data a, Course_Data b )
    {
        return (int) (Double.parseDouble(a.getDistance_mils()) - Double.parseDouble(b.getDistance_mils()));
    }
}
