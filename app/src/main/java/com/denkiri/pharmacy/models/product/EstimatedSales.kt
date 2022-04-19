package com.denkiri.pharmacy.models.product
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("estimatedSales"))],

        primaryKeys = ["estimatedSales"]
)
class EstimatedSales {
    @SerializedName("estimatedSales")
    @Expose
    var estimatedSales:Double = 0.0
    @Ignore
    constructor(estimatedSales: Double) {
        this.estimatedSales = estimatedSales
    }
    constructor()
}