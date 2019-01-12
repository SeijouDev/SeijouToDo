package dev.seijou.com.seijoutodo.Views.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import dev.seijou.com.seijoutodo.Helpers.BasicListAdpater
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.ObjectTypes.purchase
import dev.seijou.com.seijoutodo.Helpers.OnClickHandler
import dev.seijou.com.seijoutodo.Helpers.SqliteHelper
import dev.seijou.com.seijoutodo.Objects.Account
import dev.seijou.com.seijoutodo.Objects.BasicListItem
import dev.seijou.com.seijoutodo.R

/**
 * Created by frontend on 18/12/17.
 */
class ShoppingFragment : Fragment(), OnClickHandler{

    companion object {
        fun newInstance(): ShoppingFragment {
            return ShoppingFragment()
        }
    }

    lateinit var adapter : BasicListAdpater
    lateinit var account : Account
    lateinit var mView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_shopping, container, false)
        account = SqliteHelper(context!!).getSessionUser()!!

        mView.findViewById<FloatingActionButton>(R.id.fabNewPurchase).setOnClickListener {
            showNewPurchaseDialog(context!!, inflater, mView, account.id)
        }

        updateList(mView, account.id)

        return mView
    }

    fun showNewPurchaseDialog(c: Context, inflater: LayoutInflater, v: View, userId: String) {
        val alertDialog = AlertDialog.Builder(c).create()
        alertDialog.setTitle("Nueva compra")
        alertDialog.setIcon(R.mipmap.ic_launcher)
        var dialogView = inflater.inflate(R.layout.form_create_object, null)
        var etx1 = dialogView.findViewById<EditText>(R.id.etx1)
        var etx2 = dialogView.findViewById<EditText>(R.id.etx2)
        etx1.hint = "Compra"
        etx2.hint = "Descripción"
        alertDialog.setView(dialogView)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Guardar", { dialog, which ->
            var name = etx1.text.toString()
            var value = etx2.text.toString()
            var obj = BasicListItem(name, value, purchase, true, userId)
            obj.save(c, userId)
            updateList(v, account.id)
        })
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",{ dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    fun updateList(view : View, userId: String){

        var recycler = view.findViewById<RecyclerView>(R.id.recyclerShooping)
        recycler.setLayoutManager(LinearLayoutManager(context))

        val rows = SqliteHelper(context!!).getPurchases(userId)

        if(rows.size > 0){
            recycler.visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layoutNoInfo).visibility = View.GONE
            adapter = BasicListAdpater(rows,this)
            recycler.adapter = adapter
        }
        else {
            recycler.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layoutNoInfo).visibility = View.VISIBLE
        }

    }

    override fun onItemLongClick(v: View, i: Int) {
        var item = adapter.list[i]
        val options = arrayOf<CharSequence>("Eliminar")
        val builder = AlertDialog.Builder(context)
        builder.setItems(options) { dialog, which ->
            SqliteHelper(context!!).deletePurchase(item.id)
            updateList(mView, account.id)
        }
        builder.show()
    }

}