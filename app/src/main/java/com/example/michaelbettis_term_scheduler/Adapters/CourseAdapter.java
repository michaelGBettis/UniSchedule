package com.example.michaelbettis_term_scheduler.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.Converters;
import com.example.michaelbettis_term_scheduler.R;

import java.util.ArrayList;
import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    private List<CourseEntity> courses = new ArrayList<>();
    private List<CourseEntity> coursesFull = new ArrayList<>();
    private onItemClickListener listener;

    //sets the recycler view layout
    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    //passes the values of the courses to the view holder
    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {

        CourseEntity currentCourse = courses.get(position);
        holder.textViewTitle.setText(currentCourse.getCourse_name());
        holder.textViewStartDate.setText(Converters.formatter(currentCourse.getStart_date()));
        holder.textViewEndDate.setText(Converters.formatter(currentCourse.getEnd_date()));
        holder.textViewStatus.setText(currentCourse.getCourse_status());

    }

    //sets the number of items to display in the recycler view
    @Override
    public int getItemCount() {
        return courses.size();
    }

    //passes the list of courses to recycler view
    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
        coursesFull = new ArrayList<>(courses);
        notifyDataSetChanged();
    }

    public CourseEntity getCourseAt(int position) {
        return courses.get(position);
    }


    public Filter getFilter() {
        return courseFilter;
    }

    private final Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CourseEntity> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(coursesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (CourseEntity item : coursesFull) {
                    if (item.getCourse_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            courses.clear();
            courses.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class CourseHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewStartDate;
        private final TextView textViewEndDate;
        private final TextView textViewStatus;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewStartDate = itemView.findViewById(R.id.text_view_start_date);
            textViewEndDate = itemView.findViewById(R.id.text_view_end_date);
            textViewStatus = itemView.findViewById(R.id.text_view_status_indicator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courses.get(position));
                    }
                }
            });
        }
    }

    //onClickListener for the course items
    public interface onItemClickListener {
        void onItemClick(CourseEntity course);
    }

    //sets the onClickListener
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
