package com.wenhaiz.himusic.module.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.LikedAlbum
import com.wenhaiz.himusic.data.bean.LikedAlbum_
import com.wenhaiz.himusic.data.bean.LikedCollect
import com.wenhaiz.himusic.data.bean.LikedCollect_
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.ext.showToast
import com.wenhaiz.himusic.module.main.MainActivity
import com.wenhaiz.himusic.module.main.local.EditCollectActivity
import com.wenhaiz.himusic.module.main.local.LocalFragment
import com.wenhaiz.himusic.module.play.service.PlayProxy
import com.wenhaiz.himusic.module.ranking.RankingContract
import com.wenhaiz.himusic.utils.BoxUtil
import com.wenhaiz.himusic.utils.GlideApp
import com.wenhaiz.himusic.utils.ScreenUtil
import com.wenhaiz.himusic.utils.getDate
import com.wenhaiz.himusic.utils.removeFragment
import com.wenhaiz.himusic.widget.CollectOpsDialog
import com.wenhaiz.himusic.widget.SongOpsDialog

class DetailFragment : Fragment(), DetailContract.View {
    @BindView(R.id.action_bar_title)
    lateinit var mActionBarTitle: TextView
    @BindView(R.id.detail_cover)
    lateinit var mCover: ImageView
    @BindView(R.id.detail_title)
    lateinit var mTitle: TextView
    @BindView(R.id.detail_artist)
    lateinit var mArtist: TextView
    @BindView(R.id.detail_date)
    lateinit var mDate: TextView
    @BindView(R.id.detail_song_list)
    lateinit var mSongList: RecyclerView
    @BindView(R.id.loading)
    lateinit var mLoading: LinearLayout
    @BindView(R.id.loading_failed)
    lateinit var mLoadFailed: LinearLayout
    @BindView(R.id.detail_liked_icon)
    lateinit var mLikedIcon: ImageView
    @BindView(R.id.detail_liked)
    lateinit var mLiked: LinearLayout
    @BindView(R.id.liked_space)
    lateinit var mLikedSpace: Space
    @BindView(R.id.detail_add_to_play)
    lateinit var mAddToPlay: LinearLayout
    @BindView(R.id.detail_more)
    lateinit var mMore: ImageButton

    private lateinit var mSongListAdapter: SongListAdapter
    lateinit var mPresenter: DetailContract.Presenter
    private lateinit var mUnBinder: Unbinder
    private lateinit var mLoadType: DetailContract.LoadType

    private lateinit var mAlbum: Album
    private lateinit var mCollect: Collect
    private var isCollectFromUser: Boolean = false
    var localFragment: LocalFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_detail, container, false)
        mUnBinder = ButterKnife.bind(this, contentView)
        mLoadType = arguments!!.getSerializable(DetailContract.ARGS_LOAD_TYPE) as DetailContract.LoadType
        //是否是用户自建歌单
        isCollectFromUser = arguments!!.getBoolean(DetailContract.ARGS_IS_USER_COLLECT, false)
        initView()
        return contentView
    }

    override fun initView() {
        //根据加载类型设置标题
        mActionBarTitle.text = when (mLoadType) {
            DetailContract.LoadType.COLLECT -> getString(R.string.collect_detail)
            DetailContract.LoadType.ALBUM -> getString(R.string.album_detail)
            else -> ""
        }

        //如果是自建歌单，隐藏收藏按钮并调整布局
        if (mLoadType == DetailContract.LoadType.COLLECT && isCollectFromUser) {
            mLiked.hide()
            mLikedSpace.hide()
            mMore.show()

            val lp = mAddToPlay.layoutParams as LinearLayout.LayoutParams
            lp.marginEnd = ScreenUtil.dp2px(context!!, 20f)
            mAddToPlay.layoutParams = lp

        }

        mSongListAdapter = SongListAdapter(context!!, ArrayList())
        mSongList.layoutManager = LinearLayoutManager(context)
        mSongList.adapter = mSongListAdapter
        loadDetail()
    }

    /**
     * 加载专辑或歌单详情
     */
    private fun loadDetail() {
        when (mLoadType) {
            DetailContract.LoadType.GLOBAL_RANKING -> {//全球排行榜
                val ranking: RankingContract.GlobalRanking = arguments!!.getSerializable(DetailContract.ARGS_GLOBAL_RANKING) as RankingContract.GlobalRanking
                mPresenter.loadGlobalRanking(ranking)
            }
            DetailContract.LoadType.OFFICIAL_RANKING -> {//官方排行榜
                val collect: Collect = arguments!!.getParcelable(DetailContract.ARGS_COLLECT)
                setRankingDetail(collect)
            }
            DetailContract.LoadType.ALBUM -> {//专辑
                val id = arguments!!.getLong(DetailContract.ARGS_ID)
                mPresenter.loadAlbumDetail(id)
            }
            DetailContract.LoadType.COLLECT -> {//歌单
                val id = arguments!!.getLong(DetailContract.ARGS_ID)
                //根据歌单来源加载歌曲列表
                mPresenter.loadCollectDetail(id, isCollectFromUser)
            }
            DetailContract.LoadType.SONG -> {//歌曲
                val id = arguments!!.getLong(DetailContract.ARGS_ID)
                mPresenter.loadSongDetail(id)
            }
        }
    }

    private fun playSong(song: Song) {
        (activity as PlayProxy).playSong(song)
    }

    @OnClick(R.id.action_bar_back, R.id.detail_play_all, R.id.detail_download_all,
            R.id.detail_add_to_play, R.id.detail_liked, R.id.loading_failed, R.id.detail_more)
    fun onClick(view: View) {
        when (view.id) {
            R.id.action_bar_back -> {//返回
                removeFragment(fragmentManager!!, this)
            }
            R.id.detail_play_all -> {//播放全部
                (activity as MainActivity).playService.replaceList(mSongListAdapter.songList)
            }
            R.id.detail_add_to_play -> {//将全部歌曲添加到当前播放列表
                (activity as MainActivity).playService.addToPlayList(mSongListAdapter.songList)
            }
            R.id.detail_liked -> {//收藏歌单或专辑
                switchLikedState()
            }
            R.id.detail_download_all -> {//下载全部
                context!!.showToast("download all")
            }
            R.id.loading_failed -> {//加载失败后点击，重新加载
                loadDetail()
            }
            R.id.detail_more -> {//右上角更多操作，只在显示自建歌单时可见
                showCollectOperationDialog()
            }
        }
    }


    /**
     * 切换收藏状态
     */
    private fun switchLikedState() {
        if (mLoadType == DetailContract.LoadType.ALBUM) {
            val albumBox = BoxUtil.getBoxStore(context!!).boxFor(LikedAlbum::class.java)
            var liked = false
            var likedAlbum = isCurAlbumLiked()
            if (likedAlbum != null) {
                albumBox.remove(likedAlbum)
                context!!.showToast("已取消收藏")
            } else {
                likedAlbum = LikedAlbum(mAlbum)
                albumBox.put(likedAlbum)
                liked = true
                context!!.showToast("收藏成功")
            }
            setLikedIcon(liked)
        } else if (mLoadType == DetailContract.LoadType.COLLECT) {
            val collectBox = BoxUtil.getBoxStore(context!!).boxFor(LikedCollect::class.java)
            var liked = false
            var likedCollect = isCurCollectLiked()
            if (likedCollect != null) {
                collectBox.remove(likedCollect)
                context!!.showToast(R.string.unliked)
            } else {
                likedCollect = LikedCollect(mCollect)
                collectBox.put(likedCollect)
                liked = true
                context!!.showToast(R.string.liked)
            }
            setLikedIcon(liked)
        }
    }

    private fun isCurAlbumLiked(): LikedAlbum? {
        var likedAlbum: LikedAlbum? = null
        val albumBox = BoxUtil.getBoxStore(context!!).boxFor(LikedAlbum::class.java)
        val list = albumBox.query().equal(LikedAlbum_.albumId, mAlbum.id)
                .and()
                .equal(LikedAlbum_.providerName, mAlbum.supplier.name)
                .build()
                .find()
        if (list.size > 0) {
            likedAlbum = list[0]
        }
        return likedAlbum
    }

    private fun isCurCollectLiked(): LikedCollect? {
        var likedCollect: LikedCollect? = null
        val collectBox = BoxUtil.getBoxStore(context!!).boxFor(LikedCollect::class.java)
        val list = collectBox.query().equal(LikedCollect_.collectId, mCollect.collectId)
                .and()
                .equal(LikedCollect_.providerName, mCollect.source.name)
                .build()
                .find()
        if (list.size > 0) {
            likedCollect = list[0]
        }
        return likedCollect
    }

    private fun setLikedIcon(isLiked: Boolean) {
        if (isLiked) {
            mLikedIcon.setImageResource(R.drawable.ic_liked)
        } else {
            mLikedIcon.setImageResource(R.drawable.ic_like_border)
        }
    }

    private fun showCollectOperationDialog() {
        val collectOpsDialog = CollectOpsDialog(context!!)
        //监听对歌单进行的操作
        collectOpsDialog.onCollectOperationListener = object : CollectOpsDialog.OnCollectOperationListener {
            override fun onUpdate() {
                editCurCollect()
            }

            override fun onDelete() {
                deleteCurCollect()
                //更新主界面的歌单显示
                localFragment?.showCollects()
                removeFragment(fragmentManager!!, this@DetailFragment)
            }

        }
        collectOpsDialog.show()
    }

    private fun editCurCollect() {
        val intent = Intent(context, EditCollectActivity::class.java)
        intent.action = EditCollectActivity.ACTION_UPDATE
        intent.putExtra("collectId", mCollect.id)
        startActivityForResult(intent, REQUEST_UPDATE)
    }

    private fun deleteCurCollect() {
        val collectId = mCollect.id
        val collectBox = BoxUtil.getBoxStore(context!!).boxFor(Collect::class.java)
        collectBox.remove(collectId)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun onLoading() {
        mLoading.show()
        mLoadFailed.hide()
        mSongList.hide()
    }

    override fun onCollectDetailLoad(collect: Collect) {
        activity!!.runOnUiThread({
            mCollect = collect
            mTitle.text = collect.title
            mArtist.hide()
            GlideApp.with(context).load(collect.coverUrl)
                    .placeholder(R.drawable.ic_main_all_music)
                    .into(mCover)
            val displayDate = "更新时间：${getDate(collect.updateDate)}"
            mDate.text = displayDate
            mSongListAdapter.setData(collect.songs)

            mLoading.hide()
            mSongList.show()

            if (!isCollectFromUser && isCurCollectLiked() != null) {
                setLikedIcon(true)
            }
        })
    }

    override fun onGlobalRankingLoad(collect: Collect) {
        activity!!.runOnUiThread {
            setRankingDetail(collect)
        }
    }

    private fun setRankingDetail(collect: Collect) {
        activity!!.runOnUiThread({
            mActionBarTitle.text = collect.title
            mTitle.text = collect.title
            mArtist.text = collect.desc
            GlideApp.with(context)
                    .load(collect.coverDrawable)
                    .into(mCover)
            mDate.hide()
            mSongListAdapter.setData(collect.songs)

            mLoading.hide()
            mSongList.show()
        })
    }

    override fun onAlbumDetailLoad(album: Album) {
        activity!!.runOnUiThread {
            mAlbum = album
            mTitle.text = album.title
            mArtist.show()
            mArtist.text = album.artist
            val displayDate = "发行时间：${getDate(album.publishDate)}"
            GlideApp.with(context).load(album.coverUrl)
                    .placeholder(R.drawable.ic_main_all_music)
                    .into(mCover)
            mDate.text = displayDate
            mSongListAdapter.setData(album.songs)

            mLoading.hide()
            mSongList.show()
            if (isCurAlbumLiked() != null) {
                setLikedIcon(true)
            }
        }

    }

    override fun onFailure(msg: String) {
        activity!!.runOnUiThread {
            mLoading.hide()
            mLoadFailed.show()
            context!!.showToast(msg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //修改歌单信息完成后回调
        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_UPDATED) {
            //重新加载歌单信息
            loadDetail()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnBinder.unbind()
    }

    companion object {
        const val TAG = "DetailFragment"
        const val REQUEST_UPDATE = 0x00
        const val RESULT_UPDATED = 0x01
    }

    inner class SongListAdapter(val context: Context, var songList: List<Song>) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_detail_song_list, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val song = songList[position]
            val index = "${position + 1}"
            holder.index.text = index
            holder.title.text = song.name
            val displayArtistName =
                    if (mLoadType == DetailContract.LoadType.ALBUM) {
                        song.artistName
                    } else {
                        if (song.albumName != "") {
                            val artistAlbum = "${song.artistName} · ${song.albumName}"
                            if (artistAlbum.length < 30) {
                                artistAlbum
                            } else {
                                song.artistName
                            }
                        } else {
                            song.artistName
                        }
                    }
            holder.artistAlbum.text = displayArtistName
            holder.item.setOnClickListener({
                playSong(song)
            })
            holder.opration.setOnClickListener {
                val dialog = SongOpsDialog(context, song, activity!!)
                if (mLoadType == DetailContract.LoadType.COLLECT && isCollectFromUser) {
                    dialog.showDelete = true
                    dialog.deleteListener = View.OnClickListener {
                        mCollect.songs.remove(song)
                        BoxUtil.getBoxStore(context).boxFor(Collect::class.java).put(mCollect)
                        setData(mCollect.songs)
                    }
                }
                dialog.show()
            }
        }

        override fun getItemCount(): Int = songList.size

        fun setData(songList: List<Song>) {
            this.songList = songList
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var item: LinearLayout = itemView.findViewById(R.id.detail_song_item)
            var index: TextView = itemView.findViewById(R.id.detail_index)
            val title: TextView = itemView.findViewById(R.id.detail_song_title)
            var artistAlbum: TextView = itemView.findViewById(R.id.detail_artist_album)
            var opration: ImageButton = itemView.findViewById(R.id.detail_btn_operation)
        }
    }
}