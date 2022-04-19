package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("monthlyIncome"))],
    primaryKeys = ["monthlyIncome"]
)
class MonthlyIncome {
    @SerializedName("monthly_income")
    @Expose
    var monthlyIncome: Double = 0.0
    @Ignore
    constructor(monthlyIncome: Double){
        this.monthlyIncome = monthlyIncome
    }
    constructor()
}