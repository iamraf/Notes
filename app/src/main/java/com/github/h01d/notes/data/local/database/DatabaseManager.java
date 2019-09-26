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

package com.github.h01d.notes.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.h01d.notes.data.local.database.dao.NotesDao;
import com.github.h01d.notes.data.local.database.entity.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase
{
    public abstract NotesDao getNotesDao();

    private static volatile DatabaseManager instance;

    public static DatabaseManager getDatabase(final Context context)
    {
        if(instance == null)
        {
            synchronized(DatabaseManager.class)
            {
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, "notes_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
