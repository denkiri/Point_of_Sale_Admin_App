package com.denkiri.pharmacy.models.product

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class TotalCost {
    @SerializedName("total_cost")
    @Expose
    var totalCost:Double = 0.0
}