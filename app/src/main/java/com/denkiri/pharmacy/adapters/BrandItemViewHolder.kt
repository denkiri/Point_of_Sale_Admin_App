package com.denkiri.pharmacy.adapters
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryClick
import java.lang.ref.WeakReference
class BrandItemViewHolder (type: Int, itemView: View, listener: OnCategoryClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == brand_title ) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnCategoryClick> = WeakReference(listener)
    var itemVew: View
    var brand_title: TextView
    init {
        this.itemVew=itemView
        brand_title=itemView.findViewById(R.id.brand)

        if (type==0) {

            brand_title.setOnClickListener(this)
        }
    }
}