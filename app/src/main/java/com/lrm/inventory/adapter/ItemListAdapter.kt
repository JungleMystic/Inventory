package com.lrm.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lrm.inventory.data.Item
import com.lrm.inventory.databinding.ListItemBinding

class ItemListAdapter(
    val onItemClicked: (Item) -> Unit
): ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private var binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.itemPrice.toString()
                itemQuantity.text = item.itemQuantity.toString()
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemName == newItem.itemName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onItemClicked(currentItem)
        }
    }
}