package com.ikang.libmvi.util.ext


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
infix fun <T> Boolean.then(value: T?) = if (this) value else null

fun <T> Boolean.then(value: T?, default: T) = if (this) value else default

inline fun <T> Boolean.then(function: () -> T, default: T) = if (this) function() else default

inline fun <T> Boolean.then(function: () -> T, default: () -> T) = if (this) function() else default()

infix inline fun <reified T> Boolean.then(function: () -> T) = if (this) function() else null