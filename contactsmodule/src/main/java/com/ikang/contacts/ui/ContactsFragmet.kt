package com.ikang.contacts.ui

import androidx.databinding.ViewDataBinding
import com.example.contacts.R
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.fragment.BaseFragment
import com.ikang.libmvi.base.ui.fragment.BaseRefreshMoreFragment

/**
 * @author ikang-renwei
 * @Date 2019/12/23 10:48
 * @describe 通讯录
 */
class ContactsFragmet : BaseFragment<BaseViewModel,ViewDataBinding>(){

    override fun layoutId(): Int = R.layout.fragment_contacts

    override fun lazyLoadData() {

    }

//    override fun onLoadMore() {
//    }
//
//    override fun onRefresh() {
//    }

}