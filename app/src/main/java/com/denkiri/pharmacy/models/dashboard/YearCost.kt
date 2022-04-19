package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("yearlyCost"))],

        primaryKeys = ["yearlyCost"]
)
class YearCost {

    @SerializedName("yearly_cost")
    @Expose
    var yearlyCost :Double= 0.0
    @Ignore
    constructor(yearCost: Double) {
        this.yearlyCost = yearCost
    }
    constructor()
}