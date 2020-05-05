package com.ynov.myconfinement.ui.agenda;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Pictures")
public class Picture {

    @DatabaseField(columnName = "pictureId", generatedId = true)
    private int pictureId;

    @DatabaseField
    private String uri;

    @DatabaseField
    private String date;

    @DatabaseField
    private String location;

    public Picture() { }

    public Picture(Uri uri, String date, String location) {
        this.uri = uri.toString();
        this.date = date;
        this.location = location;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(String path) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
