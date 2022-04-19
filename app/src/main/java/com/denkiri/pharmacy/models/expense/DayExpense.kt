package com.denkiri.pharmacy.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("dayExpense"))],
    primaryKeys = ["dayExpense"]
)
class DayExpense {
    @SerializedName("day_expenses")
    @Expose
    var dayExpense: Double = 0.0
    @Ignore
    constructor(dayExpense: Double){
        this.dayExpense = dayExpense
    }
    constructor()
}