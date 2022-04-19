package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.utils.OnSupplierItemClick

class BrandAdapter  (private val type: Int, private  val context: Context, private  var modelList: List<Brand>?, private val recyclerListener: OnSupplierItemClick):
    RecyclerView.Adapter<BrandViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.brand_item,parent,false)
        return BrandViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val model= modelList!![position]
        holder.brand_title.text =model.brandName
        if(model.status==1){
            holder.disable.visibility= View.VISIBLE
            holder.enable.visibility= View.INVISIBLE

        }
        else{
            holder.enable.visibility= View.VISIBLE
            holder.disable.visibility= View.INVISIBLE
        }

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Brand>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}