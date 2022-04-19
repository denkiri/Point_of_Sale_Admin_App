package com.denkiri.pharmacy.models.reports.salesReport

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


@Entity(
        indices = [(Index("totalcost"))],
        primaryKeys = ["totalcost"]
)
class TotalCost {
    @SerializedName("totalcost")
    @Expose
    var totalcost: Double = 0.0
    @Ignore
    constructor(totalcost: Double){
        this.totalcost = totalcost
    }
    constructor()

}