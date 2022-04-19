package com.denkiri.pharmacy.models.purchases
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PurchaseOrder {
    @SerializedName("transaction_id")
    @Expose
    var transactionId: Int? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("date_order")
    @Expose
    var dateOrder: String? = null

    @SerializedName("suplier")
    @Expose
    var suplier: String? = null

    @SerializedName("date_deliver")
    @Expose
    var dateDeliver: String? = null

    @SerializedName("p_name")
    @Expose
    var pName: String? = null

    @SerializedName("qty")
    @Expose
    var qty: Int? = null

    @SerializedName("cost")
    @Expose
    var cost: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("remark")
    @Expose
    var remark: String? = null

    @SerializedName("suplier_id")
    @Expose
    var suplierId: Int? = null

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
}