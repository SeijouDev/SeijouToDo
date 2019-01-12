package dev.seijou.com.seijoutodo.Helpers

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.ObjectTypes.*
import dev.seijou.com.seijoutodo.Objects.BasicListItem
import dev.seijou.com.seijoutodo.Objects.Account

/**
 * Created by frontend on 12/12/17.
 */
class SqliteHelper(context: Context) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    private val context : Context

    init {
        this.context = context
    }

    companion object {
        private val databaseVersion = 11

        private val databaseName = "seijouTodoDB"

        private val expensesTable = "contacts"
        private val expenseId = "id"
        private val expenseName = "name"
        private val expenseValue = "value"
        private val expenseActive = "active"
        private val expenseUserId = "fk_user"

        private val createExpensesTable =
                "CREATE TABLE $expensesTable ( " +
                        "$expenseId INTEGER PRIMARY KEY, " +
                        "$expenseName TEXT NOT NULL, " +
                        "$expenseValue TEXT NOT NULL, " +
                        "$expenseActive INT NOT NULL, " +
                        "$expenseUserId TEXT NOT NULL" +
                        ");"

        private val dropExpensesTable = "DROP TABLE IF EXISTS $expensesTable"

        private val usersTable = "users"
        private val usersUserId = "userId"
        private val userName = "userName"
        private val userLastname = "userLastname"
        private val userEmail = "userEmail"
        private val userBudget = "userBudget"
        private val userLogintype = "loginType"

        private val createUsersTable = "CREATE TABLE $usersTable ( " +
                "$usersUserId TEXT NOT NULL, " +
                "$userName TEXT NOT NULL," +
                "$userLastname TEXT NOT NULL, " +
                "$userEmail TEXT NOT NULL, " +
                "$userLogintype INTEGER NOT NULL, " +
                "$userBudget TEXT NOT NULL )"

        private val dropUsersTable = "DROP TABLE IF EXISTS $usersTable"

        private val shoppingTable = "shoppingTable"
        private val purchaseId = "purchaseId"
        private val purchaseName = "purchaseName"
        private val purchaseDesc = "purchaseDesc"
        private val purchaseActive = "purchaseActive"
        private val purchaseUserId = "purchase_fk_user"

        private val createShoppingTable =
                "CREATE TABLE $shoppingTable ( " +
                        "$purchaseId INTEGER PRIMARY KEY, " +
                        "$purchaseName TEXT NOT NULL, " +
                        "$purchaseDesc TEXT NOT NULL, " +
                        "$purchaseActive INT NOT NULL, " +
                        "$purchaseUserId TEXT NOT NULL" +
                        ");"

        private val dropShoppingTable = "DROP TABLE IF EXISTS $shoppingTable"

        private val musicTable = "musicTable"
        private val songId = "songId"
        private val songName = "songName"
        private val songDesc = "songDesc"
        private val songActive = "songActive"
        private val songUserId = "song_fk_user"


        private val createMusicTable =
                "CREATE TABLE $musicTable ( " +
                        "$songId INTEGER PRIMARY KEY, " +
                        "$songName TEXT NOT NULL, " +
                        "$songDesc TEXT NOT NULL, " +
                        "$songActive INT NOT NULL, " +
                        "$songUserId TEXT NOT NULL" +
                        ");"

        private val dropMusicTable = "DROP TABLE IF EXISTS $musicTable"



    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createExpensesTable)
        db.execSQL(createUsersTable)
        db.execSQL(createShoppingTable)
        db.execSQL(createMusicTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(dropExpensesTable)
        db.execSQL(dropUsersTable)
        db.execSQL(dropShoppingTable)
        db.execSQL(dropMusicTable)
        onCreate(db)
    }

    /******************************************************************/

    fun insertPurchase(purchase: BasicListItem) : BasicListItem  {
        val db = this.writableDatabase

        var values = ContentValues()
        values.put(purchaseName, purchase.title)
        values.put(purchaseDesc, purchase.desc)
        values.put(purchaseActive, purchase.active)
        values.put(purchaseUserId, purchase.userId)

        val id = db.insert(shoppingTable, null, values)
        db.close()

        purchase.id = id.toInt()

        return  purchase
    }

    fun updatePurchase(purchase: BasicListItem)  {
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(purchaseName, purchase.title)
        values.put(purchaseDesc, purchase.desc)
        values.put(purchaseActive, purchase.active)

        Log.e("update" , "${purchase}")

        var result = db.update(shoppingTable, values, "id=${purchase.id}", null)

        Log.e("update","$result");
        db.close()

    }

    fun getPurchases(userId: String) : ArrayList<BasicListItem> {
        val db = this.readableDatabase
        var list = ArrayList<BasicListItem>()

        var cursor = db.query(shoppingTable, null, "$purchaseUserId = '$userId'", null, null, null, null)
        if(cursor != null)
        {
            try {
                cursor.moveToFirst()

                do {
                    val obj = BasicListItem(cursor.getString(1), cursor.getString(2), purchase,  cursor.getInt(3) > 0, userId)
                    obj.id = cursor.getInt(0)
                    list.add(obj)
                }while (cursor.moveToNext())
            } catch (e: CursorIndexOutOfBoundsException) {
                Log.e("sqlite","Empty")
            }

        }


        return list
    }

    fun deletePurchase(id: Int) {
        val db = this.writableDatabase
        db.delete(shoppingTable, "$purchaseId = $id", null)
        db.close()
    }

    /******************************************************************/

    fun insertSong(song: BasicListItem) : BasicListItem  {
        val db = this.writableDatabase

        var values = ContentValues()
        values.put(songName, song.title)
        values.put(songDesc, song.desc)
        values.put(songActive, song.active)
        values.put(songUserId, song.userId)

        val id = db.insert(musicTable, null, values)
        db.close()

        song.id = id.toInt()

        return  song
    }

    fun updateSong(song: BasicListItem)  {
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(songName, song.title)
        values.put(songDesc, song.desc)
        values.put(songActive, song.active)

        var result = db.update(musicTable, values, "id=${song.id}", null)

        Log.e("update","$result");
        db.close()

    }

    fun getSongs(userId: String) : ArrayList<BasicListItem> {
        val db = this.readableDatabase
        var list = ArrayList<BasicListItem>()

        var cursor = db.query(musicTable, null, "$songUserId = '$userId'", null, null, null, null)
        if(cursor != null)
        {
            try {
                cursor.moveToFirst()

                do {
                    val obj = BasicListItem(cursor.getString(1), cursor.getString(2), music,  cursor.getInt(3) > 0, userId)
                    obj.id = cursor.getInt(0)
                    list.add(obj)
                }while (cursor.moveToNext())
            } catch (e: CursorIndexOutOfBoundsException) {
                Log.e("sqlite","Empty")
            }

        }


        return list
    }

    fun deleteSong(id: Int) {
        val db = this.writableDatabase
        db.delete(musicTable, "$songId = $id", null)
        db.close()
    }

    /******************************************************************/

    fun insertExpense(expense: BasicListItem, userId: String) : BasicListItem  {
        val db = this.writableDatabase

        var values = ContentValues()
        values.put(expenseName, expense.title)
        values.put(expenseValue, expense.desc)
        values.put(expenseActive, expense.active)
        values.put(expenseUserId, expense.userId)

        val id = db.insert(expensesTable, null, values)
        db.close()

        expense.id = id.toInt()

        return  expense
    }

    fun updateExpense(expense: BasicListItem)  {
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(expenseName, expense.title)
        values.put(expenseValue, expense.desc)
        values.put(expenseActive, expense.active)

        Log.e("update" , "${expense}")

        var result = db.update(expensesTable, values, "id=${expense.id}", null)

        Log.e("update","$result");
        db.close()

    }

    fun getExpenses(userId: String) : ArrayList<BasicListItem> {
        val db = this.readableDatabase
        var list = ArrayList<BasicListItem>()

        var cursor = db.query(expensesTable, null, "$expenseUserId = '$userId'", null, null, null, null)
        if(cursor != null)
        {
            try {
                cursor.moveToFirst()

                do {
                    val obj = BasicListItem(cursor.getString(1), cursor.getString(2), expenses,  cursor.getInt(3) > 0, userId)
                    obj.id = cursor.getInt(0)
                    list.add(obj)
                }while (cursor.moveToNext())
            } catch (e: CursorIndexOutOfBoundsException) {
                Log.e("sqlite","Empty")
            }

        }


        return list
    }

    fun deleteExpense(id: Int) {
        val db = this.writableDatabase
        db.delete(expensesTable, "$expenseId = $id", null)
        db.close()
    }

    /******************************************************************/

    fun insertUser(user: Account)   {

        val db = this.writableDatabase
        try {

            var values = ContentValues()
            values.put(usersUserId, user.id)
            values.put(userName, user.name)
            values.put(userLastname, user.lastname)
            values.put(userEmail, user.email)
            values.put(userLogintype, user.loginType)
            values.put(userBudget, user.budget)

            db.insert(usersTable, null, values)

        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            db.close()
        }

    }

    fun updateUser(user: Account)   {

        val db = this.writableDatabase
        try {

            var values = ContentValues()
            values.put(usersUserId, user.id)
            values.put(userName, user.name)
            values.put(userLastname, user.lastname)
            values.put(userEmail, user.email)
            values.put(userLogintype, user.loginType)
            values.put(userBudget, user.budget)

            db.update(usersTable, values,null,null)

        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            db.close()
        }

    }

    fun getSessionUser() : Account? {
        val db = this.readableDatabase
        var account : Account? = null
        val userId = PreferencesManager.getUserId(context)

        var cursor = db.query(usersTable, null, "$usersUserId = '$userId'", null, null, null, null)
        if(cursor != null)
        {
            try {
                cursor.moveToFirst()

                do {
                    account = Account(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getInt(4),cursor.getString(5))
                } while (cursor.moveToNext())
            } catch (e: CursorIndexOutOfBoundsException) {
                Log.e("sqlite","Empty")
            }

        }

        return account
    }

    fun userExists(userId: String) : Boolean {
        val db = this.readableDatabase
        var cursor = db.query(usersTable, null, "$usersUserId = '$userId'", null, null, null, null)
        return  (cursor != null && cursor.count > 0)
    }



}