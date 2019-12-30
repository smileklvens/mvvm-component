package com.kotlin.mall.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment
import com.ikang.libmvi.config.StaticDemoActivity.Companion.ME_COLLECT
import com.ikang.libmvi.config.StaticDemoActivity.Companion.ME_PROCEDURE
import com.ikang.libmvi.config.StaticDemoActivity.Companion.ME_SETTING
import com.ikang.libmvi.util.XToast
import com.ikang.libmvi.util.ext.click
import com.ikang.libmvi.util.ext.observe
import com.ikang.mymodule.R
import com.ikang.providerservice.router.service.ILoginService
import com.ikang.staffapp.ui.fragment.me.MeListAdapter
import com.ikang.staffapp.ui.fragment.me.MeViewModel
import com.ikang.staffapp.ui.fragment.me.personalcenter.PersonalCenterActivity
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.layout_me_header.view.*


/*
   一般写法
 */
class MeFragment : BaseRefreshMoreFragment<MeViewModel, ViewDataBinding>() {

    @Autowired
    @JvmField var loginService: ILoginService? = null

    private val mAdapter by lazy { MeListAdapter() }
    private var page: Int = 0

    override fun layoutId(): Int = R.layout.fragment_me

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
        bindSwipeRecycler(
                meSmartRefreshLayout,
                meRecyclerView,
                mAdapter as BaseQuickAdapter<*, BaseViewHolder>
        )
//        meRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_me_header, null, false)
        mAdapter.addHeaderView(inflate)


        inflate.meHeadercl.click {
            val intent = Intent(context, PersonalCenterActivity::class.java)
            startActivity(intent)
        }

        observe(viewModel.meListBean) {
            stopRefresh()

            if (it.curPage == 1) mAdapter.setNewData(it.datas)
            else mAdapter.addData(it.datas)
            page = it.curPage
        }

        meSmartRefreshLayout.setEnableLoadMore(false)
        meSmartRefreshLayout.setEnableRefresh(false)

        viewModel.getMeListBean(page)


        //        inflate.meHeaderNameTv  = LoginServiceControl.accountService.accountId
        Log.e("router", (loginService?.accountId))
    }

    override fun onItemClick(
            adapter: BaseQuickAdapter<*, BaseViewHolder>,
            view: View,
            position: Int
    ) {
        when ((mAdapter.data.get(position)).type) {
            //收藏
            ME_COLLECT -> {
                context?.let { XToast.toast(it, "收藏") }
            }
            //我得行程
            ME_PROCEDURE -> {
                context?.let { XToast.toast(it, "我得行程") }
            }
            //设置
            ME_SETTING -> {
                context?.let { XToast.toast(it, "设置") }
//                val intent = Intent(context, LoginActivity::class.java)
//                startActivity(intent)
            }
        }
    }


    override fun onRefresh() {
        //刷新
        page = 0
        viewModel.getMeListBean(page)
    }

    override fun onLoadMore() {
        //加载
        viewModel.getMeListBean(page + 1)
    }
}