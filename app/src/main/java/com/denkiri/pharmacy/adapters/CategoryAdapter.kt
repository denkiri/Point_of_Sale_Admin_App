package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnSupplierItemClick

class CategoryAdapter  (private val type: Int,private  val context: Context, private  var modelList: List<Category>?, private val recyclerListener: OnSupplierItemClick):RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var itemView:View? =null
        itemView =LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model= modelList!![position]
        holder.category_title.text =model.categoryName
        if(model.status==1){
            holder.disable.visibility=View.VISIBLE
            holder.enable.visibility=View.INVISIBLE

        }
        else{
            holder.enable.visibility=View.VISIBLE
            holder.disable.visibility=View.INVISIBLE
        }

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Category>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}