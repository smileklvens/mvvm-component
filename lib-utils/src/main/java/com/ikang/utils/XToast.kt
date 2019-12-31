package com.ikang.libmvi.util

import android.content.Context
import android.widget.Toast
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * @author ikang-renwei
 * @Date 2019/12/13 17:00
 * @describe Toast工具类
 */
object XToast {
    private val LENGTH_DEFAULT: Int = Toast.LENGTH_SHORT

    fun cancel() {
    }

    /**
     * 通用，短吐司提示。
     * @param context
     * @param text
     */
    fun toast(context: Context, text: String) {
        context.toast(text);
    }
    /**
     * 通用，长吐司提示。
     * @param context
     * @param text
     */
    fun longToast(context: Context, text: String) {
        context.longToast(text);
    }
}