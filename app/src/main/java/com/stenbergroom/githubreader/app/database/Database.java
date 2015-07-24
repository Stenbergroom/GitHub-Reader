package com.stenbergroom.githubreader.app.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import com.nispok.snackbar.Snackbar;
import com.stenbergroom.githubreader.app.R;
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

    public void savingDataToDatabase() throws IOException {
        cv.put(COLUMN_USERS_LOGIN, User.getGhUser().getLogin());
        cv.put(COLUMN_USERS_COMPANY, User.getGhUser().getCompany());
        cv.put(COLUMN_USERS_FOLLOWERS, User.getGhUser().getFollowersCount());
        cv.put(COLUMN_USERS_FOLLOWING, User.getGhUser().getFollowingCount());
        cv.put(COLUMN_USERS_AVATAR_URL, User.getGhUser().getAvatarUrl());

        database.insert(TABLE_USERS, null, cv);

        new savingRepositoriesToDatabaseTask().execute();
    }

    public class savingRepositoriesToDatabaseTask extends AsyncTask<Void, Void, Void> {

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
            dbHelper.close();

            Snackbar.with(context)
                    .text(context.getString(R.string.data_saved_success))
                    .show((Activity) context);
        }
    }
}
