package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import java.lang.ref.WeakReference

class IncomeViewHolder (type: Int, itemView: View, listener: RecordedExpenseItemClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val listenerWeakReference: WeakReference<RecordedExpenseItemClick> = WeakReference(listener)
    var itemVew: View
    var incomeTitle: TextView
    var incomeAmount: TextView
    var date: TextView
    lateinit var edit: ImageView
    lateinit var delete: ImageView
    init {
        this.itemVew=itemView
        incomeTitle=itemView.findViewById(R.id.income_title)
        incomeAmount=itemView.findViewById(R.id.income_amount)
        date=itemView.findViewById(R.id.date)
        if (type==0) {
            delete = itemView.findViewById(R.id.delete)
            delete.setOnClickListener(this)
            edit =itemView.findViewById(R.id.edit)
            edit.setOnClickListener(this)

        }
    }

    override fun onClick(v: View?) {
        if ( v == edit  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if (v == delete) {
            listenerWeakReference.get()?.delete(adapterPosition)
        }


    }
}