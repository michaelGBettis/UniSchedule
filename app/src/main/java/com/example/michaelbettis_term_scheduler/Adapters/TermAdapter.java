package com.example.michaelbettis_term_scheduler.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.Converters;
import com.example.michaelbettis_term_scheduler.R;

import java.util.ArrayList;
import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> implements Filterable {
    private List<TermEntity> terms = new ArrayList<>();
    private List<TermEntity> termsFull = new ArrayList<>();
    private onItemClickListener listener;


    //sets the recycler view layout
    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    //passes the values of the terms to the view holder
    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {

        TermEntity currentTerm = terms.get(position);
        holder.textViewTitle.setText(currentTerm.getTerm_name());
        holder.textViewStartDate.setText(Converters.formatter(currentTerm.getStart_date()));
        holder.textViewEndDate.setText(Converters.formatter(currentTerm.getEnd_date()));

    }

    //sets the number of items to display in the recycler view
    @Override
    public int getItemCount() {
        return terms.size();
    }

    //passes the list of terms to recycler view
    public void setTerms(List<TermEntity> terms) {
        this.terms = terms;
        termsFull = new ArrayList<>(terms);
        notifyDataSetChanged();
    }

    public TermEntity getTermAt(int position) {
        return terms.get(position);
    }

    @Override
    public Filter getFilter() {
        return termFilter;
    }

    private final Filter termFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TermEntity> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(termsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (TermEntity item : termsFull) {
                    if (item.getTerm_name().toLowerCase().contains(filterPattern)) {
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
            terms.clear();
            terms.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class TermHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewStartDate;
        private final TextView textViewEndDate;


        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewStartDate = itemView.findViewById(R.id.text_view_start_date);
            textViewEndDate = itemView.findViewById(R.id.text_view_end_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(terms.get(position));
                    }
                }
            });
        }
    }

    //onClickListener for the term items
    public interface onItemClickListener {
        void onItemClick(TermEntity term);
    }

    //sets the onClickListener
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
