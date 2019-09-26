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

package com.github.h01d.notes.data.local.database.entity;

import android.text.format.DateUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "notes")
@TypeConverters({Note.class})
public class Note implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String text;
    @NonNull
    private Date created;

    public Note(@NonNull String title, @NonNull String text, @NonNull Date created)
    {
        this.title = title;
        this.text = text;
        this.created = created;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @NonNull
    public String getTitle()
    {
        return title;
    }

    public void setTitle(@NonNull String title)
    {
        this.title = title;
    }

    @NonNull
    public String getText()
    {
        return text;
    }

    public void setText(@NonNull String text)
    {
        this.text = text;
    }

    @NonNull
    public Date getCreated()
    {
        return created;
    }

    public void setCreated(@NonNull Date created)
    {
        this.created = created;
    }

    @BindingAdapter(value = "date")
    public static void setDate(TextView textView, Date date)
    {
        try
        {
            textView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        }
        catch(Exception e)
        {
            textView.setText("");
        }
    }

    @TypeConverter
    public static Date timestampToDate(Long value)
    {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date)
    {
        return date == null ? null : date.getTime();
    }
}
