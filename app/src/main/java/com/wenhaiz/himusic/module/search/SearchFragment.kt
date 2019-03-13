package com.wenhaiz.himusic.module.search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.SearchHistory
import com.wenhaiz.himusic.data.bean.SearchHistory_
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.http.data.SearchTip
import com.wenhaiz.himusic.module.main.MainFragment
import com.wenhaiz.himusic.module.play.service.PlayProxy
import com.wenhaiz.himusic.utils.BoxUtil
import com.wenhaiz.himusic.widget.SongOpsDialog

class SearchFragment : Fragment(), SearchContract.View {

    @BindView(R.id.search_view)
    lateinit var mSearchView: LinearLayout
    @BindView(R.id.search_begin_search)
    lateinit var mTvBeginSearch: TextView
    //    @BindView(R.id.hot_search)
//    lateinit var mHotSearch: LinearLayout
    @BindView(R.id.search_content_list)
    lateinit var mContentList: RecyclerView
    @BindView(R.id.loading)
    lateinit var mLoading: LinearLayout
    @BindView(R.id.loading_failed)
    lateinit var mLoadFailed: LinearLayout

    private lateinit var mUnBinder: Unbinder
    lateinit var mPresenter: SearchContract.Presenter
    var searchKeyword = ""
    private lateinit var resultSongs: List<Song>
    private var currentContent = CONTENT_SEARCH_HISTORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SearchPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_search, container, false)
        mUnBinder = ButterKnife.bind(this, contentView)
        initView()
        return contentView
    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        mPresenter = presenter
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun initView() {
        showSearchHistory()
    }


    @OnClick(R.id.search_begin_search, R.id.loading_failed)
    fun onClick(view: View) {
        when (view.id) {
            R.id.search_begin_search -> {
                //搜索
                beginSearch(searchKeyword)
            }
            R.id.loading_failed -> {
                //重新搜索
                beginSearch(searchKeyword)
            }
        }
    }

    fun beginSearch(keyword: String) {
        if (!TextUtils.isEmpty(keyword)) {
            val mainFragment: MainFragment = fragmentManager!!.findFragmentById(R.id.main_container) as MainFragment
            mainFragment.hideSoftInput()
            mPresenter.searchByKeyWord(keyword)
            saveSearchHistory(keyword)
            currentContent = CONTENT_SEARCH_RESULT
        }
    }

    private fun saveSearchHistory(keyword: String) {
        val box = BoxUtil.getBoxStore(context!!).boxFor(SearchHistory::class.java)
        val queryResult = box.query().equal(SearchHistory_.keyword, keyword).build().find()
        if (queryResult.size > 0) {
            val searchHistory = queryResult[0]
            searchHistory.searchTime = System.currentTimeMillis()
            box.put(searchHistory)
        } else {
            val newSearch = SearchHistory(null, keyword, System.currentTimeMillis())
            box.put(newSearch)
        }
    }

    fun showSearchHistory() {
        currentContent = CONTENT_SEARCH_HISTORY
//        mHotSearch.show()
        mSearchView.hide()
        val box = BoxUtil.getBoxStore(context!!).boxFor(SearchHistory::class.java)
        val searchHistory = box.query().notEqual(SearchHistory_.keyword, "").orderDesc(SearchHistory_.searchTime)
                .build().find()
        if (mContentList.adapter is SearchHistoryAdapter) {
            (mContentList.adapter as SearchHistoryAdapter).setData(searchHistory)
        } else {
            mContentList.adapter = SearchHistoryAdapter(searchHistory)
            mContentList.layoutManager = LinearLayoutManager(context)
        }
    }

    fun showSearchRecommend(keyword: String) {
        searchKeyword = keyword
        mSearchView.show()
        val display = "搜索\"$keyword\""
        mTvBeginSearch.text = display
        currentContent = CONTENT_RECOMMEND_KEYWORD
        mPresenter.loadSearchRecommend(searchKeyword)
    }

    override fun onSearchRecommendLoaded(recommends: List<SearchTip>) {
        if (currentContent == CONTENT_RECOMMEND_KEYWORD) {
            activity!!.runOnUiThread {
                if (mContentList.adapter is SearchRecommendAdapter) {
                    (mContentList.adapter as SearchRecommendAdapter).setData(recommends)
                } else {
                    mContentList.layoutManager = LinearLayoutManager(context)
                    mContentList.adapter = SearchRecommendAdapter(recommends)
                }
            }
        }
    }

    override fun onSearchResult(songs: List<Song>) {
        if (currentContent == CONTENT_SEARCH_RESULT) {
            mSearchView.hide()
            resultSongs = songs
            mContentList.adapter = ResultSongsAdapter(resultSongs)
            mContentList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            mLoading.hide()
            mContentList.show()
        }
    }

    override fun onLoading() {
        mLoading.show()
        mContentList.hide()
        mLoadFailed.hide()
    }

    override fun onFailure(msg: String) {
        activity!!.runOnUiThread {
            mLoadFailed.show()
            mLoading.hide()
            mContentList.hide()
            mSearchView.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainFragment: MainFragment = fragmentManager!!.findFragmentById(R.id.main_container) as MainFragment
        mainFragment.hideSearchBar()
        mUnBinder.unbind()
    }

    companion object {
        const val CONTENT_SEARCH_HISTORY = 0x00
        const val CONTENT_RECOMMEND_KEYWORD = 0x01
        const val CONTENT_SEARCH_RESULT = 0x02
    }


    inner class ResultSongsAdapter(private var songs: List<Song>) : RecyclerView.Adapter<ResultSongsAdapter.ViewHolder>() {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val song = songs[position]

            holder.songName.text = song.name
            val displayArtist = "${song.artistName}·${song.albumName}"
            holder.artist.text = displayArtist

            holder.btnMore.setOnClickListener {
                val dialog = SongOpsDialog(context!!, song, activity!!)
                dialog.show()
            }

            holder.item.setOnClickListener {
                if (activity is PlayProxy) {
                    (activity as PlayProxy).playSong(song)
                }
            }

        }

        override fun getItemCount(): Int = songs.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false)
            return ViewHolder(itemView)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val songName: TextView = itemView.findViewById(R.id.result_song_name)
            val artist: TextView = itemView.findViewById(R.id.result_artist_album)
            val btnMore: ImageButton = itemView.findViewById(R.id.result_more)
            val item: LinearLayout = itemView.findViewById(R.id.result_item)
        }
    }

    inner class SearchHistoryAdapter(private var history: List<SearchHistory>) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val searchHistory = history[position]
            holder.historyKeyword.text = searchHistory.keyword
            holder.deleteHistory.setOnClickListener {
                BoxUtil.getBoxStore(context!!).boxFor(SearchHistory::class.java).remove(searchHistory)
                showSearchHistory()
            }
            holder.item.setOnClickListener {
                searchKeyword = searchHistory.keyword
                val mainFragment: MainFragment = fragmentManager!!.findFragmentById(R.id.main_container) as MainFragment
                mainFragment.setSearchKeyword(searchHistory.keyword)
                beginSearch(searchHistory.keyword)
            }
        }

        override fun getItemCount(): Int = history.size

        fun setData(newData: List<SearchHistory>) {
            history = newData
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val historyKeyword: TextView = itemView.findViewById(R.id.search_history_keyword)
            val deleteHistory: ImageButton = itemView.findViewById(R.id.search_delete_history)
            val item: RelativeLayout = itemView.findViewById(R.id.search_history_item)
        }
    }

    inner class SearchRecommendAdapter(private var keywords: List<SearchTip>)
        : RecyclerView.Adapter<SearchRecommendAdapter.ViewHolder>() {
        override fun getItemCount(): Int = keywords.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_search_recommend, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recommendKeyword = keywords[position]
            holder.keyword.text = recommendKeyword.tip
            holder.item.setOnClickListener {
                searchKeyword = recommendKeyword.tip
                val mainFragment: MainFragment = fragmentManager!!.findFragmentById(R.id.main_container) as MainFragment
                mainFragment.setSearchKeyword(recommendKeyword.tip)
                beginSearch(recommendKeyword.tip)
            }
        }

        fun setData(newKeywords: List<SearchTip>) {
            keywords = newKeywords
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val keyword: TextView = itemView.findViewById(R.id.search_recommend_keyword)
            val item: RelativeLayout = itemView.findViewById(R.id.search_recommend_item)
        }
    }
}