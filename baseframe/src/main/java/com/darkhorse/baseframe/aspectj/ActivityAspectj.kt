package com.darkhorse.baseframe.aspectj

import com.darkhorse.baseframe.extension.logI
import com.darkhorse.baseframe.utils.TimeUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class ActivityAspectj {
    @Around("execution(* com.darkhorse.baseframe.base.BaseActivity.setContentView(..))")
    @Throws(Throwable::class)
    fun getContentViewTime(point: ProceedingJoinPoint) {
        val methodName = point.signature.toShortString()
        val preTime = TimeUtils.timeInMillis();
        point.proceed()
        val costTime = TimeUtils.timeInMillis() - preTime
        logI("$methodName cost $costTime ms")
    }

    @After("execution(* com.darkhorse.baseframe.base.BaseActivity.on**(..))")
    @Throws(Throwable::class)
    fun listenActivityLife(point: JoinPoint) {
        val methodName = "${point.target.javaClass.name}.${point.signature.name}()"
        logI(methodName)
    }
}