package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.cashier.Users
import com.denkiri.pharmacy.utils.OnCustomerItemClick
class UserAdapter(private val type: Int, private  val context: Context, private  var modelList: List<Users>?, private val recyclerListener: OnCustomerItemClick): RecyclerView.Adapter<UserItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.cashier_item,parent,false)
        return UserItemViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val model= modelList!![position]
        holder.name.text =model.cashierName
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Users>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}