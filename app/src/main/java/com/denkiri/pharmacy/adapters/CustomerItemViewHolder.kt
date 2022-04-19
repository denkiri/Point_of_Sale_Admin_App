package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnCustomerItemClick
import java.lang.ref.WeakReference

class CustomerItemViewHolder (type: Int, itemView: View, listener: OnCustomerItemClick): RecyclerView.ViewHolder(itemView),
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
    }
    private val listenerWeakReference: WeakReference<OnCustomerItemClick> = WeakReference(listener)
    var itemVew: View
    var customerName: TextView
    var addressValue:TextView
    var contactValue:TextView
    var status:TextView
    var statusB:TextView
    var enable:Button
    var disable:Button
    lateinit var edit: ImageView
    lateinit var delete: ImageView
    init {
        this.itemVew=itemView
        customerName=itemView.findViewById(R.id.customerName)
        addressValue=itemView.findViewById(R.id.addressValue)
        contactValue=itemView.findViewById(R.id.contactValue)
        status=itemView.findViewById(R.id.status)
        statusB=itemView.findViewById(R.id.status2)
        enable=itemView.findViewById(R.id.enable)
        disable=itemView.findViewById(R.id.disable)
        if (type==0) {
            delete = itemView.findViewById(R.id.delete)
            delete.setOnClickListener(this)
            edit =itemView.findViewById(R.id.edit)
            edit.setOnClickListener(this)
            enable=itemView.findViewById(R.id.enable)
            enable.setOnClickListener(this)
            disable=itemView.findViewById(R.id.disable)
            disable.setOnClickListener(this)
        }
    }
}