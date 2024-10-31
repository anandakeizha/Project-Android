package com.dev.insertdata


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.ImageView
import androidx.core.content.contentValuesOf
import java.io.ByteArrayOutputStream


class DBmain(context: Context)  : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "data"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alldata"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_NICKNAME = "nick"
        private const val COLUMN_ADDRES= "addres"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_BORN = "born"
        private const val COLUMN_FOTO = "foto"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY , $COLUMN_NAME TEXT , $COLUMN_NICKNAME TEXT ," +
                " $COLUMN_PHONE TEXT , $COLUMN_BORN TEXT , $COLUMN_EMAIL TEXT , $COLUMN_ADDRES TEXT ,$COLUMN_FOTO BLOB)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase? , oldVersion: Int , newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)

    }

    fun insertData(data: inputData) {
        val db = writableDatabase
        val vales = ContentValues().apply {
            put(COLUMN_NAME, data.name)
            put(COLUMN_NICKNAME, data.nickname)
            put(COLUMN_EMAIL , data.email)
            put(COLUMN_ADDRES, data.addres)
            put(COLUMN_BORN , data.born)
            put(COLUMN_PHONE, data.phone)
            put(COLUMN_FOTO , data.image)
        }
        db?.insert(TABLE_NAME, null, vales)
        db.close()
    }

    fun getAllData(): List<inputData>{
        val dataList = mutableListOf<inputData>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while(cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val nick = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val addres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRES))
            val born = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORN))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOTO))
            val data = inputData(id, name, nick, email, addres, born, phone , image)
            dataList.add(data)
        }
        cursor.close()
        db.close()
        return dataList
    }

    fun updateData(data : inputData){
        val db = writableDatabase
        val values = contentValuesOf().apply {
            put(COLUMN_NAME, data.name)
            put(COLUMN_NICKNAME, data.nickname)
            put(COLUMN_EMAIL , data.email)
            put(COLUMN_ADDRES, data.addres)
            put(COLUMN_BORN , data.born)
            put(COLUMN_PHONE, data.phone)
            put(COLUMN_FOTO, data.image)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(data.id.toString())
        db.update (TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getDatabyId(dataid : Int): inputData{
        val db = readableDatabase
        val query = "SELECT *  FROM $TABLE_NAME WHERE $COLUMN_ID = $dataid"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val nick = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val addres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRES))
        val born = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORN))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOTO))

        cursor.close()
        db.close()
        return inputData(id, name , nick , email , addres , born , phone, image)

    }

    fun deleteNote(dataid: Int){
        val db = writableDatabase
        val whereClasue = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(dataid.toString())
        db.delete(TABLE_NAME , whereClasue, whereArgs)
        db.close()
    }

    fun ImageViewToByte(img: ImageView): ByteArray {
        val bitmap: Bitmap = (img.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes: ByteArray = stream.toByteArray()
        return bytes
    }

}