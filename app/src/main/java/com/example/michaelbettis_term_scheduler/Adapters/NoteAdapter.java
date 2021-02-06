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

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<NoteEntity> notes = new ArrayList<>();
    private List<NoteEntity> notesFull = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        NoteEntity currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getNote_name());

    }

    //sets the number of items to display in the recycler view
    @Override
    public int getItemCount() {
        return notes.size();
    }

    //passes the list of notes to recycler view
    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
        notesFull = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    public NoteEntity getNoteAt(int position) {
        return notes.get(position);
    }

    public Filter getFilter() {
        return noteFilter;
    }

    private final Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NoteEntity> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(notesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (NoteEntity item : notesFull) {
                    if (item.getNote_name().toLowerCase().contains(filterPattern)) {
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
            notes.clear();
            notes.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class NoteHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    //onClickListener for the note items
    public interface onItemClickListener {
        void onItemClick(NoteEntity note);
    }

    //sets the onClickListener
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
