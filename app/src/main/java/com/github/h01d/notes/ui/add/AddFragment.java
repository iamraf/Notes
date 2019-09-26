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

package com.github.h01d.notes.ui.add;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.h01d.notes.R;
import com.github.h01d.notes.data.local.database.entity.Note;
import com.github.h01d.notes.databinding.FragmentAddBinding;
import com.github.h01d.notes.ui.notes.NotesViewModel;

import java.util.Calendar;

public class AddFragment extends Fragment
{
    private NotesViewModel mViewModel;
    private FragmentAddBinding mDataBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);

        mDataBinding.fAddTitle.requestFocus();

        mDataBinding.fAddFab.setOnClickListener(v ->
        {
            final String title = mDataBinding.fAddTitle.getText().toString();
            final String text = mDataBinding.fAddText.getText().toString();

            if(title.isEmpty() || text.isEmpty())
            {
                Toast.makeText(getContext(), "Title and text cannot be empty.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mViewModel.insert(new Note(title, text, Calendar.getInstance().getTime()));

                // Manually hiding keyboard

                if(getActivity() != null)
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(imm != null)
                    {
                        imm.hideSoftInputFromWindow(mDataBinding.getRoot().getWindowToken(), 0);
                    }
                }

                Navigation.findNavController(mDataBinding.getRoot()).popBackStack();
            }
        });

        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
    }
}
