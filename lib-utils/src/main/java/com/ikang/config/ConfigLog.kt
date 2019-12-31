package com.ikang.libmvi.config

/**
 * @author ikang-renwei
 * @Date 2019/12/13 16:57
 * @describe 标志位常量
 */
object ConfigLog {

    /** test log开关  */
    var LOG_OPEN = true

    fun setLogOpen(isOpen: Boolean) {
        LOG_OPEN = isOpen
    }

    fun isLogOpen(): Boolean {
        return LOG_OPEN
    }
}