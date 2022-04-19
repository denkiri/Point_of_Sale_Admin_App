package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("yearlyExpense"))],
    primaryKeys = ["yearlyExpense"]
)
class YearlyExpense {
    @SerializedName("yearly_expenses")
    @Expose
    var yearlyExpense: Double = 0.0
    @Ignore
    constructor(yearlyExpense: Double){
        this.yearlyExpense =yearlyExpense
    }
    constructor()
}