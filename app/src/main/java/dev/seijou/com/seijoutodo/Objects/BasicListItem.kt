package dev.seijou.com.seijoutodo.Objects

import android.content.Context
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.ObjectTypes
import dev.seijou.com.seijoutodo.Helpers.SqliteHelper

/**
 * Created by frontend on 12/12/17.
 */
class BasicListItem (title: String, desc: String, type: ObjectTypes, active: Boolean, userId: String) {

    internal var id: Int
    internal var userId: String
    internal var title: String
    internal var desc: String
    internal var active: Boolean
    internal var type: ObjectTypes

    init {
        this.id = -1
        this.title = title
        this.desc = desc
        this.type  = type
        this.active = active
        this.userId = userId
    }

    fun save(c: Context, userId: String){
        val db = SqliteHelper(c)

        when(this.type){
            ObjectTypes.expenses -> db.insertExpense(this, userId)
            ObjectTypes.purchase -> db.insertPurchase(this)
            ObjectTypes.music -> db.insertSong(this)
        }

    }

    override fun toString(): String {
        return " $id - $title - $desc - $active - $type - $userId"
    }
}