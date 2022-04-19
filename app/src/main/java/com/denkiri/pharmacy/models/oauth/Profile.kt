package com.denkiri.pharmacy.models.oauth
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("id"))],
    primaryKeys = ["id"]
)
class Profile {
    @field:SerializedName("id")
    var id: Int? = 0
    @field:SerializedName("username")
    var username: String? = null
    @field:SerializedName("password")
    var password: String? = null
    @field:SerializedName("name")
    var name: String? = null
    @field:SerializedName("position")
    var position: String? = null
    @field:SerializedName("secret")
    var secret: String? = null
    @field:SerializedName("email")
    var email:String? =null
    @field:SerializedName("mobile")
    var mobile:String? =null
    @field:SerializedName("token")
    var token:String? =null
    @field:SerializedName("business_code")
    var businessCode:String? =null
    @field:SerializedName("business_name")
    var businessName:String? =null
    @field:SerializedName("business_type")
    var businessType:String? =null
    @Ignore
    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
    @Ignore
    constructor(name: String?, username: String?,email:String,mobile:String) {
        this.name = name
        this.username= username
        this.email=email
        this.mobile=mobile
    }
    constructor()
}