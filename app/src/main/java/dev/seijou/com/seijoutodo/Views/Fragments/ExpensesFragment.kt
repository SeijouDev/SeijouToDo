package dev.seijou.com.seijoutodo.Views.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.ObjectTypes.expenses
import dev.seijou.com.seijoutodo.R
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Log
import android.widget.*
import dev.seijou.com.seijoutodo.Helpers.*
import dev.seijou.com.seijoutodo.Objects.Account
import dev.seijou.com.seijoutodo.Objects.BasicListItem
import dev.seijou.com.seijoutodo.Views.Activities.MainActivity


/**
 * Created by frontend on 12/12/17.
 */
class ExpensesFragment : Fragment() , OnClickHandler {
    companion object {
        fun newInstance(): ExpensesFragment {
            return ExpensesFragment()
        }
    }

    lateinit var mView : View
    lateinit var adapter : BasicListAdpater
    lateinit var account : Account
    var budget  = -1f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_expenses, container, false)
        account = SqliteHelper(context!!).getSessionUser()!!
        budget = account.budget.toFloat()

        Log.e("asdasd" , "$account")
        mView.findViewById<TextView>(R.id.budget).setText(Helpers.formattedNumber(budget))
        mView.findViewById<TextView>(R.id.budget).setOnClickListener {
            Toast.makeText(context, "Puedes cambiar tu presupuesto base en el menú de ajustes", Toast.LENGTH_LONG).show()
        }


        updateList(mView, account.id)

        mView.findViewById<ImageButton>(R.id.btnNewExp).setOnClickListener {
            showNewBasicItemDialog(context!!, layoutInflater,expenses, mView , account.id)
        }

        return mView
    }

    override fun onItemLongClick(v: View, i: Int) {
        var item = adapter.list[i]
        val options = arrayOf<CharSequence>(if(item.active)"Desactivar" else "Activar", "Eliminar")
        val builder = AlertDialog.Builder(context)
        builder.setItems(options) { dialog, which ->
            if(which == 0){
                item.active = !item.active
                SqliteHelper(context!!).updateExpense(item)
                updateList(mView, account.id)
            }
            else {
                SqliteHelper(context!!).deleteExpense(item.id)
                updateList(mView, account.id)
            }
        }
        builder.show()

    }

    fun showNewBasicItemDialog(c: Context, inflater: LayoutInflater, type: Constants.Companion.ObjectTypes, v: View, userId: String) {
        val alertDialog = AlertDialog.Builder(c).create()
        alertDialog.setTitle("Nuevo gasto")
        alertDialog.setIcon(R.mipmap.ic_launcher)
        var dialogView = inflater.inflate(R.layout.form_create_object, null)
        var etx1 = dialogView.findViewById<EditText>(R.id.etx1)
        var etx2 = dialogView.findViewById<EditText>(R.id.etx2)
        etx1.hint = "Gasto"
        etx2.hint = "Valor"
        etx2.inputType = InputType.TYPE_CLASS_NUMBER
        alertDialog.setView(dialogView)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Guardar", { dialog, which ->
            var name = etx1.text.toString()
            var value = etx2.text.toString()
            var obj = BasicListItem(name, value, expenses, true, userId)
            obj.save(c, userId)
            updateList(v, account.id)
        })
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",{ dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    fun updateList(view : View, userId: String){

        var recycler = view.findViewById<RecyclerView>(R.id.expensesList)
        recycler.setLayoutManager(LinearLayoutManager(context))

        val rows = SqliteHelper(context!!).getExpenses(userId)

        if(rows.size > 0){

            recycler.visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layoutNoInfo).visibility = View.GONE

            adapter = BasicListAdpater(rows,this)
            recycler.adapter = adapter

            var sum = 0f
            rows.forEach { if(it.active) sum+= it.desc.toFloat() }

            view.findViewById<TextView>(R.id.expensesSum).setText(Helpers.formattedNumber(sum))
            view.findViewById<TextView>(R.id.total).setText(Helpers.formattedNumber(budget - sum))

        }
        else {
            recycler.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layoutNoInfo).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.expensesSum).setText("-")
            view.findViewById<TextView>(R.id.total).setText("-")
        }

    }
}