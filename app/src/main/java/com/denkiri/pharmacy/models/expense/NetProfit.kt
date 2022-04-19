package com.denkiri.pharmacy.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("netIncome"))],
    primaryKeys = ["netIncome"]
)
class NetProfit {
    @SerializedName("net_income")
    @Expose
    var netIncome: Double = 0.0
    @Ignore
    constructor(netIncome: Double){
        this.netIncome =netIncome
    }
    constructor()
}