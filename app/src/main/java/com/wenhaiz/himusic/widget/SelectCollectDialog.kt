package com.wenhaiz.himusic.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.showToast
import com.wenhaiz.himusic.module.main.local.EditCollectActivity
import com.wenhaiz.himusic.utils.BoxUtil
import com.wenhaiz.himusic.utils.GlideApp

class SelectCollectDialog(context: Context) : BaseBottomDialog(context) {

    @BindView(R.id.collects)
    lateinit var collects: RecyclerView

    var song: Song? = null

    override fun getLayoutResId(): Int {
        return R.layout.dialog_select_collect
    }

    override fun initView() {
        collects.layoutManager = LinearLayoutManager(context)
        val myCollects = BoxUtil.getBoxStore(context).boxFor(Collect::class.java).query().build().find()
        collects.adapter = CollectAdapter(myCollects)
    }

    @OnClick(R.id.create_collect)
    fun createCollect() {
        //新建歌单并把歌曲添加到新歌单中
        val intent = Intent(context, EditCollectActivity::class.java)
        intent.action = EditCollectActivity.ACTION_CREATE
        val data = Bundle()
        data.putSerializable("song", song)
        intent.putExtras(data)
        context.startActivity(intent)
        dismiss()
    }


    fun setChosenSong(song: Song): SelectCollectDialog {
        this.song = song
        return this
    }

    inner class CollectAdapter(var collects: List<Collect>) :
            RecyclerView.Adapter<CollectAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_liked_collect, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val collect = collects[position]
            holder.name.text = collect.title
            holder.songNumber.hide()
//            val displaySongNumber = "${collect.songCount}首"
//            holder.songNumber.text = displaySongNumber
            GlideApp.with(context)
                    .load(collect.coverUrl)
                    .error(R.drawable.ic_main_all_music)
                    .placeholder(R.drawable.ic_main_all_music)
                    .into(holder.cover)
            holder.itemView.setOnClickListener {
                saveNewRelationRecord(collect)
                dismiss()
            }

        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        private fun saveNewRelationRecord(collect: Collect) {
            val collectId = collect.id
            val box = BoxUtil.getBoxStore(context).boxFor(Collect::class.java)
            val result = box.get(collectId)

            if (result.songs.contains(song)) {
                context.showToast("歌曲已存在")
            } else {
                result.songs.add(song)
                if (box.put(result) > 0) {
                    context.showToast("添加成功")
                    //更新歌单修改时间
                    collect.updateDate = System.currentTimeMillis() / 1000
                    box.put(collect)
                    //todo:刷新数据，防止缓存导致的数据不更新
//                    collect.resetSongs()
                } else {
                    context.showToast("添加失败")
                }
            }
        }

        override fun getItemCount(): Int = collects.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val cover: ImageView = itemView.findViewById(R.id.liked_collect_cover)
            val name: TextView = itemView.findViewById(R.id.liked_collect_name)
            val songNumber: TextView = itemView.findViewById(R.id.liked_collect_song_number)
        }
    }

}