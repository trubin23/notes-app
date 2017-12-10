package com.example.trubin23.myfirstapplication;

import android.app.Application;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

	private DBNotes mDBNotes;

	@Override
	public void onCreate() {
		super.onCreate();

		mDBNotes = new DBNotes(this);
	}

	DBNotes getDBNotes(){
		return mDBNotes;
	}
}
