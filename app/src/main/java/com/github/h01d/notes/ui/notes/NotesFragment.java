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

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.github.h01d.notes.R;
import com.github.h01d.notes.data.local.database.entity.Note;
import com.github.h01d.notes.databinding.FragmentNotesBinding;

public class NotesFragment extends Fragment implements NotesAdapter.NotesAdapterListener
{
    private FragmentNotesBinding mDataBinding;

    private SearchView mSearchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);

        mDataBinding.fNotesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mDataBinding.fNotesRecycler.setHasFixedSize(true);
        mDataBinding.fNotesRecycler.setAdapter(new NotesAdapter(this));

        mDataBinding.fNotesFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_notesFragment_to_addFragment));

        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        NotesViewModel mViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        mViewModel.getNotes().observe(this, notes ->
        {
            if(notes.isEmpty())
            {
                mDataBinding.fNotesRecycler.setVisibility(View.GONE);
                mDataBinding.fNotesMessage.setVisibility(View.VISIBLE);
            }
            else
            {
                mDataBinding.fNotesRecycler.setVisibility(View.VISIBLE);
                mDataBinding.fNotesMessage.setVisibility(View.GONE);

                ((NotesAdapter) mDataBinding.fNotesRecycler.getAdapter()).setData(notes);
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if(!mSearchView.isIconified())
                {
                    mSearchView.setIconified(true);
                }
                else
                {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.notes_menu, menu);

        MenuItem item = menu.findItem(R.id.m_notes_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                ((NotesAdapter) mDataBinding.fNotesRecycler.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onClicked(Note note)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);

        Navigation.findNavController(mDataBinding.getRoot()).navigate(R.id.action_notesFragment_to_detailsFragment, bundle);
    }
}
