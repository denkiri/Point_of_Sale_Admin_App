package com.denkiri.pharmacy.models.product
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("estimatedProfit"))],

        primaryKeys = ["estimatedProfit"]
)
class EstimatedProfit {
    @SerializedName("estimatedProfit")
    @Expose
    var estimatedProfit:Double=0.0
    @Ignore
    constructor(estimatedProfit: Double) {
        this.estimatedProfit = estimatedProfit
    }
    constructor()
}