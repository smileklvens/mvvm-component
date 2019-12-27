package com.ikang.newsmodule

import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.newsmodule.R
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment
import com.ikang.libmvi.util.ext.observe
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.news_item_news_list_adapter.view.*

/**
 * @author ikang-renwei
 * @Date 2019/12/23 11:11
 * @describe
 */
class NewsFragment : BaseRefreshMoreFragment<NewsViewModel,ViewDataBinding>(){

    private val mAdapter by lazy { NewsListAdapter() }
    private var page: Int = 0

    override fun layoutId(): Int = R.layout.fragment_news

    override fun lazyLoadData() {
        bindSwipeRecycler(
                newsSmartRefreshLayout,
                newsRecyclerView,
                mAdapter as BaseQuickAdapter<*, BaseViewHolder>
        )
//        newsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))

        val inflater = LayoutInflater.from(context).inflate(R.layout.news_item_news_list_adapter,null,false)
        mAdapter.addHeaderView(inflater)

        observe(viewModel.newsListBean){
            stopRefresh()
            mAdapter.setNewData(it.datas)

            inflater.newsSystemImgIV.setBackgroundResource(R.drawable.icon_system_message)
            inflater.newsSystemTitleTV.text = "系统消息"
            inflater.newsSystemContentTv.text = "[图片]"
            inflater.newsSystemTimeTv.text = "昨天"
        }

        newsSmartRefreshLayout.setEnableLoadMore(false)
        newsSmartRefreshLayout.setEnableRefresh(false)

        viewModel.getNewsListBean(page)
    }

    override fun onLoadMore() {
    }

    override fun onRefresh() {
    }

}