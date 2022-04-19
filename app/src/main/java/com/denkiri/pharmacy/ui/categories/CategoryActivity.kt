package com.denkiri.pharmacy.ui.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denkiri.pharmacy.R

class CategoryActivity : AppCompatActivity() {
    var categoryId: Int? = 0
    var categoryName: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)
        categoryId = intent.getIntExtra("categoryId", 0)
        categoryName=intent.getStringExtra("categoryName")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EditFragment.newInstance())
                    .commitNow()
        }

    }
}