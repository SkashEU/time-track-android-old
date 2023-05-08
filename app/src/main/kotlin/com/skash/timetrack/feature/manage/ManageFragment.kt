package com.skash.timetrack.feature.manage

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.skash.timetrack.R
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentManageBinding
import com.skash.timetrack.feature.adapter.ManageViewPagerAdapter

class ManageFragment : BindableFragment<FragmentManageBinding>(R.layout.fragment_manage) {

    private lateinit var adapter: ManageViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ManageViewPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(adapter.getTitleForPosition(position))
        }.attach()
    }

    override fun createBindingInstance(view: View): FragmentManageBinding {
        return FragmentManageBinding.bind(view)
    }

    override fun bindActions() {}
}