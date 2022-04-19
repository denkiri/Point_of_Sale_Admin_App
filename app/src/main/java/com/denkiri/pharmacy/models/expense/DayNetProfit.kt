package com.denkiri.pharmacy.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("dayNetProfit"))],
    primaryKeys = ["dayNetProfit"]
)
class DayNetProfit {
    @SerializedName("day_net_profit")
    @Expose
    var dayNetProfit: Double = 0.0
    @Ignore
    constructor(dayNetProfit: Double){
        this.dayNetProfit = dayNetProfit
    }
    constructor()
}