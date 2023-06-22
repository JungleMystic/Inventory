package com.lrm.inventory

import android.app.Application
import com.lrm.inventory.data.ItemRoomDatabase

class InventoryApplication: Application() {

    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}