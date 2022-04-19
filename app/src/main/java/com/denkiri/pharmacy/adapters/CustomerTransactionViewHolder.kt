package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.lang.ref.WeakReference

class CustomerTransactionViewHolder (type: Int, itemView: View, listener: OnCategoryItemClick): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == view  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnCategoryItemClick> = WeakReference(listener)
    var itemVew: View
    var customerName: TextView
    var addressValue: TextView
    var contactValue: TextView
    lateinit var view: ImageView

    init {
        this.itemVew=itemView
        customerName=itemView.findViewById(R.id.customerName)
        addressValue=itemView.findViewById(R.id.addressValue)
        contactValue=itemView.findViewById(R.id.contactValue)

        if (type==0) {
            view =itemView.findViewById(R.id.view)
            view.setOnClickListener(this)
        }
    }
}