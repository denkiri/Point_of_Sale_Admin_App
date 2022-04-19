package com.denkiri.pharmacy.models.reports.salesReport

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


@Entity(
        indices = [(Index("totalprofit"))],
        primaryKeys = ["totalprofit"]
)

class TotalProfit {
    @SerializedName("totalprofit")
    @Expose
    var totalprofit: Double = 0.0
    @Ignore
    constructor(totalprofit: Double){
        this.totalprofit = totalprofit
    }
    constructor()
}