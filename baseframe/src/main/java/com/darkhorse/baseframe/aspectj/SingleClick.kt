package com.darkhorse.baseframe.aspectj

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

@Aspect
class SingleClick {
    /**
     * 最近一次点击时间
     */
    private var mLastClickTimeMillis: Long = 0

    @Pointcut("execution(@com.darkhorse.baseframe.aspectj.annotation.SingleClick * *(..))")       //方法切入点
    fun methodAnnotated() {
    }

    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("methodAnnotated()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(proceedingJoinPoint: ProceedingJoinPoint) {
        val methodSignature = proceedingJoinPoint.signature as MethodSignature
        val method = methodSignature.method

        val singleClick = method.getAnnotation(com.darkhorse.baseframe.aspectj.annotation.SingleClick::class.java)

        val intervalTime = singleClick?.value ?: 3000
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - mLastClickTimeMillis < intervalTime) {
            return
        }
        mLastClickTimeMillis = currentTimeMillis
        proceedingJoinPoint.proceed()
    }
}