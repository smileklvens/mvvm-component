package com.ikang.staffapp.http

import com.ikang.libmvi.base.BaseResult
import com.ikang.staffapp.data.entity.OauthResp
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
const val BASE_URL = "https://newuat-oauth2.health.ikang.com"

interface ApiService {

    /*统一登录平台获取 access_token*/
    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getLoginToken(@FieldMap formMap: Map<String, String>): BaseResult<OauthResp>

    /*统一登录平台 session*/
    @GET("/api/login")
    suspend fun getLoginSession(@FieldMap formMap: Map<String, String>): BaseResult<OauthResp>


    /*双向认证*/
    @GET
    suspend fun doubleCheckHttps(@Url  fullUrl:String): ResponseBody
}