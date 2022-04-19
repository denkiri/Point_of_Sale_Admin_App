package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.utils.OnCategoryClick
class BrandItemAdapter  (private val type: Int, private  val context: Context, private  var modelList: List<Brand>?, private val recyclerListener: OnCategoryClick): RecyclerView.Adapter<BrandItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.select_brand_item,parent,false)
        return BrandItemViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: BrandItemViewHolder, position: Int) {
        val model= modelList!![position]
        holder.brand_title.text =model.brandName

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Brand>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}