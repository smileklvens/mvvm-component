package com.ikang.loginmodule

import android.content.Context
import com.ikang.providerservice.router.service.ILoginService



/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class LoginServiceRouter : ILoginService {
    override val isLogin: Boolean = true

    override val accountId: String = "张三"
    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    override fun init(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}