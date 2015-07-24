package com.stenbergroom.githubreader.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GitHubReaderDatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USERS_LOGIN = "login";
    public static final String COLUMN_USERS_COMPANY = "company";
    public static final String COLUMN_USERS_FOLLOWERS = "followers";
    public static final String COLUMN_USERS_FOLLOWING = "following";

    public static final String TABLE_REPOSITORIES = "Repositories";
    public static final String COLUMN_REPOSITORIES_NAME = "name";
    public static final String COLUMN_REPOSITORIES_LANGUAGE = "language";
    public static final String COLUMN_REPOSITORIES_FORKS = "forks";
    public static final String COLUMN_REPOSITORIES_WATCHES = "watches";

    public static final String CREATE_TABLE_USERS = "create table "+TABLE_USERS+" ("+"_id integer primary key autoincrement, "
            +COLUMN_USERS_LOGIN+" text, "+COLUMN_USERS_COMPANY+" text, "+COLUMN_USERS_FOLLOWERS+" integer, "+COLUMN_USERS_FOLLOWING+");";

    public static final String CREATE_TABLE_REPOSITORIES = "create table "+TABLE_REPOSITORIES+" ("+"_id integer primary key autoincrement, "
            +COLUMN_REPOSITORIES_NAME+ " text, "+COLUMN_REPOSITORIES_LANGUAGE+" text, "+COLUMN_REPOSITORIES_FORKS+" integer, "+COLUMN_REPOSITORIES_WATCHES+");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_REPOSITORIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
