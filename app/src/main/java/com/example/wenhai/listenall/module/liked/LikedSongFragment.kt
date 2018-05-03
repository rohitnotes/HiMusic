package com.example.wenhai.listenall.module.liked

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.example.wenhai.listenall.R
import com.example.wenhai.listenall.data.bean.LikedSong
import com.example.wenhai.listenall.data.bean.LikedSongDao
import com.example.wenhai.listenall.data.bean.Song
import com.example.wenhai.listenall.ext.showToast
import com.example.wenhai.listenall.module.main.MainActivity
import com.example.wenhai.listenall.utils.DAOUtil
import com.example.wenhai.listenall.widget.SongOpsDialog

class LikedSongFragment : Fragment() {
    @BindView(R.id.liked_songs)
    lateinit var mSongs: RecyclerView

    private lateinit var mUnbinder: Unbinder
    private lateinit var mLikedSongAdapter: LikedSongsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_liked_song, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        initView()
        return view
    }

    fun initView() {
        val likedSongDao = DAOUtil.getSession(context!!).likedSongDao
        val likedSongList = likedSongDao.queryBuilder()
                .orderDesc(LikedSongDao.Properties.LikedTime)
                .list()
        val list: ArrayList<LikedSong> = ArrayList()
        likedSongList.mapTo(list) { it }
        mLikedSongAdapter = LikedSongsAdapter(list)
        mSongs.adapter = mLikedSongAdapter
        mSongs.layoutManager = LinearLayoutManager(context)
    }

    private fun deleteLikedSong(song: LikedSong) {
        val likedSongDao = DAOUtil.getSession(context!!).likedSongDao
        likedSongDao.delete(song)
        context!!.showToast("已取消喜欢")
    }


    @OnClick(R.id.liked_shuffle_all)
    fun onClick(view: View) {
        when (view.id) {
            R.id.liked_shuffle_all -> {
                if (mLikedSongAdapter.likedSongs.isEmpty()) {
                    context!!.showToast(R.string.no_songs_to_play)
                } else {
                    //shuffle all liked songs
                    val songList: ArrayList<Song> = ArrayList()
                    mLikedSongAdapter.likedSongs.mapTo(songList) { it.song }
                    (activity as MainActivity).playService.shuffleAll(songList)
                }
            }
        }
    }


    inner class LikedSongsAdapter(var likedSongs: ArrayList<LikedSong>) : RecyclerView.Adapter<LikedSongsAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_liked_song, parent, false)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int = likedSongs.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val song = likedSongs[position]
            holder.songName.text = song.songName
            val songInfoStr = "${song.artistName} · ${song.albumName}"
            holder.songInfo.text = songInfoStr
            holder.operation.setOnClickListener {
                val dialog = SongOpsDialog(context!!, song.song, activity!!)
                dialog.showDelete = true
                dialog.deleteListener = View.OnClickListener {
                    likedSongs.remove(song)
                    notifyDataSetChanged()
                    deleteLikedSong(song)
                    dialog.dismiss()
                }
                dialog.show()
            }
            holder.itemView.setOnClickListener {
                (activity as MainActivity).playService.playNewSong(song.song)
            }
        }

        inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
            var songName: TextView = item.findViewById(R.id.liked_song_name)
            var songInfo: TextView = item.findViewById(R.id.liked_song_info)
            var operation: ImageButton = item.findViewById(R.id.liked_song_ops)
        }
    }


}