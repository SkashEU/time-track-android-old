package com.skash.timetrack.feature.overview

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.skash.timetrack.R
import com.skash.timetrack.core.util.BindableFragment
import com.skash.timetrack.databinding.FragmentOverviewBinding
import com.skash.timetrack.feature.adapter.OverviewViewPagerAdapter

class OverviewFragment : BindableFragment<FragmentOverviewBinding>(R.layout.fragment_overview) {

    private lateinit var adapter: OverviewViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OverviewViewPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(adapter.getTitleForPosition(position))
        }.attach()
    }

    override fun createBindingInstance(view: View): FragmentOverviewBinding {
        return FragmentOverviewBinding.bind(view)
    }

    override fun bindActions() {}
}
