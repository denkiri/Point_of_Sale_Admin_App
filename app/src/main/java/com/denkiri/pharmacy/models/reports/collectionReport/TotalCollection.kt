package com.denkiri.pharmacy.models.reports.collectionReport
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("totalcollection"))],
        primaryKeys = ["totalcollection"]
)
class TotalCollection {
    @SerializedName("totalcollection")
    @Expose
    var totalcollection: Double = 0.0
    @Ignore
    constructor(totalcollection: Double){
        this.totalcollection = totalcollection
    }
    constructor()
}