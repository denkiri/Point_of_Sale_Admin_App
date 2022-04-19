package com.denkiri.pharmacy.models.reports.incomeReport
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("lastMonthIncome"))],
    primaryKeys = ["lastMonthIncome"]
)
class LastMonthCashierIncome {
    @SerializedName("lastMonth_income")
    @Expose
    var lastMonthIncome: Double = 0.0
    constructor()
    constructor(lastMonthIncome: Double) {
        this.lastMonthIncome = lastMonthIncome
    }
}