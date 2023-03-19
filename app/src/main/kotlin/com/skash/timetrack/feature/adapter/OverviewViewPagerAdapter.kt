package com.skash.timetrack.feature.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skash.timetrack.R
import com.skash.timetrack.feature.overview.task.TasksOverviewFragment
import com.skash.timetrack.feature.overview.worktime.WorkTimeOverviewFragment

class OverviewViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TasksOverviewFragment()
            1 -> WorkTimeOverviewFragment()
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }

    fun getTitleForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_overview_tasks
            1 -> R.string.title_overview_work_time
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }
}