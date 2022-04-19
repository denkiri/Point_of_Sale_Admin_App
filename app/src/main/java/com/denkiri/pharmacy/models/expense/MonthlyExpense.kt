package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("monthlyExpense"))],
    primaryKeys = ["monthlyExpense"]
)
class MonthlyExpense {
    @SerializedName("monthly_expenses")
    @Expose
    var monthlyExpense: Double = 0.0
    @Ignore
    constructor(monthlyExpense: Double){
        this.monthlyExpense =monthlyExpense
    }
    constructor()
}