package com.narmware.realpic.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.narmware.realpic.pojo.LoginData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    LoginData loginData;
  /*  List<Friends> friendsList;
    Friends friends;
    */
    //QuizPojo quesDetails;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void setUserLogin(String user_name,String user_email,String profile_pic) {

        ContentValues values = new ContentValues();
        values.put(Support.USER_NAME,user_name);
        values.put(Support.USER_EMAIL,user_email);
        values.put(Support.USER_PROFILE_PIC,profile_pic);

        database.insert("login", null, values);
        //database.close();
    }

    public LoginData getUserLogin() {
        Cursor cursor = database.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int user_id=cursor.getInt(0);
            String  user_name=cursor.getString(1);
            String user_email=cursor.getString(2);
            String user_profile_pic=cursor.getString(3);

            if(user_id != -1)
            {
                loginData = new LoginData(user_name,user_email,user_profile_pic);
            }

            cursor.moveToNext();
        }
        cursor.close();
        return loginData;
    }
   /* public List<Image> getAllDetails() {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
             long id= Long.parseLong(cursor.getString(0));
             String name=cursor.getString(1);
             String path=cursor.getString(2);
             boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            Image quesDetails = new Image(id,name,path,isSelected,album,height,width);
                quesAnsList.add(quesDetails);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public List<Image> getSelectedImages(String albumName) {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image where isSelected = 'true' and album = '"+ albumName+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            long id= Long.parseLong(cursor.getString(0));
            String name=cursor.getString(1);
            String path=cursor.getString(2);
            boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            if(cursor.getString(0)!="id") {
                Image quesDetails = new Image(id, name, path, isSelected, album, height, width);
                quesAnsList.add(quesDetails);
            }

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }*/


    /*public void UpdateCartFlag(int flag, int f_id) {
        ContentValues values = new ContentValues();
        values.put(Constants.FRND_CART_FLAG, flag);

        database.update("friend_profile", values, "f_server_id=" + f_id, null);
    }*/







    public void deleteSingleFriend(String f_server_id)
    {
        database.execSQL("delete from friend_profile where f_server_id = '"+f_server_id+"'");
    }

    public void deleteAll()
    {
        database.execSQL("delete from image");

    }


}