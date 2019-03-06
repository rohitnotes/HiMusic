package com.wenhaiz.himusic.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.wenhaiz.himusic.R

/**
 * 对 Fragment 执行操作的公用方法
 *
 * Created by Wenhai on 2017/7/30.
 */

fun addFragmentToActivity(fragmentManager: android.support.v4.app.FragmentManager, fragment: android.support.v4.app.Fragment, id: Int) {
    fragmentManager.beginTransaction().add(id, fragment).commit()
}


fun addFragmentToView(fragmentManager: FragmentManager, fragment: Fragment, viewId: Int) {
    fragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(viewId, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
}


fun addFragmentToMainView(fragmentManager: FragmentManager, fragment: Fragment) {
    addFragmentToView(fragmentManager, fragment, R.id.main_container)
}


fun removeFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .remove(fragment)
            .commit()
    fragmentManager.popBackStack()
}
