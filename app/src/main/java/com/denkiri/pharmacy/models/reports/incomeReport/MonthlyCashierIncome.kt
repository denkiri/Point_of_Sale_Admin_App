package com.denkiri.pharmacy.models.reports.incomeReport
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("monthIncome"))],
    primaryKeys = ["monthIncome"]
)
class MonthlyCashierIncome {
    @SerializedName("month_income")
    @Expose
    var monthIncome: Double = 0.0

      constructor()

    constructor(monthIncome:Double) {
        this.monthIncome = monthIncome
    }
}