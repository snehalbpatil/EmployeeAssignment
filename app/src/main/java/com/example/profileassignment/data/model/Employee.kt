package com.example.profileassignment.data.model

import java.io.Serializable

data class Employee (var id:String,var name:String,var designation:String,var dob:String):Serializable {
    constructor(): this(  "","","","")
}