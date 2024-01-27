package com.jasmeet.googlesignindemo


const val IMG_URL = "url"
const val NAME = "name"
const val EMAIL = "email"

sealed class Screens(val route : String) {
    data object Login : Screens("login")
    data object Home : Screens(
        "home/{$IMG_URL}/{$NAME}/{$EMAIL}"
    ){
        fun passData(imgUrl : String, name : String, email : String) : String {
            return this.route
                .replace("{$IMG_URL}", imgUrl)
                .replace("{$NAME}", name)
                .replace("{$EMAIL}", email)
        }
    }
}