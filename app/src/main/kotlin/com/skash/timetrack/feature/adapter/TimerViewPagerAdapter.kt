package com.skash.timetrack.feature.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skash.timetrack.R
import com.skash.timetrack.feature.timer.project.ProjectTimeFragment
import com.skash.timetrack.feature.timer.worktime.WorkTimeFragment

class TimerViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProjectTimeFragment()
            1 -> WorkTimeFragment()
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }

    fun getTitleForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_project_timer
            1 -> R.string.title_work_time_timer
            else -> throw IllegalArgumentException("Valid positions are 0 - ${itemCount - 1}")
        }
    }
}