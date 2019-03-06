package com.wenhaiz.himusic.base

import android.content.Context

interface BaseView<in T> {
    fun setPresenter(presenter: T)
    fun initView()
    fun onLoading()
    fun onFailure(msg: String)
    fun getViewContext(): Context
}