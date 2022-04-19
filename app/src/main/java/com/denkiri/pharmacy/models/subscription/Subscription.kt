package com.denkiri.pharmacy.models.subscription
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("id"))],
        primaryKeys = ["id"]
)
class Subscription {
    @field:SerializedName("id")
    @Expose
    var id: Int? = 0

    @field:SerializedName("fixed_amount")
    @Expose
    var fixedAmount: Int? = null

    @field:SerializedName("code")
    @Expose
    var code: String? = null

    @field:SerializedName("percentage")
    @Expose
    var percentage: Int? =0
    @Ignore
    constructor(code: String?) {
        this.code = code
    }
    constructor()
}