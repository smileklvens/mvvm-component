package com.ikang.loginmodule.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.ikang.providerservice.router.service.ILoginService
import com.ikang.providerservice.router.service.ProviderPath


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #} 组件间通信
 */
@Route(path = ProviderPath.Provider.LOGIN)
class LoginServiceRouter : ILoginService {
    override val isLogin: Boolean = true

    override val accountId: String = "张三"

    override fun init(context: Context?) {
    }
}