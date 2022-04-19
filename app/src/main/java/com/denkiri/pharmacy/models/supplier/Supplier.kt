package com.denkiri.pharmacy.models.supplier
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Supplier {
    @SerializedName("suplier_id")
    @Expose
    var suplierId = 0

    @SerializedName("suplier_name")
    @Expose
    var suplierName: String? = null

    @SerializedName("suplier_address")
    @Expose
    var suplierAddress: String? = null

    @SerializedName("suplier_contact")
    @Expose
    var suplierContact: String? = null

    @SerializedName("contact_person")
    @Expose
    var contactPerson: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
}