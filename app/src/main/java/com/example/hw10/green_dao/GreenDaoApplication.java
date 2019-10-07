package com.example.hw10.green_dao;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.hw10.model.DaoMaster;
import com.example.hw10.model.DaoSession;

public class GreenDaoApplication extends Application {
    private static GreenDaoApplication sApplication;
    private DaoSession mDaoSession;

    public static GreenDaoApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoOpenHelper daoOpenHelper = new GreenDaoOpenHelper(this, "greenDao");
        SQLiteDatabase database = daoOpenHelper.getWritableDatabase();
        mDaoSession = new DaoMaster(database).newSession();
        sApplication = this;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}

