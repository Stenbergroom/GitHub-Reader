package com.stenbergroom.githubreader.app.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.IconicsTextView;
import com.pkmmte.view.CircularImageView;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.activity.UserActivity;
import com.stenbergroom.githubreader.app.entity.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UserContentHelper {

    private Bitmap bitmap;
    private String usernameCompany, followersCount, followingCount;
    private UserActivity activity;

    public UserContentHelper(UserActivity activity) {
        this.activity = activity;
    }

    public void runTask() {
        UserContentHelperTask userContentHelperTask = new UserContentHelperTask();
        userContentHelperTask.execute();
    }

    public class UserContentHelperTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL avatarUrl = new URL(User.getGhUser().getAvatarUrl());
                bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
                if (!(User.getGhUser().getCompany() == null) && !User.getGhUser().getCompany().equals("")) {
                    usernameCompany = User.getGhUser().getLogin() + ", " + User.getGhUser().getCompany();
                } else {
                    usernameCompany = User.getGhUser().getLogin();
                }
                followingCount = String.valueOf(User.getGhUser().getFollowingCount());
                followersCount = String.valueOf(User.getGhUser().getFollowersCount());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CircularImageView imageAvatar = (CircularImageView) activity.findViewById(R.id.image_avatar);
            imageAvatar.setImageBitmap(bitmap);
            IconicsTextView tvUsernameCompany = (IconicsTextView) activity.findViewById(R.id.tv_username_company);
            tvUsernameCompany.setText(usernameCompany);
            IconicsTextView tvFollowersCount = (IconicsTextView) activity.findViewById(R.id.tv_followers_counts);
            tvFollowersCount.setText(followersCount);
            IconicsTextView tvFollowingCount = (IconicsTextView) activity.findViewById(R.id.tv_following_counts);
            tvFollowingCount.setText(followingCount);
        }
    }
}
