package com.wenhaiz.himusic.module.liked

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.LikedAlbum
import com.wenhaiz.himusic.data.bean.LikedAlbum_
import com.wenhaiz.himusic.module.detail.DetailContract
import com.wenhaiz.himusic.module.detail.DetailFragment
import com.wenhaiz.himusic.utils.BoxUtil
import com.wenhaiz.himusic.utils.GlideApp
import com.wenhaiz.himusic.utils.addFragmentToMainView

class LikedAlbumFragment : Fragment() {

    @BindView(R.id.liked_album)
    lateinit var mAlbumList: RecyclerView
    private lateinit var mUnbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_liked_album, container, false)
        mUnbinder = ButterKnife.bind(this, contentView)
        initView()
        return contentView
    }

    private fun initView() {
        val likedAlbumBox = BoxUtil.getBoxStore(context!!).boxFor(LikedAlbum::class.java)
        val list = likedAlbumBox.query().orderDesc(LikedAlbum_.likedTime).build().find()
        mAlbumList.adapter = LikedAlbumListAdapter(list)
        mAlbumList.layoutManager = LinearLayoutManager(context)
    }

    private fun showAlbumDetail(album: Album) {
        val data = Bundle()
        data.putLong(DetailContract.ARGS_ID, album.id)
        data.putSerializable(DetailContract.ARGS_LOAD_TYPE, DetailContract.LoadType.ALBUM)
        val detailFragment = DetailFragment()
        detailFragment.arguments = data
        addFragmentToMainView(parentFragment!!.fragmentManager!!, detailFragment)
    }


    inner class LikedAlbumListAdapter(private val albumList: List<LikedAlbum>) : RecyclerView.Adapter<LikedAlbumListAdapter.ViewHolder>() {
        override fun getItemCount(): Int = albumList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val album = albumList[position].album
            holder.albumName.text = album.title
            holder.artistName.text = album.artist
            GlideApp.with(context)
                    .load(album.coverUrl)
                    .placeholder(R.drawable.ic_main_all_music)
                    .into(holder.albumCover)
            holder.itemView.setOnClickListener {
                showAlbumDetail(album)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_liked_album, parent, false)
            return ViewHolder(itemView)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val albumName: TextView = itemView.findViewById(R.id.liked_album_name)
            val artistName: TextView = itemView.findViewById(R.id.liked_album_artist)
            val albumCover: ImageView = itemView.findViewById(R.id.liked_album_cover)
        }
    }
}