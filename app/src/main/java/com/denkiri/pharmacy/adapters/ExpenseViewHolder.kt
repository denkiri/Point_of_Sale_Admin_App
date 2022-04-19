package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnExpenseItemClick
import com.denkiri.pharmacy.utils.OnSupplierItemClick
import java.lang.ref.WeakReference

class ExpenseViewHolder(type: Int, itemView: View, listener: OnExpenseItemClick): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == edit  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if (v == delete) {
            listenerWeakReference.get()?.delete(adapterPosition)
        }
        if (v == enable) {
            listenerWeakReference.get()?.enable(adapterPosition)
        }
        if (v == disable) {
            listenerWeakReference.get()?.disable(adapterPosition)
        }
        if (v == view) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnExpenseItemClick> = WeakReference(listener)
    var itemVew: View
    var expense_title: TextView
    var expense_amount: TextView
    lateinit var edit: ImageView
    lateinit var delete: ImageView
    var view: ImageView
    var enable: Button
    var disable: Button
    init {
        this.itemVew=itemView
        expense_title=itemView.findViewById(R.id.expense_title)
        expense_amount=itemView.findViewById(R.id.expense_amount)
        enable=itemView.findViewById(R.id.enable)
        disable=itemView.findViewById(R.id.disable)
        view=itemVew.findViewById(R.id.view)
        if (type==0) {
            delete = itemView.findViewById(R.id.delete)
            delete.setOnClickListener(this)
            edit =itemView.findViewById(R.id.edit)
            edit.setOnClickListener(this)
            enable=itemView.findViewById(R.id.enable)
            enable.setOnClickListener(this)
            disable=itemView.findViewById(R.id.disable)
            disable.setOnClickListener(this)
            view.setOnClickListener(this)

        }
    }
}