package com.wenhaiz.himusic.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.ext.showToast
import com.wenhaiz.himusic.module.artist.detail.ArtistDetailFragment
import com.wenhaiz.himusic.module.detail.DetailContract
import com.wenhaiz.himusic.module.detail.DetailFragment
import com.wenhaiz.himusic.module.main.MainActivity
import com.wenhaiz.himusic.module.play.service.PlayProxy
import com.wenhaiz.himusic.utils.addFragmentToMainView

class SongOpsDialog(context: Context, val song: Song, private val activity: FragmentActivity) : BaseBottomDialog(context) {
    private var playProxy: PlayProxy? = null
    private var isMainActivity = false

    init {
        playProxy = if (activity is PlayProxy) {
            activity
        } else {
            null
        }
        isMainActivity = activity is MainActivity
    }

    @BindView(R.id.song_name)
    lateinit var songNameTv: TextView
    @BindView(R.id.save_cover)
    lateinit var saveCover: LinearLayout
    @BindView(R.id.artist_detail)
    lateinit var artistDetail: LinearLayout
    @BindView(R.id.artist_name)
    lateinit var artistName: TextView
    @BindView(R.id.album_detail)
    lateinit var albumDetail: LinearLayout
    @BindView(R.id.album_name)
    lateinit var albumName: TextView
    @BindView(R.id.delete)
    lateinit var deleteSong: LinearLayout

    var deleteListener: View.OnClickListener? = null

    private var showAlbum = true
    private var showArtist = true
    var showDelete = false
    var canSaveCover = false


    override fun getLayoutResId(): Int = R.layout.dialog_song_ops

    override fun initView() {
        songNameTv.text = song.name
        artistName.text = song.artistName

        if (showAlbum) {
            albumName.text = song.albumName
        } else {
            albumDetail.hide()
        }

        if (showDelete && deleteListener != null) {
            deleteSong.show()
        } else {
            deleteSong.hide()
        }

        if (!showArtist) {
            artistDetail.hide()
        }

        if (!canSaveCover) {
            saveCover.hide()
        }
    }

    @OnClick(R.id.add_play_next, R.id.delete, R.id.artist_detail, R.id.album_detail,
            R.id.add_to_collect)
    fun onClick(view: View) {
        when (view.id) {
            R.id.add_play_next -> {
                if (playProxy!!.setNextSong(song)) {
                    context.showToast(R.string.play_has_set_to_next)
                } else {
                    context.showToast(context.getString(R.string.song_already_in_playlist))
                }
                dismiss()
            }
            R.id.delete -> {
                deleteListener?.onClick(view)
                dismiss()
            }
            R.id.artist_detail -> {
                showArtistDetail()
                dismiss()
            }
            R.id.album_detail -> {
                showAlbumDetail()
                dismiss()
            }
            R.id.add_to_collect -> {
                //显示歌单界面
                SelectCollectDialog(context)
                        .setChosenSong(song)
                        .show()
                dismiss()
            }
        }
    }

    private fun showAlbumDetail() {
        val data = Bundle()
        data.putLong(DetailContract.ARGS_ID, song.albumId)
        data.putSerializable(DetailContract.ARGS_LOAD_TYPE, DetailContract.LoadType.ALBUM)
        if (isMainActivity) {
            val detailFragment = DetailFragment()
            detailFragment.arguments = data
            addFragmentToMainView(activity.supportFragmentManager, detailFragment)
        } else {
            val intent = Intent()
            intent.putExtras(data)
            activity.setResult(MainActivity.RESULT_SHOW_ALBUM, intent)
            activity.finish()
        }
    }

    private fun showArtistDetail() {
        val data = Bundle()
        data.putParcelable("artist", song.artist)
        if (isMainActivity) {
            val artistDetailFragment = ArtistDetailFragment()
            artistDetailFragment.arguments = data
            addFragmentToMainView(activity.supportFragmentManager, artistDetailFragment)
        } else {
            val intent = Intent()
            intent.putExtras(data)
            activity.setResult(MainActivity.RESULT_SHOW_ARTIST, intent)
            activity.finish()
        }
    }

}