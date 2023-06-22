package com.lrm.inventory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lrm.inventory.InventoryApplication
import com.lrm.inventory.InventoryViewModel
import com.lrm.inventory.InventoryViewModelFactory
import com.lrm.inventory.R
import com.lrm.inventory.data.Item
import com.lrm.inventory.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    lateinit var item: Item

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {
            this.findNavController().navigateUp()
        }

        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    private fun bind(item: Item) {
        binding.apply {
            itemName.text = item.itemName
            itemPrice.text = item.itemPrice.toString()
            itemQuantity.text = item.itemQuantity.toString()
            sellButton.isEnabled = viewModel.isStockAvailable(item)
            sellButton.setOnClickListener { viewModel.sellItem(item) }
            deleteButton.setOnClickListener { showConfirmationDialog() }
            editItemFab.setOnClickListener { editItem() }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) {_, _ -> }
            .setPositiveButton(getString(R.string.yes)) {_, _ ->
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(item.id)
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}