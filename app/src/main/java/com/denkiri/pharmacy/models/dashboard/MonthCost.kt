package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("monthlyCost"))],
        primaryKeys = ["monthlyCost"]
)
class MonthCost {
    @SerializedName("monthly_cost")
    @Expose
    var monthlyCost:Double = 0.0
    @Ignore
    constructor(monthCost: Double) {
        this.monthlyCost = monthCost
    }
    constructor()
}