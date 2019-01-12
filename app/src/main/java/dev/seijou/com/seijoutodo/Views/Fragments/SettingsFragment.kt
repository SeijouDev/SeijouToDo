package dev.seijou.com.seijoutodo.Views.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.seijou.com.seijoutodo.R
import android.text.InputType
import android.util.Log
import android.widget.TextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import dev.seijou.com.seijoutodo.Helpers.SqliteHelper
import dev.seijou.com.seijoutodo.Objects.Account


/**
 * Created by frontend on 12/12/17.
 */
class SettingsFragment : Fragment() {

    lateinit var txBudget : TextView
    lateinit var account : Account

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_settings, container, false)

        txBudget = view.findViewById(R.id.budget_value)
        account = SqliteHelper(context!!).getSessionUser()!!
        Log.e("budget","ACCOUNT -> ${account.toString()}" )
        view.findViewById<LinearLayout>(R.id.layout_budget).setOnClickListener { showBudgetDialog() }

        updateBudgetField()

        return view
    }

    fun showBudgetDialog() {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Presupuesto")

        account = SqliteHelper(context!!).getSessionUser()!!

        var input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setSingleLine()
        input.setText(account.budget)
        input.setSelection(0, input.text.length)

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        input.layoutParams = lp
        val container = FrameLayout(context)
        container.addView(input)

        alertDialog.setView(container)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Guardar", { dialog, which ->
            account.budget = (input.text.toString())
            Log.e("budget","new budget -> ${(input.text.toString())}" )
            SqliteHelper(context!!).updateUser(account)
            updateBudgetField()
        })

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",{ dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    fun updateBudgetField() = txBudget.setText(String.format("$ %,.0f", account.budget.toFloat()))
}