package dev.jvitor.convidado.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.jvitor.convidado.databinding.FragmentAllGuestBinding
import dev.jvitor.convidado.model.DataBaseConstants
import dev.jvitor.convidado.view.adapter.GuestsAdapter
import dev.jvitor.convidado.view.listener.OnGuestListener
import dev.jvitor.convidado.viewmodel.GuestsViewModel

class AllGuestsFragment : Fragment() {

    private var _binding: FragmentAllGuestBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GuestsViewModel
    private val adapter = GuestsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {

        viewModel = ViewModelProvider(this)[GuestsViewModel::class.java]

        _binding = FragmentAllGuestBinding.inflate(inflater, container, false)

        binding.recyclerAllGuests.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllGuests.adapter = adapter

        val listener = object : OnGuestListener{
            override fun onClick(id: Int) {
                val intent = Intent(context, GuestFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(DataBaseConstants.GUEST_ID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDelete(id: Int) {
                viewModel.delete(id)
                viewModel.getAll()
            }
        }

        adapter.attachListener(listener)

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.guests.observe(viewLifecycleOwner) {
            adapter.updateGuests(it)
        }
    }
}