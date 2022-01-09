import AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_DIRECTORY
import AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG
import AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE
import AttractionDbHelper.AttractionEntry.SQL_CREATE_ENTRIES
import AttractionDbHelper.AttractionEntry.SQL_DELETE_ENTRIES
import AttractionDbHelper.AttractionEntry.TABLE_NAME
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class AttractionDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object AttractionEntry: BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DEFAULT_IMG = "defaultDirectory"
        const val COLUMN_NAME_CUSTOM_IMG_DIRECTORY = "customDirectory"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_DEFAULT_IMG TEXT, $COLUMN_NAME_CUSTOM_IMG_DIRECTORY TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        var item = ContentValues()
        item.put(COLUMN_NAME_TITLE, "Avenue of stars")
        item.put(COLUMN_NAME_DEFAULT_IMG, "avenue_of_stars")
        item.put(COLUMN_NAME_CUSTOM_IMG_DIRECTORY, "")
        db.insert(TABLE_NAME, null, item)
        item = ContentValues()
        item.put(COLUMN_NAME_TITLE, "Big buddha")
        item.put(COLUMN_NAME_DEFAULT_IMG, "big_buddha")
        item.put(COLUMN_NAME_CUSTOM_IMG_DIRECTORY, "")
        db.insert(TABLE_NAME, null, item)
        item = ContentValues()
        item.put(COLUMN_NAME_TITLE, "Flower market")
        item.put(COLUMN_NAME_DEFAULT_IMG, "flower_market")
        item.put(COLUMN_NAME_CUSTOM_IMG_DIRECTORY, "")
        db.insert(TABLE_NAME, null, item)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Attraction.db"
    }
}