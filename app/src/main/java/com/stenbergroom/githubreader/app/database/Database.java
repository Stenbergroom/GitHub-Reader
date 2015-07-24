package com.stenbergroom.githubreader.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.stenbergroom.githubreader.app.entity.User;
import com.stenbergroom.githubreader.app.helper.UserHelper;

import java.io.IOException;

public class Database {

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

//    private Context context;
    private DBHelper dbHelper;
    private ContentValues cv;
    private SQLiteDatabase database;

    public Database(Context context) {
//        this.context = context;
        dbHelper = new DBHelper(context);
        cv = new ContentValues();
        database = dbHelper.getWritableDatabase();
    }

    public void pushUserInfo() throws IOException {

        String userLogin = User.getGhUser().getLogin();
        String userCompany = User.getGhUser().getCompany();
        int userFollowers = User.getGhUser().getFollowersCount();
        int userFollowing = User.getGhUser().getFollowingCount();

        cv.put(COLUMN_USERS_LOGIN, userLogin);
        cv.put(COLUMN_USERS_COMPANY, userCompany);
        cv.put(COLUMN_USERS_FOLLOWERS, userFollowers);
        cv.put(COLUMN_USERS_FOLLOWING, userFollowing);

        //database.insert(TABLE_USERS, null, cv);

        /**/
        long rowID =  database.insert(TABLE_USERS, null, cv);
        Log.d("TEST", "----- row inserted, ID = "+rowID);
        Cursor c = database.query(TABLE_USERS, null, null, null, null, null, null);
        if(c.moveToFirst()){
            int id = c.getColumnIndex("_id");
            int login = c.getColumnIndex(COLUMN_USERS_LOGIN);
            int company = c.getColumnIndex(COLUMN_USERS_COMPANY);
            int followers = c.getColumnIndex(COLUMN_USERS_FOLLOWERS);
            int following = c.getColumnIndex(COLUMN_USERS_FOLLOWING);
            do {
                Log.d("TEST",
                        "ID = "+c.getInt(id)+", login = "+c.getString(login)+", company = "+c.getString(company)+", followers = "+c.getInt(followers)+", following = "+c.getInt(following));
            }while (c.moveToNext());
        } else {
            Log.d("TEST", "0 rows");
            c.close();
        }

        dbHelper.close();
    }
}
