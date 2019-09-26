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

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.h01d.notes.data.NotesRepository;
import com.github.h01d.notes.data.local.database.entity.Note;

import java.util.List;

public class NotesViewModel extends AndroidViewModel
{
    private NotesRepository repository;

    private LiveData<List<Note>> notes;

    public NotesViewModel(Application application)
    {
        super(application);

        repository = new NotesRepository(application);
        notes = repository.getNotes();
    }

    public LiveData<List<Note>> getNotes()
    {
        return notes;
    }

    public void insert(Note note)
    {
        repository.insert(note);
    }

    public void delete(Note note)
    {
        repository.delete(note);
    }

    public void update(Note note)
    {
        repository.update(note);
    }
}
