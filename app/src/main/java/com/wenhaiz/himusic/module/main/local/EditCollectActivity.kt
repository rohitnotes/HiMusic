package com.wenhaiz.himusic.module.main.local

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.wenhaiz.himusic.BuildConfig
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Collect_
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.showToast
import com.wenhaiz.himusic.module.detail.DetailFragment
import com.wenhaiz.himusic.utils.BoxUtil
import io.objectbox.Box

class EditCollectActivity : AppCompatActivity() {

    lateinit var mUnbinder: Unbinder

    @BindView(R.id.action_bar_title)
    lateinit var mTitle: TextView
    @BindView(R.id.collect_title)
    lateinit var collectTitle: EditText
    @BindView(R.id.collect_cover)
    lateinit var collectCover: ImageView
    @BindView(R.id.collect_intro)
    lateinit var collectIntro: EditText

    var action: String = ""
    var collectId: Long = 0
    var mCollect: Collect? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collect)
        action = intent.action
        mUnbinder = ButterKnife.bind(this)
        initView()
    }

    private fun initView() {
        if (action == ACTION_CREATE) {
            mTitle.text = getString(R.string.create_collect)
        } else {
            mTitle.text = getString(R.string.edit_collect)
            collectId = intent.getLongExtra("collectId", 0)
            loadCollect()
        }

    }


    private fun loadCollect() {
        val collectBox = BoxUtil.getBoxStore(this).boxFor(Collect::class.java)
        mCollect = collectBox.query().equal(Collect_.id, collectId).build().findUnique()
        collectTitle.setText(mCollect?.title)
        collectIntro.setText(mCollect?.desc)
        //todo:显示封面
//        collectCover
    }

    @OnClick(R.id.action_bar_back, R.id.save, R.id.collect_cover)
    fun onClick(view: View) {
        when (view.id) {
            R.id.action_bar_back -> {
                finish()
            }

            R.id.save -> {
                if (collectTitle.text.isEmpty()) {
                    showToast("标题不能为空")
                } else {
                    saveCollect()
                }
            }
            R.id.collect_cover -> {
                //todo:从系统相册选择封面
            }
        }
    }

    private fun saveCollect() {
        val title = collectTitle.text.toString()
        val intro = collectIntro.text.toString()
//        val collectDao = BoxUtil.getSession(this).collectDao
        val collectBox = BoxUtil.getBoxStore(this).boxFor(Collect::class.java)
        val existCollect = collectBox.query().equal(Collect_.title, title).build().findUnique()
        //名称不重复时保存歌单
        if (action == ACTION_CREATE && existCollect == null) {
            insertCollect(title, intro, collectBox)
            finish()
        } else if (action == ACTION_UPDATE && (existCollect == null || existCollect.id == mCollect?.id)) {
            updateCollect(title, intro, collectBox)
            setResult(DetailFragment.RESULT_UPDATED)
            finish()
        } else {
            showToast("歌单已存在")
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun insertCollect(title: String, intro: String, collectBox: Box<Collect>) {
        mCollect = Collect()
        mCollect?.isFromUser = true
        //为了和网络加载的时间保持格式统一
        mCollect?.createDate = System.currentTimeMillis() / 1000
        mCollect?.updateDate = System.currentTimeMillis() / 1000
        mCollect?.title = title
        mCollect?.desc = intro
        val collectId = collectBox.put(mCollect)
        if (collectId > 0) {
            if (intent.hasExtra("song")) {
                addSongToCollect(mCollect!!)
            }
            setResult(LocalFragment.RESULT_COLLECT_CREATED)
            finish()
        } else {
            showToast("新建歌单失败，请重试")
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun updateCollect(title: String, intro: String, collectBox: Box<Collect>) {
        mCollect?.title = title
        mCollect?.desc = intro
        collectBox.put(mCollect)
    }

    private fun addSongToCollect(collect: Collect) {
        val song = intent.getSerializableExtra("song") as Song
        collect.songs.add(song)
        val collectBox = BoxUtil.getBoxStore(this).boxFor(Collect::class.java)
        if (collectBox.put(collect) > 0) {
            showToast("添加成功")
            //刷新数据，防止缓存导致的数据不更新
        } else {
            showToast("添加失败")
        }
    }

    override fun onResume() {
        super.onResume()
        showSoftInput()
    }

    private fun showSoftInput() {
        collectTitle.clearFocus()
        collectTitle.requestFocus()
        collectTitle.setSelection(collectTitle.length())
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(collectTitle, InputMethodManager.SHOW_FORCED)
    }

    override fun onPause() {
        super.onPause()
        hideSoftInput()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder.unbind()
    }

    private fun hideSoftInput() {
        collectTitle.clearFocus()
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(collectTitle.windowToken, 0)
    }

    companion object {
        const val ACTION_UPDATE = "${BuildConfig.APPLICATION_ID}.UPDATE"
        const val ACTION_CREATE = "${BuildConfig.APPLICATION_ID}.CREATE"
    }
}
