package com.ikang.data.entity

/**
 * @author ikang-renwei
 * @Date 2019/12/23 18:09
 * @describe
 */
data class NewsListBean(
        val curPage: Int,
        val datas: List<NewsListItemBean>
)

data class NewsListItemBean(
        val imgUrl: String,
        val newsNumber: String,
        val nickname: String,
        val content: String,
        val time: String
)