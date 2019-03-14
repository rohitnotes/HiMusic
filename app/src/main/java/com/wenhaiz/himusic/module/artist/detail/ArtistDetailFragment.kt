package com.wenhaiz.himusic.module.artist.detail

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.isShowing
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.ext.showToast
import com.wenhaiz.himusic.module.main.MainActivity
import com.wenhaiz.himusic.module.play.service.PlayProxy
import com.wenhaiz.himusic.utils.GlideApp
import com.wenhaiz.himusic.utils.removeFragment

class ArtistDetailFragment : Fragment(), ArtistDetailContract.View {
    @BindView(R.id.detail_artist_name)
    lateinit var mArtistName: TextView
    @BindView(R.id.detail_artist_photo)
    lateinit var mArtistPhoto: ImageView

    //歌手热门歌曲
    private lateinit var mHotSongList: RecyclerView
    private lateinit var mHotSongRefresh: SmartRefreshLayout
    lateinit var mShuffleAll: LinearLayout
    private lateinit var mHotSongAdapter: HotSongsAdapter
    private var curHotSongPage = 1
    private lateinit var mUnbinder: Unbinder
    lateinit var mPresenter: ArtistDetailContract.Presenter
    lateinit var artist: Artist


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ArtistDetailPresenter(this)
        artist = arguments?.getParcelable("artist")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_artist_detail, container, false)
        mHotSongList = contentView.findViewById(R.id.detail_song_list)
        mHotSongRefresh = contentView.findViewById(R.id.hotSongRefresh)
        mShuffleAll = contentView.findViewById(R.id.shuffle_all)
        mUnbinder = ButterKnife.bind(this, contentView)
        initView()
        return contentView
    }

    override fun initView() {
        mPresenter.loadArtistDetail(artist)
        mArtistName.text = artist.artistName
        mArtistPhoto.setOnClickListener { }
        mPresenter.loadArtistHotSongs(artist, curHotSongPage)
        initHotSongView()
    }

    private fun initHotSongView() {
        mHotSongAdapter = HotSongsAdapter(context!!, ArrayList())
        mHotSongList.layoutManager = LinearLayoutManager(context)
        mHotSongList.adapter = mHotSongAdapter
        mHotSongRefresh.setOnLoadmoreListener {
            mPresenter.loadArtistHotSongs(artist, curHotSongPage)
        }
        mShuffleAll.setOnClickListener {
            (activity as MainActivity).playService.shuffleAll(mHotSongAdapter.hotSongs)
        }
    }


    @OnClick(R.id.action_bar_back)
    fun onClick(view: View) {
        when (view.id) {
            R.id.action_bar_back -> {
                removeFragment(fragmentManager!!, this)
            }
        }
    }


    override fun setPresenter(presenter: ArtistDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun onFailure(msg: String) {
        if (mHotSongRefresh.isLoading) {
            mHotSongRefresh.finishLoadmore(200, false)
        }
        context!!.showToast(msg)
    }

    override fun onLoading() {
    }

    override fun onArtistDetail(artist: Artist) {
        GlideApp.with(this).load(artist.imgUrl)
                    .into(mArtistPhoto)
    }

    override fun onHotSongsLoad(hotSongs: List<Song>) {
        if (mHotSongRefresh.isLoading) {
            mHotSongRefresh.finishLoadmore(200, true)
        }
        if (!mShuffleAll.isShowing()) {
            mShuffleAll.show()
        }
        curHotSongPage++
        mHotSongAdapter.addData(hotSongs)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    inner class HotSongsAdapter(val context: Context, var hotSongs: List<Song>) : RecyclerView.Adapter<HotSongsAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val song = hotSongs[position]
            val index = "${position + 1}"
            holder.index.text = index
            holder.title.text = song.name
            // 虾米没有专辑信息
            if (song.albumName != "") {
                holder.album.hide()
                holder.album.text = song.albumName
            } else {
                holder.album.hide()
            }
            holder.item.setOnClickListener {
                (activity as PlayProxy).playSong(song)
            }
        }

        override fun getItemCount(): Int = hotSongs.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_artist_detail_song_list, parent, false)
            return ViewHolder(itemView)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var item: LinearLayout = itemView.findViewById(R.id.detail_song_item)
            var index: TextView = itemView.findViewById(R.id.detail_index)
            val title: TextView = itemView.findViewById(R.id.detail_song_title)
            var album: TextView = itemView.findViewById(R.id.detail_album)
        }

        fun addData(data: List<Song>) {
            (hotSongs as ArrayList).addAll(data)
            notifyDataSetChanged()
        }
    }
}