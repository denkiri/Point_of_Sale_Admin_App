package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("yearlyProfit"))],
        primaryKeys = ["yearlyProfit"]
)
class YearProfit {

    @SerializedName("yearlyProfit")
    @Expose
    var yearlyProfit:Double=0.0
    @Ignore
    constructor(yearProfit: Double) {
        this.yearlyProfit = yearProfit
    }
    constructor()
}