package com.stenbergroom.githubreader.app.entity;

public class Repository {

    private String name,language;
    private int forks, watchers;

    public Repository(String name, String language, int forks, int watchers) {
        this.name = name;
        this.language = language;
        this.forks = forks;
        this.watchers = watchers;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getForks() {
        return String.valueOf(forks);
    }

    public String getWatchers() {
        return String.valueOf(watchers);
    }
}
