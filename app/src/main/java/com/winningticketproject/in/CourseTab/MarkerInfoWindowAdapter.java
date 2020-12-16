package com.winningticketproject.in.CourseTab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.winningticketproject.in.R;
import java.util.HashMap;
import java.util.Map;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    Map<Marker, MarkerInfo> mMarkerMap = new HashMap<>();
    public MarkerInfoWindowAdapter(Context context,Map<Marker, MarkerInfo> mMarkerMap) {
        this.context = context.getApplicationContext();
        this.mMarkerMap = mMarkerMap;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_marker_info_window, null);

        Typeface semibold = Typeface.createFromAsset(context.getAssets(), "Montserrat-SemiBold.ttf");
        Typeface regular = Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.ttf");

        MarkerInfo markerInfo = mMarkerMap.get(arg0);

        TextView tv_course_name =  v.findViewById(R.id.tv_course_name);
        TextView tv_distance =  v.findViewById(R.id.tv_distance);

        tv_distance.setTypeface(regular);
        tv_course_name.setTypeface(semibold);

        tv_distance.setText(markerInfo.Distance+" miles");
        tv_course_name.setText(markerInfo.Course_title);

        return v;
    }
}
