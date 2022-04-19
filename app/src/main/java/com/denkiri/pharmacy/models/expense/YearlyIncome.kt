package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("yearlyIncome"))],
    primaryKeys = ["yearlyIncome"]
)
class YearlyIncome {
    @SerializedName("yearly_income")
    @Expose
    var yearlyIncome: Double = 0.0
    @Ignore
    constructor(yearlyIncome: Double){
        this.yearlyIncome = yearlyIncome
    }
    constructor()
}