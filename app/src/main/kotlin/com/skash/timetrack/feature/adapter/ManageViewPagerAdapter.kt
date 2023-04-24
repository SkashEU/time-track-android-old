package com.skash.timetrack.feature.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skash.timetrack.R
import com.skash.timetrack.feature.manage.client.ClientsFragment
import com.skash.timetrack.feature.manage.project.ProjectsFragment
import com.skash.timetrack.feature.manage.team.TeamFragment

class ManageViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClientsFragment()
            1 -> ProjectsFragment()
            2 -> TeamFragment()
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }

    fun getTitleForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_manage_clients
            1 -> R.string.title_manage_projects
            2 -> R.string.title_manage_team
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }
}