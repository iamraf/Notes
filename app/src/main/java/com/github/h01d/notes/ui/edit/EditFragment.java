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

package com.github.h01d.notes.ui.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.h01d.notes.R;
import com.github.h01d.notes.data.local.database.entity.Note;
import com.github.h01d.notes.databinding.FragmentEditBinding;
import com.github.h01d.notes.ui.notes.NotesViewModel;

import java.util.Calendar;

public class EditFragment extends Fragment
{
    private NotesViewModel mViewModel;
    private FragmentEditBinding mDataBinding;

    private Note mNote;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        mNote = (Note) getArguments().getSerializable("note");

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false);

        mDataBinding.setNote(mNote);

        mDataBinding.fDetailFab.setOnClickListener(v ->
                new AlertDialog.Builder(getActivity())
                        .setTitle("Edit")
                        .setMessage("Are you sure you want to edit this note?")
                        .setPositiveButton("YES", (dialogInterface, i) ->
                        {
                            final String title = mDataBinding.fEditTitle.getText().toString();
                            final String text = mDataBinding.fEditText.getText().toString();

                            if(title.isEmpty() || text.isEmpty())
                            {
                                Toast.makeText(getContext(), "Title and text cannot be empty.", Toast.LENGTH_SHORT).show();
                            }
                            else if(title.equals(mNote.getTitle()) && text.equals(mNote.getText()))
                            {
                                Toast.makeText(getContext(), "No changes has been made.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mDataBinding.fEditTitle.clearFocus();
                                mDataBinding.fEditText.clearFocus();

                                mNote.setTitle(title);
                                mNote.setText(text);
                                mNote.setCreated(Calendar.getInstance().getTime());

                                mViewModel.update(mNote);

                                Toast.makeText(getContext(), "Updated successfully.", Toast.LENGTH_SHORT).show();

                                // Manually hiding keyboard

                                if(getActivity() != null)
                                {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                                    if(imm != null)
                                    {
                                        imm.hideSoftInputFromWindow(mDataBinding.getRoot().getWindowToken(), 0);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show());

        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.m_detail_delete)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("YES", (dialogInterface, i) ->
                    {
                        mViewModel.delete(mNote);

                        Navigation.findNavController(mDataBinding.getRoot()).popBackStack();
                    })
                    .setNegativeButton("NO", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
