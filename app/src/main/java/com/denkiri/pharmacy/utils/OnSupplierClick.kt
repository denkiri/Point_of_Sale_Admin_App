package com.denkiri.pharmacy.utils

interface OnSupplierClick {
    fun selected(pos: Int)
    fun delete(pos: Int)
    fun onClickListener(position1: Int)
}