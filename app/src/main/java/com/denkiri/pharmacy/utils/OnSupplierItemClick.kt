package com.denkiri.pharmacy.utils

interface OnSupplierItemClick {
    fun delete(pos: Int)
    fun edit(pos: Int)
    fun dial(pos: Int)
    fun view(pos:Int)
    fun enable(pos:Int)
    fun disable(pos: Int)
    fun onClickListener(position1: Int)
    fun onLongClickListener(position1: Int)
}