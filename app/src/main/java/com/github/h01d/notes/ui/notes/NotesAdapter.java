/*
 * Copyright (C) 2019 Raf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.h01d.notes.ui.notes;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.h01d.notes.R;
import com.github.h01d.notes.data.local.database.entity.Note;
import com.github.h01d.notes.databinding.NoteItemBinding;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> implements Filterable
{
    private List<Note> data;
    private List<Note> filteredData;

    private NotesAdapterListener listener;

    public NotesAdapter(NotesAdapterListener listener)
    {
        this.listener = listener;

        data = new ArrayList<>();
        filteredData = new ArrayList<>();
    }

    public void setData(List<Note> notes)
    {
        data.clear();
        filteredData.clear();

        data.addAll(notes);
        filteredData.addAll(notes);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        NoteItemBinding binding = NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new NotesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position)
    {
        holder.bind(filteredData.get(position));
    }

    @Override
    public int getItemCount()
    {
        return filteredData.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                List<Note> tmp = new ArrayList<>();

                if(constraint == null || constraint.length() == 0)
                {
                    tmp.addAll(data);
                }
                else
                {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for(Note item : data)
                    {
                        if(item.getTitle().toLowerCase().contains(filterPattern) || item.getText().toLowerCase().contains(filterPattern))
                        {
                            tmp.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = tmp;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                filteredData.clear();
                filteredData.addAll((List) results.values);

                notifyDataSetChanged();
            }
        };
    }

    class NotesViewHolder extends RecyclerView.ViewHolder
    {
        private final NoteItemBinding binding;

        NotesViewHolder(@NonNull NoteItemBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(Note item)
        {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getNote()));
            binding.setNote(item);
            binding.iNoteLinear.setBackgroundColor(binding.getRoot().getContext().getResources().getIntArray(R.array.colors)[item.getId() % 10]);
            binding.executePendingBindings();
        }
    }

    interface NotesAdapterListener
    {
        void onClicked(Note note);
    }
}