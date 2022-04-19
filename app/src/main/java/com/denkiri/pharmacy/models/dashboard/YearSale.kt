package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("yearlySales"))],
        primaryKeys = ["yearlySales"]
)
class YearSale {

    @SerializedName("yearly_sales")
    @Expose
    var yearlySales:Double = 0.0
    @Ignore
    constructor(yearSale: Double) {
        this.yearlySales = yearSale
    }
    constructor()
}