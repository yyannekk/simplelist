package simplelist.trakks.net.simplelist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import simplelist.trakks.net.simplelist.model.ListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "simplelist.db";
    static final String TABLE_ITEMS = "items";
    static final String COL_ID = "id";
    static final String COL_TEXT = "text";
    static final String COL_SCHEDULE = "schedule";
    static final String COL_ARCHIVED = "archived";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                String.format("create table items (%s string primary key,%s text, %s date, %s date)", COL_ID, COL_TEXT, COL_SCHEDULE, COL_ARCHIVED)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL(String.format("DROP TABLE_ITEMS IF EXISTS %s", TABLE_ITEMS));
        onCreate(db);
    }

    private ContentValues createValues(String id, String text, Date schedule, Date archived)
    {
        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_TEXT, text);
        values.put(COL_ARCHIVED, dateFormat.format(archived));
        values.put(COL_SCHEDULE, dateFormat.format(schedule));
        return values;
    }

    public boolean create(String id, String text, Date schedule, Date archived)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = createValues(id, text, schedule, archived);
        db.insert(TABLE_ITEMS, null, v);
        return true;
    }

    public Cursor getData(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("select * from %s where id=%s", TABLE_ITEMS, id), null);
        return res;
    }

    public long numberOfRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_ITEMS);
    }

    public boolean update(String id, String text, Date schedule, Date archived)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = createValues(id, text, schedule, archived);
        db.update(TABLE_ITEMS, v, "id=?", new String[]{id});
        return true;
    }

    public int delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, "id=?", new String[]{id});
    }

    public List<ListItem> getAll()
    {
        ArrayList<ListItem> items = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("select * from %s", TABLE_ITEMS), null);
        res.moveToFirst();

        while (!res.isAfterLast())
        {
            Date scheduled = null;
            Date archived = null;
            try
            {
                String scheduleString = res.getString(res.getColumnIndex(COL_SCHEDULE));
                if (scheduleString != null)
                {
                    scheduled = dateFormat.parse(scheduleString);
                }
                String archivedString = res.getString(res.getColumnIndex(COL_ARCHIVED));
                if (archivedString != null)
                {
                    archived = dateFormat.parse(res.getString(res.getColumnIndex(COL_ARCHIVED)));
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            items.add(new ListItem(UUID.fromString(res.getString(res.getColumnIndex(COL_ID))), res.getString(res.getColumnIndex(COL_TEXT)), scheduled, archived));
            res.moveToNext();
        }
        res.close();
        return items;
    }
}
