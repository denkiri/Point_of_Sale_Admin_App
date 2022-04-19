package com.denkiri.pharmacy.models.category
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity
class Category {
    @PrimaryKey
    @SerializedName("category_id")
    @Expose
    var categoryId = 0

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null


}