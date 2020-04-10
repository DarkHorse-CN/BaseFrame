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
        if (point.signature.declaringTypeName == "com.darkhorse.baseframe.base.BaseActivity") {
            val className = point.target.javaClass.name;
            val methodName = point.signature.name;
            val preTime = TimeUtils.timeInMillis();
            point.proceed()
            val costTime = TimeUtils.timeInMillis() - preTime
            logI("$className.$methodName() cost $costTime ms")
        }
    }

    @After("execution(* com.darkhorse.baseframe.base.BaseActivity.on**(..))")
    @Throws(Throwable::class)
    fun listenActivityLife(point: JoinPoint) {
        if (point.signature.declaringTypeName == "com.darkhorse.baseframe.base.BaseActivity") {
            val className = point.target.javaClass.name;
            val methodName = point.signature.name;
            logI("$className.$methodName()")
        }
    }

    @Before("execution(* com.darkhorse.baseframe.base.BaseActivity.onCreate(..))")
    @Throws(Throwable::class)
    fun listenActivityOnCreate(point: JoinPoint) {
        if (point.signature.declaringTypeName == "com.darkhorse.baseframe.base.BaseActivity") {
            AppManager.addActivity(point.`this` as BaseActivity)
        }
    }

    @Before("execution(* com.darkhorse.baseframe.base.BaseActivity.onDestroy(..))")
    @Throws(Throwable::class)
    fun listenActivityOnDestroy(point: JoinPoint) {
        if (point.signature.declaringTypeName == "com.darkhorse.baseframe.base.BaseActivity") {
            AppManager.removeActivity(point.`this` as BaseActivity)
        }
    }
}