package com.darkhorse.baseframe.aspectj

import android.app.Activity
import com.darkhorse.baseframe.base.BaseActivity
import com.darkhorse.baseframe.extension.logI
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.TimeUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class ActivityAspectj {
    @Around("execution(* com.darkhorse.baseframe.base.BaseActivity.setContentView(..))")
    @Throws(Throwable::class)
    fun getContentViewTime(point: ProceedingJoinPoint) {
        val methodName = "${point.target.javaClass.name}.${point.signature.name}()"
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

    @Before("execution(* com.darkhorse.baseframe.base.BaseActivity.onCreate(..))")
    @Throws(Throwable::class)
    fun listenActivityOnCreate(point: JoinPoint) {
        AppManager.addActivity(point.`this` as Activity)
    }

    @After("call(* com.darkhorse.baseframe.base.BaseActivity.onDestroy(..))")
    @Throws(Throwable::class)
    fun listenActivityOnDestroy(point: JoinPoint) {
        AppManager.removeActivity(point.`this` as Activity)
    }
}