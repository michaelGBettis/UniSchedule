package com.example.michaelbettis_term_scheduler.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.R;

import java.util.ArrayList;
import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.utils.Helper;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {
    private List<AssessmentEntity> assessment = new ArrayList<>();
    private List<AssessmentEntity> assessmentFull = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        AssessmentEntity currentAssessment = assessment.get(position);
        holder.textViewTitle.setText(currentAssessment.getAssessment_name());
        holder.textViewDueDate.setText(Helper.formatter(currentAssessment.getDue_date()));
        holder.textViewType.setText(currentAssessment.getAssessment_type().substring(0, 1));

    }

    @Override
    public int getItemCount() {
        return assessment.size();
    }

    public void setAssessment(List<AssessmentEntity> assessments) {
        this.assessment = assessments;
        assessmentFull = new ArrayList<>(assessments);
        notifyDataSetChanged();
    }

    public AssessmentEntity getAssessmentAt(int position) {
        return assessment.get(position);
    }

    public Filter getFilter() {
        return assessmentFilter;
    }

    private final Filter assessmentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AssessmentEntity> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(assessmentFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AssessmentEntity item : assessmentFull) {
                    if (item.getAssessment_name().toLowerCase().contains(filterPattern)) {
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
            assessment.clear();
            assessment.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class AssessmentHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewDueDate;
        private final TextView textViewType;


        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDueDate = itemView.findViewById(R.id.text_view_due_date);
            textViewType = itemView.findViewById(R.id.text_view_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(assessment.get(position));
                    }
                }
            });

        }
    }

    //onClickListener for the assessment items
    public interface onItemClickListener {
        void onItemClick(AssessmentEntity assessment);
    }

    //sets the onClickListener
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
