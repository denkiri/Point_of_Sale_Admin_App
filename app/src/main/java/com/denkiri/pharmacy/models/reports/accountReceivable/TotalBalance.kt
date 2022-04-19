package com.denkiri.pharmacy.models.reports.accountReceivable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("balance"))],
        primaryKeys = ["balance"]
)
class TotalBalance {
    @SerializedName("balance")
    @Expose
    var balance: Double = 0.0
    @Ignore
    constructor(balance: Double){
        this.balance = balance
    }
    constructor()

}