package com.ikang.newsmodule

import androidx.lifecycle.MutableLiveData
import com.ikang.data.entity.NewsListBean
import com.ikang.data.entity.NewsListItemBean
import com.ikang.libmvi.base.BaseViewModel

/**
 * @author ikang-renwei
 * @Date 2019/12/24 18:35
 * @describe
 */
class MsgViewModel : BaseViewModel() {
    val newsListBean = MutableLiveData<NewsListBean>()

    fun getNewsListBean(page: Int) {
        launch({
            val mutableListOf = mutableListOf<NewsListItemBean>()
            mutableListOf.add(NewsListItemBean("https://ikapp-image.health.ikang.com/2019/11/22/201911221355300410.png", "99", "系统消息", "[图片]","15:30"))
            newsListBean.value = NewsListBean(
                    page,
                    mutableListOf
            )
        })
    }
}