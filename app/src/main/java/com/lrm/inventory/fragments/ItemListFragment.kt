package com.lrm.inventory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lrm.inventory.InventoryApplication
import com.lrm.inventory.InventoryViewModel
import com.lrm.inventory.InventoryViewModelFactory
import com.lrm.inventory.adapter.ItemListAdapter
import com.lrm.inventory.databinding.FragmentItemListBinding

class ItemListFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItemFab.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment()
            this.findNavController().navigate(action)
        }

        val adapter = ItemListAdapter {
            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        viewModel.allItems.observe(this.viewLifecycleOwner) {items ->
            items.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}