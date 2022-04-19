package com.denkiri.pharmacy.models.product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Product {
    @SerializedName("product_id")
    @Expose
    var productId: Int? = 0

    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("description_name")
    @Expose
    var descriptionName: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("cost")
    @Expose
    var cost: Double = 0.00

    @SerializedName("price")
    @Expose
    var price: Double= 0.00
    @SerializedName("vat")
    @Expose
    var vat: Double= 0.00

    @SerializedName("supplier")
    @Expose
    var supplier: String? = null

    @SerializedName("qty_left")
    @Expose
    var qtyLeft:Int? = 0

    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("brand")
    @Expose
    var brand: String? = null
    @SerializedName("date_delivered")
    @Expose
    var dateDelivered: String? = null

    @SerializedName("expiration_date")
    @Expose
    var expirationDate: String? = null

    @SerializedName("reorder")
    @Expose
    var reorder: Int? = null

    @SerializedName("suplier_id")
    @Expose
    var suplierId: Int? = 0

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