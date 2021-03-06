package com.example.events.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.events.ui.adapter.EventsFavoritesAdapter
import com.example.events.databinding.FragmentFavoritesBinding
import com.example.events.ui.extensions.hideComponent
import com.example.events.ui.extensions.showComponent
import com.example.events.ui.extensions.startDetail
import com.example.events.data.model.Event
import com.example.events.ui.viewmodel.EventsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentFavorites : Fragment() {


    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by sharedViewModel()
    private lateinit var recyclerFavorites: RecyclerView
    private lateinit var layoutEmptyFavorites: ConstraintLayout
    private lateinit var adapter: EventsFavoritesAdapter
    private val listFavorites by lazy {
        mutableListOf<Event>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupAdapter()
        observeListFavorites()
    }

    private fun observeListFavorites() {
        viewModel.listFavorites.observe(viewLifecycleOwner) { favorites ->
            attListFavorites(favorites)
        }
    }

    private fun attListFavorites(favorites: List<Event>?) {
        listFavorites.clear()
        if (favorites?.isNotEmpty() == true) {
            layoutEmptyFavorites.hideComponent()
            listFavorites.addAll(favorites)
        } else {
            layoutEmptyFavorites.showComponent()
        }
        adapter.notifyDataSetChanged()
    }

    private fun setupViews() {
        layoutEmptyFavorites = binding.favoritesEmptyLayout
        recyclerFavorites = binding.recyclerFavorites
    }

    private fun setupAdapter() {
        adapter = EventsFavoritesAdapter(requireContext(), listFavorites)
        adapter.onItemClick = { event, itemView ->
            verifyClickOpenDetailOrSetFavorites(itemView, event)
        }
        recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        recyclerFavorites.adapter = adapter
    }

    private fun verifyClickOpenDetailOrSetFavorites(
        itemView: Boolean,
        event: Event
    ) {
        if (!itemView) {
            event.favorites = !event.favorites
            viewModel.alterToFavorites(event)
        } else {
            this.activity?.startDetail(event.id)
        }
    }
}