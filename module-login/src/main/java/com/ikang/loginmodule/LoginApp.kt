package com.ikang.loginmodule

import android.content.Context
import com.ikang.providerservice.delegate.IApp


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #} 代理application生命周期
 */
class LoginApp: IApp {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(base: Context) {
        //传统方式通信
//        LoginServiceControl.accountService = LoginServiceImpl()
    }

    override fun onTerminate(base: Context) {
    }

}