package com.skash.timetrack.feature.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skash.timetrack.R
import com.skash.timetrack.feature.settings.project.ProjectsFragment
import com.skash.timetrack.feature.settings.user.UserFragment

class SettingsViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProjectsFragment()
            1 -> UserFragment()
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }

    fun getTitleForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_project_settings
            1 -> R.string.title_user_settings
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }
}