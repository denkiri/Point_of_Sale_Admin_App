package com.denkiri.pharmacy.ui.supplier
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.SupplierAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.supplier.Supplier
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.ui.home.HomeFragment
import com.denkiri.pharmacy.ui.receipt.ReceiptActivity
import com.denkiri.pharmacy.utils.OnSupplierItemClick
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.supplier_fragment.*
class SupplierFragment : Fragment() {
    internal  var number:String?=""
    lateinit var supplierAdapter: SupplierAdapter
    private lateinit var suppliers: List<Supplier>
    val addSupplierFragment = AddSupplierFragment()

    companion object {
        fun newInstance() = SupplierFragment()
    }

    private lateinit var viewModel: SupplierViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.supplier_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)
                if (dy > 0 && fab.visibility === View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility !== View.VISIBLE) {
                    fab.show()
                }
            }
        })
        initView()
        init()
        observers()
        fab.setOnClickListener { //  val navController = Navigation.findNavController(requireView())
         //  navController.navigate(R.id.nav_add_supplier)
           requireActivity().supportFragmentManager.beginTransaction()
                   .replace(R.id.container, AddSupplierFragment())
                    .commitNow()
            }
        back.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow()  }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
         //   observers()
         //   initView()
            itemsswipetorefresh.isRefreshing = false
        }
        orderList.setOnClickListener {   val fragment = PurchasesOrderFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow() }
    }
    fun init() {
        if (NetworkUtils.isConnected(requireContext())) {
        viewModel.getSuppliers()
    }}
    private fun initView(){
        suppliers = ArrayList()
        supplierAdapter = context?.let {
            SupplierAdapter(0,it, suppliers, object : OnSupplierItemClick {

                override fun delete(pos: Int) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Supplier")
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteSupplier((suppliers as ArrayList<Supplier>)[pos].suplierId.toString())
                    }
                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                }

                override fun edit(pos: Int) {
                 /*   val intent = Intent(activity, SupplierActivity::class.java)
                    intent.putExtra("supplierId", (suppliers as ArrayList<Supplier>)[pos].suplierId)
                    intent.putExtra("supplierName",(suppliers as ArrayList<Supplier>)[pos].suplierName)
                    intent.putExtra("contactPerson",(suppliers as ArrayList<Supplier>)[pos].contactPerson)
                    intent.putExtra("supplierContact",(suppliers as ArrayList<Supplier>)[pos].suplierContact)
                    intent.putExtra("supplierAddress",(suppliers as ArrayList<Supplier>)[pos].suplierAddress)
                    startActivity(intent)*/
                    val bundle = Bundle()
                    bundle.putInt( "supplierId", (suppliers as ArrayList<Supplier>)[pos].suplierId)
                    bundle.putString( "supplierName" , (suppliers as ArrayList<Supplier>)[pos].suplierName)
                    bundle.putString( "contactPerson" , (suppliers as ArrayList<Supplier>)[pos].contactPerson)
                    bundle.putString( "supplierContact" , (suppliers as ArrayList<Supplier>)[pos].suplierContact)
                    bundle.putString( "supplierAddress" , (suppliers as ArrayList<Supplier>)[pos].suplierAddress)
                  //  val navController = Navigation.findNavController(requireView())
                   // navController.navigate(R.id.nav_edit_supplier, bundle)
                    val fragment = EditSupplierFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()
                }

                override fun dial(pos: Int) {
                    number = (suppliers as ArrayList<Supplier>)[pos].suplierContact?.trim()
                    //Dialer intent
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
                    startActivity(intent)

                }

                override fun view(pos: Int) {
                    val bundle = Bundle()
                    bundle.putInt( "supplierId", (suppliers as ArrayList<Supplier>)[pos].suplierId)
                    bundle.putString("supplierName", (suppliers as ArrayList<Supplier>)[pos].suplierName)
                    val fragment = SupplierProductsFragment()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()
                }

                override fun enable(pos: Int) {
                    viewModel.activateSupplier(suppliers[pos].suplierId.toString())

                }

                override fun disable(pos: Int) {
                    viewModel.deactivateSupplier(suppliers[pos].suplierId.toString())
                }

                override fun onClickListener(position1: Int) {
                    if (suppliers[position1].status==1) {
                        if (suppliers[position1].suplierId.toString()== PreferenceManager(requireContext()).getSupplierId() ||  PreferenceManager(requireContext()).getSupplierId()=="0" ){
                        val intent = Intent(activity, PurchaseOrderActivity::class.java)
                        intent.putExtra("supplierId", suppliers[position1].suplierId.toString())
                        intent.putExtra("supplierName", suppliers[position1].suplierName.toString())
                        startActivity(intent)
                    }
                    else{

                            view?.let { Snackbar.make(it,"Save Invoice Items", Snackbar.LENGTH_LONG).show() }

                        }
                    }
                    else{
                        view?.let { Snackbar.make(it,"Activate Supplier", Snackbar.LENGTH_LONG).show() }
                    }
                }

                override fun onLongClickListener(position1: Int) {
                    TODO("Not yet implemented")
                }
            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = supplierAdapter
        supplierAdapter.notifyDataSetChanged()
    }
    private fun setStatus(data: Resource<SupplierData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
               // empty_text.text = data.message
               // view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusB(data: Resource<SupplierData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }


        if (status == Status.ERROR) {
            if (data.message != null) {
                fab.visibility=View.GONE
                // empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            //   empty_layout.visibility = View.VISIBLE
            main.visibility = View.VISIBLE
            //    empty_button.setOnClickListener({ init() })
        }
    }
    private fun setUpSuppliers(suppliers: ArrayList<Supplier>) {
        this.suppliers = suppliers
        productsTotal.text= suppliers.size.toString()
        supplierAdapter.refresh(this.suppliers)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    fun observers(){
        viewModel.observeSuppliers().observe(viewLifecycleOwner, {
            setStatus(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.suppliers != null && !it.data.suppliers!!.isEmpty()) {
                    setUpSuppliers(it.data.suppliers as ArrayList<Supplier>)
                } else {
                    setUpSuppliers(ArrayList())
                }
            }

        })
        viewModel.observeSupplierAction().observe(viewLifecycleOwner, {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.suppliers != null && !it.data.suppliers!!.isEmpty()) {
                    setUpSuppliers(it.data.suppliers as ArrayList<Supplier>)
                } else {
                    setUpSuppliers(ArrayList())
                }
            }

        })
    }


}