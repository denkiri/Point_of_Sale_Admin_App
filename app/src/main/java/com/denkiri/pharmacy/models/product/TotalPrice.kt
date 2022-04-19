package com.denkiri.pharmacy.models.product

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class TotalPrice {
    @SerializedName("total_price")
    @Expose
    var totalPrice:Double = 0.0
}
