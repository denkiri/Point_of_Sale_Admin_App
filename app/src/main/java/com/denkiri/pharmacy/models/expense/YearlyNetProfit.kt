package com.denkiri.pharmacy.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("yearlyNetReport"))],
    primaryKeys = ["yearlyNetReport"]
)
class YearlyNetProfit {
    @SerializedName("yearly_net_income")
    @Expose
    var yearlyNetReport: Double = 0.0
    @Ignore
    constructor(yearlyNetReport: Double){
        this.yearlyNetReport = yearlyNetReport
    }
    constructor()
}