package com.ynov.myconfinement.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ynov.myconfinement.ui.agenda.Picture;
import com.ynov.myconfinement.ui.todo.Task;

import java.util.List;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Game.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Task.class );
            TableUtils.createTable( connectionSource, Picture.class );
            Log.i( "DATABASE", "onCreate invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable( connectionSource, Task.class, true );
            TableUtils.dropTable( connectionSource, Picture.class, true );
            onCreate( database, connectionSource);
            Log.i( "DATABASE", "onUpgrade invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade database", exception );
        }
    }

    public void insertTask(Task task) {
        try {
            Dao<Task, Integer> dao = getDao(Task.class);
            dao.create(task);
            Log.i( "DATABASE", "insertTask invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert task into database", exception );
        }
    }

    public void deleteTask(Task task) {
        try {
            Dao<Task, Integer> dao = getDao(Task.class);
            dao.delete(task);
            Log.i( "DATABASE", "deleteTask invoked" );
        } catch ( Exception exception ) {
            Log.e( "DATABASE", "Can't delete task in database", exception );
        }
    }

    public List<Task> getTasks() {
        try {
            Dao<Task, Integer> dao = getDao(Task.class);
            List<Task> tasks = dao.queryForAll();
            Log.i( "DATABASE", "getTasks invoked" );
            return tasks;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't get tasks from database", exception );
            return null;
        }
    }

    public void insertPicture(Picture picture) {
        try {
            Dao<Picture, Integer> dao = getDao(Picture.class);
            dao.create(picture);
            Log.i( "DATABASE", "insertPicture invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert picture into database", exception );
        }
    }

    public void deletePicture(Picture picture) {
        try {
            Dao<Picture, Integer> dao = getDao(Picture.class);
            dao.delete(picture);
            Log.i( "DATABASE", "deletePicture invoked" );
        } catch ( Exception exception ) {
            Log.e( "DATABASE", "Can't delete picture in database", exception );
        }
    }

    public List<Picture> getPictures() {
        try {
            Dao<Picture, Integer> dao = getDao(Picture.class);
            List<Picture> pictures = dao.queryForAll();
            Log.i( "DATABASE", "getPictures invoked" );
            return pictures;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't get pictures from database", exception );
            return null;
        }
    }

}
