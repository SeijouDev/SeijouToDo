package dev.seijou.com.seijoutodo.Helpers


/**
 * Created by frontend on 11/12/17.
 */
class Constants {
    companion object {
        val sharedPreferencesName = "seijouToDoPrefs"
        val userIdPrefs = "userId"
        val accountName = "name"
        val accountLastname = "lastname"
        val accountEmail = "email"
        val accountImage = "image"
        val accountLoginType = "loginType"
        val budget = "budget"

        val expensesFragmentTag = "Gastos"
        val musicFragmentTag = "Música"
        val placesFragmentTag = "Lugares"
        val settingsFragmentTag = "Ajustes"
        val todoFragmentTag = "Pendientes"
        val shoppingFragmentTag = "Compras"

        enum class ObjectTypes {
            expenses, purchase, music
        }
    }
}