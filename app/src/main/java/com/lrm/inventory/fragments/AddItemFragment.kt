package com.lrm.inventory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lrm.inventory.InventoryApplication
import com.lrm.inventory.InventoryViewModel
import com.lrm.inventory.InventoryViewModelFactory
import com.lrm.inventory.R
import com.lrm.inventory.data.Item
import com.lrm.inventory.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private val navigationArgs: AddItemFragmentArgs by navArgs()
    lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {
            this.findNavController().navigateUp()
        }

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {selectedItem ->
                item = selectedItem
                bind(item)
            }
            binding.fragmentLabel.text = getString(R.string.edit_item)
        } else {
            binding.fragmentLabel.text = getString(R.string.add_item)
            binding.saveButton.setOnClickListener {
                addNewItem()
            }
        }

        binding.cancelButton.setOnClickListener {
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemQuantity.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemQuantity.text.toString()
            )

            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    private fun bind(item: Item) {
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(item.itemPrice.toString(), TextView.BufferType.SPANNABLE)
            itemQuantity.setText(item.itemQuantity.toString(), TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener { updateItem() }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemQuantity.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}