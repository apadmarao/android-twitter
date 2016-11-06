package com.ani.twitter.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TwitterDatabase.NAME, version = TwitterDatabase.VERSION)
public class TwitterDatabase {

    public static final String NAME = "TwitterDatabase";

    public static final int VERSION = 1;


    private TwitterDatabase() {
    }
}
