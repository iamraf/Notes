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

package com.github.h01d.notes.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.github.h01d.notes.data.local.database.DatabaseManager;
import com.github.h01d.notes.data.local.database.dao.NotesDao;
import com.github.h01d.notes.data.local.database.entity.Note;

import java.util.List;

public class NotesRepository
{
    private NotesDao notesDao;
    private LiveData<List<Note>> notes;

    public NotesRepository(Application application)
    {
        DatabaseManager db = DatabaseManager.getDatabase(application);

        notesDao = db.getNotesDao();
        notes = notesDao.getNotes();
    }

    public LiveData<List<Note>> getNotes()
    {
        return notes;
    }

    public void insert(Note note)
    {
        new insertAsyncTask(notesDao).execute(note);
    }

    public void delete(Note note)
    {
        new deleteAsyncTask(notesDao).execute(note);
    }

    public void update(Note note)
    {
        new updateAsyncTask(notesDao).execute(note);
    }

    static class insertAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NotesDao notesDao;

        insertAsyncTask(NotesDao dao)
        {
            notesDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params)
        {
            notesDao.insert(params[0]);
            return null;
        }
    }

    static class deleteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NotesDao notesDao;

        deleteAsyncTask(NotesDao dao)
        {
            notesDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params)
        {
            notesDao.delete(params[0]);
            return null;
        }
    }

    static class updateAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NotesDao notesDao;

        updateAsyncTask(NotesDao dao)
        {
            notesDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params)
        {
            notesDao.update(params[0]);
            return null;
        }
    }
}
