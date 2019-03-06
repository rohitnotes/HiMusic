package com.wenhaiz.himusic.module.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.module.main.local.LocalFragment
import com.wenhaiz.himusic.module.main.online.OnLineFragment
import com.wenhaiz.himusic.module.search.SearchFragment
import com.wenhaiz.himusic.utils.LogUtil
import com.wenhaiz.himusic.utils.addFragmentToView
import com.wenhaiz.himusic.utils.removeFragment

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"

        @JvmStatic
        val PAGE_POSITION_MY_SONGS = 0
        const val PAGE_POSITION_ONLINE_SONGS = 1
        const val PAGE_COUNT = 2
    }

    @BindView(R.id.main_pager)
    lateinit var mPager: ViewPager
    @BindView(R.id.main_btn_search)
    lateinit var mBtnSearch: ImageButton
    @BindView(R.id.main_et_search)
    lateinit var mEtSearch: EditText
    @BindView(R.id.main_tab)
    lateinit var mTab: TabLayout
    @BindView(R.id.main_btn_cancel)
    lateinit var mCancelSearch: Button


    private lateinit var mUnBinder: Unbinder
    private lateinit var mPagerAdapter: MainPagerAdapter
    lateinit var searchFragment: SearchFragment
    private var textWatch: TextWatcher? = null
    var isTextChangedByUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LogUtil.d(TAG, "onPause")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_main, container, false)
        mUnBinder = ButterKnife.bind(this, contentView)
        initView()
        return contentView
    }

    fun initView() {
        mPagerAdapter = MainPagerAdapter(fragmentManager!!)
        mPager.adapter = mPagerAdapter

        mTab.setupWithViewPager(mPager)
        mTab.tabGravity = Gravity.NO_GRAVITY

    }

    @Suppress("DEPRECATION")
    @OnClick(R.id.main_btn_slide_menu, R.id.main_btn_search, R.id.main_btn_cancel)
    fun onButtonClick(v: View) {
        mEtSearch.clearFocus()
        when (v.id) {
            R.id.main_btn_slide_menu -> (activity as MainActivity).openDrawer()
            R.id.main_btn_search -> {
                showSearchBar()
            }
            R.id.main_btn_cancel -> {
                hideSearchBar()
            }
        }
    }

    fun hideSearchBar() {
        mTab.show()
        mBtnSearch.show()
        mEtSearch.hide()
        mEtSearch.removeTextChangedListener(textWatch)
        textWatch = null
        mCancelSearch.hide()
        removeFragment(fragmentManager!!, searchFragment)
        hideSoftInput()
    }

    fun hideSoftInput() {
        mEtSearch.clearFocus()
        val inputManager: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(mEtSearch.windowToken, 0)
    }

    @Suppress("DEPRECATION")
    private fun showSearchBar() {
        searchFragment = SearchFragment()
        addFragmentToView(fragmentManager!!, searchFragment, R.id.main_pager_container)

        mTab.hide()
        mBtnSearch.hide()
        mCancelSearch.show()
        mEtSearch.show()
        mEtSearch.setText("")
        showSoftInput()

        textWatch = object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val text = editable!!.toString()
                if (text == "") {
                    searchFragment.showSearchHistory()
                    mEtSearch.setTextColor(context!!.resources.getColor(R.color.colorGray))
                } else {
                    // if user typed keyword ,then load recommend keywords
                    if (isTextChangedByUser) {
                        searchFragment.showSearchRecommend(text)
                    } else {
                        isTextChangedByUser = true
                    }
                    mEtSearch.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        }
        mEtSearch.addTextChangedListener(textWatch)
        mEtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = mEtSearch.text.toString()
                if (!TextUtils.isEmpty(keyword)) {
                    searchFragment.beginSearch(keyword)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }

    private fun showSoftInput() {
        mEtSearch.requestFocus()
        val inputManager: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(mEtSearch, InputMethodManager.SHOW_FORCED)
    }

    fun setSearchKeyword(keyword: String) {
        isTextChangedByUser = false
        mEtSearch.setText(keyword)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mUnBinder.unbind()
    }


    inner class MainPagerAdapter(fragmentManager: FragmentManager)
        : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return if (position == PAGE_POSITION_ONLINE_SONGS) {
                OnLineFragment()
            } else {
                LocalFragment()
            }
        }

        override fun getCount(): Int = PAGE_COUNT

        override fun getPageTitle(position: Int): CharSequence {
            return if (position == PAGE_POSITION_MY_SONGS) {
                getString(R.string.main_mine)
            } else {
                getString(R.string.main_discover_music)
            }
        }
    }
}