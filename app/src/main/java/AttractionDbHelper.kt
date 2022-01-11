import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class AttractionDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object AttractionEntry: BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DEFAULT_IMG = "defaultImageName"
        const val COLUMN_NAME_CUSTOM_IMG_URI = "customImgUri"
        const val COLUMN_NAME_LAT = "latitude"
        const val COLUMN_NAME_LNG = "longitude"


        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_DEFAULT_IMG TEXT, $COLUMN_NAME_CUSTOM_IMG_URI TEXT, " +
                    "$COLUMN_NAME_LAT TEXT, $COLUMN_NAME_LNG TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    fun getSize(): Int{
        val db = readableDatabase
        val cursor = db.query(AttractionEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return cursor.count
    }
    fun getData(position: Int, column: String): Any{
        val db = readableDatabase
        val cursor = db.query(AttractionEntry.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(position.toString()),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        return cursor.getString(cursor.getColumnIndexOrThrow(column))
    }
    fun updateData(position: Int, column: String, value: String){
        val db = writableDatabase
        var values = ContentValues().apply {
            put(column, value)
        }
        db.update(AttractionEntry.TABLE_NAME, values, "${BaseColumns._ID} LIKE ?", arrayOf(position.toString()))
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(AttractionEntry.SQL_CREATE_ENTRIES)
        var item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Avenue of stars")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "avenue_of_stars")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.29308898388784")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.17413976128829")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Big buddha")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "big_buddha")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.25402438248997")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "113.90491962235149")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Flower market")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "flower_market")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.325229")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.172089")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Lan kwai fong")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "lan_kwai_fong")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.2811")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.1555")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Peak")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "peak")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.27084")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.14979")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Stanley")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "stanley")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.21866423061577")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.21107994481291")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Star ferries")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "star_ferries")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.293790377174222")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.16874283947945")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Flower market")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "flower_market")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.325229")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.172089")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
        item = ContentValues()
        item.put(AttractionEntry.COLUMN_NAME_TITLE, "Temple street")
        item.put(AttractionEntry.COLUMN_NAME_DEFAULT_IMG, "temple_street")
        item.put(AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        item.put(AttractionEntry.COLUMN_NAME_LAT, "22.30552")
        item.put(AttractionEntry.COLUMN_NAME_LNG, "114.169731")
        db.insert(AttractionEntry.TABLE_NAME, null, item)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(AttractionEntry.SQL_DELETE_ENTRIES)
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