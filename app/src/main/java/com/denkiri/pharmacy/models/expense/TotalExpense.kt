package com.denkiri.pharmacy.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("totalExpense"))],
    primaryKeys = ["totalExpense"]
)
class TotalExpense {
    @SerializedName("total_expenses")
    @Expose
    var totalExpense: Double = 0.0
    @Ignore
    constructor(totalExpense: Double){
        this.totalExpense =totalExpense
    }
    constructor()
}