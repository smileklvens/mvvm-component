package com.ikang.schedulemodule

import androidx.databinding.ViewDataBinding
import com.example.schedulemodule.R
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.fragment.BaseFragment
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment

/**
 * @author ikang-renwei
 * @Date 2019/12/23 11:02
 * @describe 日程
 */
class ScheduleFragment : BaseFragment<BaseViewModel,ViewDataBinding>(){
    override fun layoutId(): Int = R.layout.fragment_schedule

//    override fun onLoadMore() {
//    }
//
//    override fun onRefresh() {
//    }

}