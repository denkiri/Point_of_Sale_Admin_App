package com.denkiri.pharmacy.ui.reports
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReportData
import com.denkiri.pharmacy.storage.repository.InventoryRepository
class InventoryReportViewModel (application: Application) : AndroidViewModel(application){
    internal var inventoryRepository: InventoryRepository
    private val inventoryObservable = MediatorLiveData<Resource<InventoryReportData>>()
    init {
        inventoryRepository = InventoryRepository(application)
        inventoryObservable.addSource(inventoryRepository.inventoryObservable) { data -> inventoryObservable.setValue(data) }
    }
    fun getInventoryReport() {
        inventoryRepository.inventoryReport()
    }
    fun observeInventory(): LiveData<Resource<InventoryReportData>> {
        return inventoryObservable
    }
}