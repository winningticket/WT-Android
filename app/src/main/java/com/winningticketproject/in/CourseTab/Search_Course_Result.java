package com.winningticketproject.in.CourseTab;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.winningticketproject.in.AppInfo.camparable_values;
import com.winningticketproject.in.R;

import java.util.Collections;

import static com.winningticketproject.in.CourseTab.Course_Search_Page.all_search_data;
import static com.winningticketproject.in.CourseTab.Course_Search_Page.private_search_data;
import static com.winningticketproject.in.CourseTab.Course_Search_Page.public_search_data;
import static com.winningticketproject.in.CourseTab.Course_Search_Page.semi_private_search_data;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Search_Course_Result extends AppCompatActivity implements View.OnClickListener {

    TextView all_text,public_text,private_text,semi_private_text;
    RecyclerView all_map_data;
    TextView course_search_result_not_found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__course__result);

        all_text = findViewById(R.id.all_text);
        public_text = findViewById(R.id.public_text);
        private_text = findViewById(R.id.private_text);
        semi_private_text = findViewById(R.id.semi_private_text);

        all_text.setTypeface(medium);
        public_text.setTypeface(medium);
        private_text.setTypeface(medium);
        semi_private_text.setTypeface(medium);

        course_search_result_not_found = findViewById(R.id.course_search_result_not_found);
        course_search_result_not_found.setTypeface(regular);

        TextView search_title = findViewById(R.id.search_title);
        search_title.setTypeface(regular);

        all_map_data = findViewById(R.id.all_map_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Search_Course_Result.this, LinearLayoutManager.VERTICAL, false);
        all_map_data.setLayoutManager(layoutManager);

        Collections.sort(all_search_data, new camparable_values());

        if (all_search_data.size()>0) {
            Course_Search_adapter all_records = new Course_Search_adapter(Search_Course_Result.this, all_search_data);
            all_map_data.setAdapter(all_records);
            all_records.notifyDataSetChanged();
            all_map_data.destroyDrawingCache();
            all_map_data.setVisibility(View.VISIBLE);

        }else {
            course_search_result_not_found.setVisibility(View.VISIBLE);
        }

        all_text.setTextColor(getResources().getColor(R.color.colorwhite));


        all_text.setOnClickListener(this);
        public_text.setOnClickListener(this);
        private_text.setOnClickListener(this);
        semi_private_text.setOnClickListener(this);

        ImageButton back_to_course_page = findViewById(R.id.back_to_course_page);
        back_to_course_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.all_text:

                Collections.sort(all_search_data, new camparable_values());

                all_text.setTextColor(getResources().getColor(R.color.colorwhite));
                all_text.setBackground(getResources().getDrawable(R.drawable.new_selected_bottom_bg));

                public_text.setBackground(null);
                private_text.setBackground(null);
                semi_private_text.setBackground(null);
                public_text.setTextColor(getResources().getColor(R.color.colorblack));
                private_text.setTextColor(getResources().getColor(R.color.colorblack));
                semi_private_text.setTextColor(getResources().getColor(R.color.colorblack));

                if (all_search_data.size()>0) {
                    Course_Search_adapter all_records = new Course_Search_adapter(Search_Course_Result.this,all_search_data);
                    all_map_data.setAdapter(all_records);
                    all_records.notifyDataSetChanged();
                    all_map_data.destroyDrawingCache();
                    all_map_data.setVisibility(View.VISIBLE);
                    course_search_result_not_found.setVisibility(View.GONE);
                }else {
                    all_map_data.setVisibility(View.GONE);
                    course_search_result_not_found.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.public_text:

                public_text.setTextColor(getResources().getColor(R.color.colorwhite));
                public_text.setBackground(getResources().getDrawable(R.drawable.new_selected_bottom_bg));

                all_text.setBackground(null);
                private_text.setBackground(null);
                semi_private_text.setBackground(null);

                all_text.setTextColor(getResources().getColor(R.color.colorblack));
                private_text.setTextColor(getResources().getColor(R.color.colorblack));
                semi_private_text.setTextColor(getResources().getColor(R.color.colorblack));

                Collections.sort(public_search_data, new camparable_values());

                if (public_search_data.size()>0) {
                    Course_Search_adapter public_dataaa = new Course_Search_adapter(Search_Course_Result.this,public_search_data);
                    all_map_data.setAdapter(public_dataaa);
                    public_dataaa.notifyDataSetChanged();
                    all_map_data.destroyDrawingCache();
                    all_map_data.setVisibility(View.VISIBLE);
                    course_search_result_not_found.setVisibility(View.GONE);
                }else {
                    all_map_data.setVisibility(View.GONE);
                    course_search_result_not_found.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.private_text:

                private_text.setTextColor(getResources().getColor(R.color.colorwhite));
                private_text.setBackground(getResources().getDrawable(R.drawable.new_selected_bottom_bg));

                public_text.setBackground(null);
                all_text.setBackground(null);
                semi_private_text.setBackground(null);

                public_text.setTextColor(getResources().getColor(R.color.colorblack));
                all_text.setTextColor(getResources().getColor(R.color.colorblack));
                semi_private_text.setTextColor(getResources().getColor(R.color.colorblack));

                Collections.sort(private_search_data, new camparable_values());

                if (private_search_data.size()>0) {
                    Course_Search_adapter private_dataaa = new Course_Search_adapter(Search_Course_Result.this,private_search_data);
                    all_map_data.setAdapter(private_dataaa);
                    private_dataaa.notifyDataSetChanged();
                    all_map_data.destroyDrawingCache();
                    all_map_data.setVisibility(View.VISIBLE);
                    course_search_result_not_found.setVisibility(View.GONE);
                }else {
                    all_map_data.setVisibility(View.GONE);
                    course_search_result_not_found.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.semi_private_text:

                semi_private_text.setTextColor(getResources().getColor(R.color.colorwhite));
                semi_private_text.setBackground(getResources().getDrawable(R.drawable.new_selected_bottom_bg));

                public_text.setBackground(null);
                all_text.setBackground(null);
                private_text.setBackground(null);
                public_text.setTextColor(getResources().getColor(R.color.colorblack));
                private_text.setTextColor(getResources().getColor(R.color.colorblack));
                all_text.setTextColor(getResources().getColor(R.color.colorblack));


                Collections.sort(semi_private_search_data, new camparable_values());

                if (semi_private_search_data.size()>0) {
                    Course_Search_adapter semi_private_dataaa = new Course_Search_adapter(Search_Course_Result.this,semi_private_search_data);
                    all_map_data.setAdapter(semi_private_dataaa);
                    semi_private_dataaa.notifyDataSetChanged();
                    all_map_data.destroyDrawingCache();
                    all_map_data.setVisibility(View.VISIBLE);
                    course_search_result_not_found.setVisibility(View.GONE);
                }else {
                    course_search_result_not_found.setVisibility(View.VISIBLE);
                    all_map_data.setVisibility(View.GONE);
                }



                break;

        }

    }
}
