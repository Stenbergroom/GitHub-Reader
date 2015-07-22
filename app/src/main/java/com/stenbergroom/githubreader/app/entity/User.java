package com.stenbergroom.githubreader.app.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.stenbergroom.githubreader.app.MainActivity;
import org.kohsuke.github.GHPersonSet;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class User {

    private final String login;
    private final String company;
    private final int followersCount;
    private final int followingCount;
    private URL avatarUrl;
    private Bitmap bitmap;

    public User(String login, String company, int followersCount, int followingCount) {
        this.login = login;
        this.company = company;
        this.followersCount = followersCount;
        this.followingCount = followingCount;

        try {
            avatarUrl = new URL(MainActivity.getUser().getAvatarUrl());
            bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User(String login, int followersCount, int followingCount) {
        this.login = login;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        company = null;
    }

    public String getLogin() {
        return login;
    }

    public String getCompany() {
        return company;
    }

    public String getFollowersCount() {
        return String.valueOf(followersCount);
    }

    public String getFollowingCount() {
        return String.valueOf(followingCount);
    }


}
