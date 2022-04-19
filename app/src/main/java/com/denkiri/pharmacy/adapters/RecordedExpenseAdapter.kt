package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.expense.Income
import com.denkiri.pharmacy.models.expense.RecordedExpense
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import java.text.NumberFormat
import java.util.*

class RecordedExpenseAdapter(private val type: Int, private  val context: Context, private  var modelList: List<RecordedExpense>?, private val recyclerListener: RecordedExpenseItemClick):
    RecyclerView.Adapter<RecordedExpenseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecordedExpenseViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense,parent,false)
        return RecordedExpenseViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder:RecordedExpenseViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.expenseTitle.text =model.expenseName
        holder.expenseAmount.text =format.format(model.amount).toString()
        holder.date.text=model.date
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<RecordedExpense>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredExpenseList: ArrayList<RecordedExpense>) {
        this.modelList = filteredExpenseList
        notifyDataSetChanged()
    }
}