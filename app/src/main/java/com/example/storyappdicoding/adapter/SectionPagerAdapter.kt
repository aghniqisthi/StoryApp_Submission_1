package com.example.storyappdicoding.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.storyappdicoding.ui.LoginFragment
import com.example.storyappdicoding.ui.RegisterFragment

class SectionPagerAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> LoginFragment()
        }
    }

    override fun getItemCount(): Int = 2

}