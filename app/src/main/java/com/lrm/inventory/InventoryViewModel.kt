package com.lrm.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lrm.inventory.data.Item
import com.lrm.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getAllItems().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantity = itemQuantity.toInt()
        )
    }

    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemQuantity)
        insertItem(newItem)
    }


    fun isEntryValid(
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemQuantity.isBlank()) {
            return false
        }

        return true
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun sellItem(item: Item) {
        if (item.itemQuantity > 0) {
            val newItem = item.copy(itemQuantity = item.itemQuantity - 1)
            updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item): Boolean {
        return (item.itemQuantity > 0)
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantity = itemQuantity.toInt()
        )
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemQuantity)
        updateItem(updatedItem)
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}