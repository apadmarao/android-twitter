package com.ani.twitter;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TwitterDatabase.NAME, version = TwitterDatabase.VERSION)
public class TwitterDatabase {

    public static final String NAME = "TwitterDatabase";

    public static final int VERSION = 1;

    private static TwitterDatabase INSTANCE;

    private TwitterDatabase() {
    }

    public synchronized static TwitterDatabase getInstance() {
        if (INSTANCE == null) {
            return new TwitterDatabase();
        } else {
            return INSTANCE;
        }
    }
}
