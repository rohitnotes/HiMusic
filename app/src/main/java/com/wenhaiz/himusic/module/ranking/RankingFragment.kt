package com.wenhaiz.himusic.module.ranking

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.ext.hide
import com.wenhaiz.himusic.ext.show
import com.wenhaiz.himusic.http.data.RankList
import com.wenhaiz.himusic.module.detail.DetailContract
import com.wenhaiz.himusic.module.detail.DetailFragment
import com.wenhaiz.himusic.utils.GlideApp
import com.wenhaiz.himusic.utils.addFragmentToMainView
import com.wenhaiz.himusic.utils.removeFragment

class RankingFragment : Fragment(), RankingContract.View {
    @BindView(R.id.action_bar_title)
    lateinit var mTitle: TextView
    @BindView(R.id.ranking_official)
    lateinit var mOfficialRanking: RecyclerView
    @BindView(R.id.ranking_global)
    lateinit var mGlobalRanking: RecyclerView
    @BindView(R.id.loading)
    lateinit var mLoading: LinearLayout
    @BindView(R.id.loading_failed)
    lateinit var mLoadFailed: LinearLayout
    @BindView(R.id.content)
    lateinit var mContent: LinearLayout

    lateinit var mPresenter: RankingContract.Presenter
    private lateinit var mUnbinder: Unbinder

    private val globalRankAdapter = GlobalRankingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RankingPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView: View = inflater.inflate(R.layout.fragment_ranking, container, false)
        mUnbinder = ButterKnife.bind(this, contentView)
        initView()
        return contentView
    }

    override fun initView() {
        mPresenter.loadRankingList()
        mTitle.text = context!!.getString(R.string.main_ranking_list)
        initGlobalRankingView()
    }

    private fun initGlobalRankingView() {
        val titles = context!!.resources.getStringArray(R.array.global_ranking).toList()
        val covers = intArrayOf(R.drawable.ranking_billboard, R.drawable.ranking_uk, R.drawable.ranking_oricon)
        val data = ArrayList<Map<String, Any>>()
        (0 until titles.size).mapTo(data) { hashMapOf<String, Any>(Pair("title", titles[it]), Pair("cover", covers[it])) }
        mGlobalRanking.layoutManager = GridLayoutManager(context, 3,
                GridLayoutManager.HORIZONTAL, false)
        mGlobalRanking.adapter = globalRankAdapter


        SimpleAdapter(context, data,
                R.layout.item_ranking_global,
                arrayOf("title", "cover"),
                intArrayOf(R.id.ranking_global_title, R.id.ranking_global_cover))

//        mGlobalRanking.setOnItemClickListener { _, _, i, _ ->
//            val ranking = when (i) {
//                0 -> RankingContract.GlobalRanking.BILLBOARD
//                1 -> RankingContract.GlobalRanking.UK
//                2 -> RankingContract.GlobalRanking.ORICON
//                else -> {
//                    RankingContract.GlobalRanking.BILLBOARD
//                }
//            }
//            val detailFragment = DetailFragment()
//            val args = Bundle()
//            args.putSerializable(DetailContract.ARGS_GLOBAL_RANKING, ranking)
//            args.putSerializable(DetailContract.ARGS_LOAD_TYPE, DetailContract.LoadType.GLOBAL_RANKING)
//            detailFragment.arguments = args
//            addFragmentToMainView(fragmentManager!!, detailFragment)
//        }
    }

    @OnClick(R.id.action_bar_back, R.id.loading_failed)
    fun onClick(view: View) {
        when (view.id) {
            R.id.action_bar_back -> {
                removeFragment(fragmentManager!!, this)
            }
            R.id.loading_failed -> {
                mPresenter.loadRankingList()
            }
        }
    }


    override fun setPresenter(presenter: RankingContract.Presenter) {
        mPresenter = presenter
    }

    override fun getViewContext(): Context {
        return context!!
    }


    override fun onRankingListLoad(rankList: RankList) {
        mLoading.hide()
        mContent.show()
        showOfficialRanking(rankList.xiamiRanks)
        showGlobalRanking(rankList.globalRank)
    }

    private fun showGlobalRanking(globalRanks: List<RankList.Rank>) {
        globalRankAdapter.updateData(globalRanks)
    }

    private fun showOfficialRanking(ranks: List<RankList.Rank>) {
        mOfficialRanking.adapter = OfficialRankingAdapter(ranks)
        mOfficialRanking.layoutManager = LinearLayoutManager(context)
    }

    fun showRankingDetail(rank: RankList.Rank) {
        val detailFragment = DetailFragment()
        val args = Bundle()
        args.putSerializable(DetailContract.ARGS_ID, rank)
        args.putSerializable(DetailContract.ARGS_LOAD_TYPE, DetailContract.LoadType.RANKING)
        detailFragment.arguments = args
        addFragmentToMainView(fragmentManager!!, detailFragment)
    }

    override fun onLoading() {
        mLoading.show()
        mContent.hide()
        mLoadFailed.hide()
    }

    override fun onFailure(msg: String) {
            mLoading.hide()
            mContent.hide()
            mLoadFailed.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    inner class OfficialRankingAdapter(private val rankingCollects: List<RankList.Rank>) : RecyclerView.Adapter<OfficialRankingAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val rank = rankingCollects[position]
            GlideApp.with(context)
                    .load(rank.logoMiddle)
                    .into(holder.rankingCover)

            val firstSong = rank.songs[0]
            val firstPreview = "1.${firstSong.name}-${firstSong.artistName}"
            holder.songPreview0.text = firstPreview

            val secondSong = rank.songs[1]
            val secondPreview = "2.${secondSong.name}-${secondSong.artistName}"
            holder.songPreview1.text = secondPreview

            val thirdSong = rank.songs[2]
            val thirdPreview = "3.${thirdSong.name}-${thirdSong.artistName}"
            holder.songPreview2.text = thirdPreview

            holder.itemView.setOnClickListener {
                showRankingDetail(rank)
            }
        }

        override fun getItemCount(): Int = rankingCollects.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = layoutInflater.inflate(R.layout.item_ranking_list, parent, false)
            return ViewHolder(itemView)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val rankingCover: ImageView = itemView.findViewById(R.id.ranking_cover)
            val songPreview0: TextView = itemView.findViewById(R.id.ranking_preview0)
            val songPreview1: TextView = itemView.findViewById(R.id.ranking_preview1)
            val songPreview2: TextView = itemView.findViewById(R.id.ranking_preview2)
        }
    }

    inner class GlobalRankingAdapter : RecyclerView.Adapter<GlobalRankingAdapter.ViewHolder>() {
        private val rankingCollects: ArrayList<RankList.Rank> = ArrayList()
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_ranking_global, null)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int = rankingCollects.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val rank = rankingCollects[position]
            holder.tvTitle.text = rank.name
            GlideApp.with(context).load(rank.logoMiddle).into(holder.ivCover)
            holder.itemView.setOnClickListener {
                showRankingDetail(rank)
            }
        }

        fun updateData(ranks: List<RankList.Rank>) {
            rankingCollects.clear()
            rankingCollects.addAll(ranks)
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivCover: ImageView = itemView.findViewById(R.id.ranking_global_cover)
            val tvTitle: TextView = itemView.findViewById(R.id.ranking_global_title)

        }
    }
}