package com.example.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.events.EventsChekinAdapter
import com.example.events.databinding.FragmentCheckinsBinding
import com.example.events.extensions.hideComponent
import com.example.events.extensions.showComponent
import com.example.events.extensions.startDetail
import com.example.events.model.Event
import com.example.events.viewmodel.EventsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentCheckins : Fragment() {

    private var _binding: FragmentCheckinsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by sharedViewModel()
    private lateinit var adapter: EventsChekinAdapter
    private lateinit var emptyLayout: ConstraintLayout
    private lateinit var recyclerCheckins: RecyclerView
    private val listEvents by lazy {
        mutableListOf<Event>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupAdapter()
        observeListCheckin()
    }

    private fun observeListCheckin() {
        viewModel.listCheckins.observe(viewLifecycleOwner) { listCheckins ->
            attList(listCheckins)
        }
    }

    private fun attList(listCheckins: List<Event>?) {
        listEvents.clear()
        if (listCheckins?.isNotEmpty() == true) {
            emptyLayout.hideComponent()
            listCheckins?.let {
                listEvents.addAll(it)
            }
        } else {
            listEvents.clear()
            emptyLayout.showComponent()
        }
        adapter.notifyDataSetChanged()
    }

    private fun setupViews() {
        emptyLayout = binding.checkinEmptyLayout
        recyclerCheckins = binding.recyclerCheckins
    }

    private fun setupAdapter() {
        adapter = EventsChekinAdapter(requireContext(), listEvents)
        adapter.onItemClick = { idEvent ->
            this.activity?.startDetail(idEvent)
        }
        recyclerCheckins.adapter = adapter
    }


}