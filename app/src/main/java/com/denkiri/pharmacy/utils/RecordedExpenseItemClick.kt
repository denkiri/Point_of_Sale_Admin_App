package com.denkiri.pharmacy.utils

interface RecordedExpenseItemClick {
    fun delete(pos: Int)
    fun edit(pos: Int)
    fun onClickListener(position1: Int)
    fun onLongClickListener(position1: Int)
}