package com.denkiri.pharmacy.models.brand
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity
class Brand {
    @PrimaryKey
    @SerializedName("brand_id")
    @Expose
    var brandId = 0

    @SerializedName("brand_name")
    @Expose
    var brandName: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

}