package com.denkiri.pharmacy.models.supplier
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class SupplierData {
    @SerializedName("error")
    @Expose
    var error = false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("suppliers")
    @Expose
    var suppliers: List<Supplier>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}