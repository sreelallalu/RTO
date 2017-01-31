package org.venture.rto.rto3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataB {
    public static final String KEY_ROWID ="_id";
    public static final String KEY_NAME ="p_name";
    public static final String KEY_PHN ="p_phn";

    private static final String DATABASE_NAME ="CourseGradedba";
    private static final String DATABASE_TABLE ="courseTabled";
    private static final int DATABASE_VERSION =1;
    private DbHelper myHelper;
    private final Context myContext;
    private SQLiteDatabase myDatabase;
    private static class DbHelper extends SQLiteOpenHelper{
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//TODO Auto-generated constructor stub
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL);"
            );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    public DataB(Context c){
        myContext = c;
    }
    public DataB open() throws SQLException{
        myHelper = new DbHelper(myContext);
        myDatabase = myHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        myHelper.close();
    }
    public long createEntry(String name ) {
// TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);



        return myDatabase.insert(DATABASE_TABLE, null, cv);
    }


    public String getName(int l) throws SQLException{
// TODO Auto-generated method stub
        String[] columns = new String[]{ KEY_ROWID, KEY_NAME};
        Cursor c = myDatabase.query(DATABASE_TABLE, columns, KEY_ROWID +"="+l,
                null, null, null, null);
        if (c != null){
            c.moveToFirst();
            String name = c.getString(1);
            return name;
        }
        return null;
    }



    public void updateEntry(long lRow, String mName) throws
            SQLException{
// TODO Auto-generated method stub
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_NAME, mName);



        myDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID+ "=" + lRow, null);
    }
    public void deleteEntry() throws SQLException{
// TODO Auto-generated method stub
        myDatabase.delete(DATABASE_TABLE, null,null);
    }
}