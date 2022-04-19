package com.denkiri.pharmacy.models.reports.incomeReport
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(
    indices = [(Index("yearIncome"))],
    primaryKeys = ["yearIncome"]
)

class YearCashierIncome {
    @SerializedName("year_income")
    @Expose
    var yearIncome: Double = 0.0
    constructor()
    constructor(yearIncome:Double) {
        this.yearIncome = yearIncome
    }
}