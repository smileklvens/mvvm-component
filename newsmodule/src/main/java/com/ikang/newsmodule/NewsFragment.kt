package com.ikang.newsmodule

import androidx.databinding.ViewDataBinding
import com.example.newsmodule.R
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment

/**
 * @author ikang-renwei
 * @Date 2019/12/23 11:11
 * @describe
 */
class NewsFragment : BaseRefreshMoreFragment<BaseViewModel,ViewDataBinding>(){

//    private val mAdapter by lazy { NewsListAdapter() }
//    private var page: Int = 0

    override fun layoutId(): Int = R.layout.fragment_news

    override fun lazyLoadData() {
//        bindSwipeRecycler(
//                newsSmartRefreshLayout,
//                newsRecyclerView,
//                mAdapter as BaseQuickAdapter<*,BaseViewHolder>
//        )
//        newsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
//
//        val inflater = LayoutInflater.from(context).inflate(R.layout.news_item_news_list_adapter,null,false)
//        mAdapter.addHeaderView(inflater)

//        observe(viewModel.)
    }

    override fun onLoadMore() {
    }

    override fun onRefresh() {
    }

}