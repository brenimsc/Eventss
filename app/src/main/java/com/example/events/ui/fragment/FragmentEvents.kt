package com.example.events.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.events.adapter.EventsAdapter
import com.example.events.databinding.FragmentEventsBinding
import com.example.events.ui.extensions.*
import com.example.events.data.model.Event
import com.example.events.ui.viewmodel.EventsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentEvents : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by sharedViewModel()
    private lateinit var adapter: EventsAdapter
    private lateinit var layoutEmptList: ConstraintLayout
    private lateinit var layoutError: ConstraintLayout
    private lateinit var buttonRecarregar: Button
    private lateinit var recyclerEvents: RecyclerView
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
    private val listEvents by lazy {
        mutableListOf<Event>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setupAdapter()
        showProgressDialog()
        observeLoading()
        observeError()
        observeListEvents()
        getEvents()
    }

    private fun getEvents() {
        viewModel.getListEvents(false)
    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error) {
                showLayoutError()
            } else {
                hideLayoutError()
            }
        }
    }

    private fun observeListEvents() {
        viewModel.listEvents.observe(viewLifecycleOwner) { list ->
            hideProgressDialog()
            attListEvents(list)
        }
    }

    private fun hideLayoutError() {
        recyclerEvents.showComponent()
        layoutError.hideComponent()
    }

    private fun hideProgressDialog() {
        progressDialog.hideDialog()
    }

    private fun showLayoutError() {
        binding.recyclerEvents.hideComponent()
        layoutError.showComponent()
    }

    private fun showProgressDialog() {
        progressDialog.showDialog()
    }

    private fun attListEvents(it: List<Event>) {
        listEvents.clear()
        if (it.isNotEmpty()) {
            layoutEmptList.hideComponent()
            listEvents.addAll(it)
        } else {
            layoutEmptList.showComponent()
        }
        adapter.notifyDataSetChanged()
    }


    private fun setupViews() {
        layoutEmptList = binding.eventsEmptyLayout
        layoutError = binding.eventsErrorLayout
        buttonRecarregar = binding.btnReload
        recyclerEvents = binding.recyclerEvents

        buttonRecarregar.setOnClickListener {
            viewModel.getListEvents(true)
        }
    }

    private fun setupAdapter() {
        adapter = EventsAdapter(requireContext(), listEvents)
        adapter.onItemClick = { id ->
            this.activity?.startDetail(id)
        }
        binding.recyclerEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEvents.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}