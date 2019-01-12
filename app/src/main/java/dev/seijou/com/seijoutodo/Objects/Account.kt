package dev.seijou.com.seijoutodo.Objects

/**
 * Created by frontend on 11/12/17.
 */
class Account (idr: String, namer: String, lastnamer: String, emailr: String, loginTyper: Int, budget: String){

    internal var id: String
    internal var name: String
    internal var lastname: String
    internal var email: String
    internal var image: String? = null
    internal var loginType: Int
    internal var budget: String

    init {
        this.id = idr
        this.name = namer
        this.lastname = lastnamer
        this.email = emailr
        this.loginType = loginTyper
        this.budget = budget
    }

    override fun toString(): String =  "- ${this.id} - ${this.name} - ${this.lastname} - ${this.email}  - ${this.loginType} - ${this.budget}"

}