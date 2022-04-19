package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryClick
import java.lang.ref.WeakReference
class CategoryItemViewHolder (type: Int, itemView: View, listener: OnCategoryClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == category_title ) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }


    }
    private val listenerWeakReference: WeakReference<OnCategoryClick> = WeakReference(listener)
    var itemVew: View
    var category_title: TextView
    init {
        this.itemVew=itemView
        category_title=itemView.findViewById(R.id.category)

        if (type==0) {

            category_title.setOnClickListener(this)
        }
    }
}