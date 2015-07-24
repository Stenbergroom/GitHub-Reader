package com.stenbergroom.githubreader.app.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.nispok.snackbar.Snackbar;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.UserActivity;
import com.stenbergroom.githubreader.app.entity.Repository;
import com.stenbergroom.githubreader.app.entity.User;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USERS_LOGIN = "login";
    public static final String COLUMN_USERS_COMPANY = "company";
    public static final String COLUMN_USERS_FOLLOWERS = "followers";
    public static final String COLUMN_USERS_FOLLOWING = "following";
    public static final String COLUMN_USERS_AVATAR_URL = "avatar";

    public static final String TABLE_REPOSITORIES = "Repositories";
    public static final String COLUMN_REPOSITORIES_NAME = "name";
    public static final String COLUMN_REPOSITORIES_LANGUAGE = "language";
    public static final String COLUMN_REPOSITORIES_FORKS = "forks";
    public static final String COLUMN_REPOSITORIES_WATCHERS = "watchers";

    private Context context;
    private DBHelper dbHelper;
    private ContentValues cv;
    private SQLiteDatabase database;

    public Database(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        cv = new ContentValues();
        database = dbHelper.getWritableDatabase();
    }

    public void pushInfo() throws IOException {

        cv.put(COLUMN_USERS_LOGIN, User.getGhUser().getLogin());
        cv.put(COLUMN_USERS_COMPANY, User.getGhUser().getCompany());
        cv.put(COLUMN_USERS_FOLLOWERS, User.getGhUser().getFollowersCount());
        cv.put(COLUMN_USERS_FOLLOWING, User.getGhUser().getFollowingCount());
        cv.put(COLUMN_USERS_AVATAR_URL, User.getGhUser().getAvatarUrl());

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
            int avatar = c.getColumnIndex(COLUMN_USERS_AVATAR_URL);
            do {
                Log.d("TEST",
                        "ID = "+c.getInt(id)+", login = "+c.getString(login)+", company = "+c.getString(company)+", followers = "+c.getInt(followers)+", following = "+c.getInt(following)+", AvatarURL = "+c.getString(avatar));
            }while (c.moveToNext());
        } else {
            Log.d("TEST", "0 rows");
            c.close();
        }
        /**/
        //processRepositoriesInfo();
        new processRepositoriesInfoTask().execute();
    }

    public class processRepositoriesInfoTask extends AsyncTask<Void, Void, Void> {

        List<Repository> repositoryList = new ArrayList<Repository>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Map<String, GHRepository> repositories = User.getGhUser().getRepositories();
                for(String repoName : repositories.keySet()) {
                    GHRepository ghRepository = repositories.get(repoName);
                    repositoryList.add(new Repository(ghRepository.getName(), ghRepository.getLanguage(), ghRepository.getForks(), ghRepository.getWatchers()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Repository repo : repositoryList) {
                cv.clear();
                cv.put(COLUMN_REPOSITORIES_NAME, repo.getName());
                cv.put(COLUMN_REPOSITORIES_LANGUAGE, repo.getLanguage());
                cv.put(COLUMN_REPOSITORIES_FORKS, repo.getForks());
                cv.put(COLUMN_REPOSITORIES_WATCHERS, repo.getWatchers());
                database.insert(TABLE_REPOSITORIES, null, cv);
            }
                    Cursor c = database.query(TABLE_REPOSITORIES, null, null, null, null, null, null);
        if(c.moveToFirst()){
            int id = c.getColumnIndex("_id");
            int name = c.getColumnIndex(COLUMN_REPOSITORIES_NAME);
            int language = c.getColumnIndex(COLUMN_REPOSITORIES_LANGUAGE);
            int forks = c.getColumnIndex(COLUMN_REPOSITORIES_FORKS);
            int watchers = c.getColumnIndex(COLUMN_REPOSITORIES_WATCHERS);
            do {
                Log.d("TEST",
                        "ID = "+c.getInt(id)+", repo name = "+c.getString(name)+", language = "+c.getString(language)+", forks = "+c.getInt(forks)+", watchers = "+c.getInt(watchers));
            }while (c.moveToNext());
        } else {
            Log.d("TEST", "0 rows");
            c.close();
        }
            dbHelper.close();
            Snackbar.with(context)
                    .text(context.getString(R.string.data_saved_success))
                    .show((Activity) context);
        }
    }

//    public void processRepositoriesInfo() throws IOException {
//        final List<Repository> repositoryList = new ArrayList<Repository>();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, GHRepository> repositories = null;
//                try {
//                    repositories = User.getGhUser().getRepositories();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                for(String repoName : repositories.keySet()) {
//                    GHRepository ghRepository = repositories.get(repoName);
//                    repositoryList.add(new Repository(ghRepository.getName(), ghRepository.getLanguage(), ghRepository.getForks(), ghRepository.getWatchers()));
////                    cv.put(COLUMN_REPOSITORIES_NAME, ghRepository.getName());
////                    cv.put(COLUMN_REPOSITORIES_LANGUAGE, ghRepository.getLanguage());
////                    cv.put(COLUMN_REPOSITORIES_FORKS, ghRepository.getForks());
////                    cv.put(COLUMN_REPOSITORIES_WATCHERS, ghRepository.getWatchers());
////                    database.insert(TABLE_REPOSITORIES, null, cv);
//                }
//                pushRepositoriesInfo(repositoryList);
//            }
//        });
//        thread.start();
//    }
//
//    private void pushRepositoriesInfo(List<Repository> repositoryList) {
//        for (Repository repo : repositoryList) {
//            cv.clear();
//            cv.put(COLUMN_REPOSITORIES_NAME, repo.getName());
//            cv.put(COLUMN_REPOSITORIES_LANGUAGE, repo.getLanguage());
//            cv.put(COLUMN_REPOSITORIES_FORKS, repo.getForks());
//            cv.put(COLUMN_REPOSITORIES_WATCHERS, repo.getWatchers());
//            database.insert(TABLE_REPOSITORIES, null, cv);
//        }
//
//        /**/
//        Cursor c = database.query(TABLE_REPOSITORIES, null, null, null, null, null, null);
//        if(c.moveToFirst()){
//            int id = c.getColumnIndex("_id");
//            int name = c.getColumnIndex(COLUMN_REPOSITORIES_NAME);
//            int language = c.getColumnIndex(COLUMN_REPOSITORIES_LANGUAGE);
//            int forks = c.getColumnIndex(COLUMN_REPOSITORIES_FORKS);
//            int watchers = c.getColumnIndex(COLUMN_REPOSITORIES_WATCHERS);
//            do {
//                Log.d("TEST",
//                        "ID = "+c.getInt(id)+", repo name = "+c.getString(name)+", language = "+c.getString(language)+", forks = "+c.getInt(forks)+", watchers = "+c.getInt(watchers));
//            }while (c.moveToNext());
//        } else {
//            Log.d("TEST", "0 rows");
//            c.close();
//        }
//        /**/
//        dbHelper.close();
//    }
}
