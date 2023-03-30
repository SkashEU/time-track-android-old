package com.skash.timetrack.feature.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.skash.timetrack.R
import com.skash.timetrack.databinding.FragmentManageBinding
import com.skash.timetrack.feature.adapter.ManageViewPagerAdapter

class ManageFragment : Fragment(R.layout.fragment_manage) {

    private var _binding: FragmentManageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ManageViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentManageBinding.bind(view)
        adapter = ManageViewPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(adapter.getTitleForPosition(position))
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}