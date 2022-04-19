package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("dayprofit"))],
        primaryKeys = ["dayprofit"]
)
class DayProfit {
    @SerializedName("dayprofit")
    @Expose
    var dayprofit: Double = 0.0
    @Ignore
    constructor(dayProfit: Double){
        this.dayprofit = dayProfit
    }
    constructor()
}