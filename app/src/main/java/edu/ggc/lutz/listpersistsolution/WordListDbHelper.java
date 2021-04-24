package edu.ggc.lutz.listpersistsolution;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.ggc.lutz.listpersistsolution.WordListContract.WordListEntry;
import edu.ggc.lutz.listpersistsolution.data.WordContent;

public class WordListDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WordListPersist.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WordListEntry.TABLE_NAME + " (" +
             WordListEntry.COLUMN_NAME_CONTENT + " TEXT PRIMARY KEY)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordListEntry.TABLE_NAME;

    public WordListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<WordContent.WordItem> getWordItems(){
        ArrayList<WordContent.WordItem> rtn=new ArrayList<>();
        String sql="select * from wordlist";

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor= db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {

                rtn.add(new WordContent.WordItem(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return rtn;
    }

    public void updateDatabse(ArrayList<WordContent.WordItem> list){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="delete from wordlist";
        db.execSQL(sql);
        for(WordContent.WordItem i:list){
            sql="insert into wordlist values (\""+i.content+"\");";
            db.execSQL(sql);
        }
    }


}