package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("dayIncome"))],
    primaryKeys = ["dayIncome"]
)
class DayIncome {
    @SerializedName("day_income")
    @Expose
    var dayIncome: Double = 0.0
    @Ignore
    constructor(dayIncome: Double){
        this.dayIncome = dayIncome
    }
    constructor()
}