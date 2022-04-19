package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryClick
import com.denkiri.pharmacy.utils.OnSupplierClick
import java.lang.ref.WeakReference

class SupplierItemViewHolder(type: Int, itemView: View, listener: OnSupplierClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == supplier_title  ) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }


    }
    private val listenerWeakReference: WeakReference<OnSupplierClick> = WeakReference(listener)
    var itemVew: View
    var supplier_title: TextView
    init {
        this.itemVew=itemView
        supplier_title=itemView.findViewById(R.id.supplier)

        if (type==0) {

            supplier_title.setOnClickListener(this)
        }
    }
}