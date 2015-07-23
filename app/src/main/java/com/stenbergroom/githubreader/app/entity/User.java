package com.stenbergroom.githubreader.app.entity;

import org.kohsuke.github.GHUser;

public class User {

    private static GHUser ghUser;

    public User(GHUser ghUser) {
        User.ghUser = ghUser;
    }

    public static GHUser getGhUser() {
        return ghUser;
    }

    public static void setGhUser(GHUser ghUser) {
        User.ghUser = ghUser;
    }
}
