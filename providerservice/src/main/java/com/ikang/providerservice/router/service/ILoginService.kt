package com.ikang.providerservice.router.service

import com.alibaba.android.arouter.facade.template.IProvider


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
interface ILoginService :IProvider {
    val isLogin: Boolean
    val accountId: String
}