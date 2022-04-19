package com.denkiri.pharmacy.models.reports.incomeReport
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("dayIncome"))],
    primaryKeys = ["dayIncome"]
)
class DayCashierIncome {
    @SerializedName("day_income")
    @Expose
    var dayIncome: Double=0.0

    constructor(dayIncome: Double) {
        this.dayIncome = dayIncome
    }
    constructor()
}