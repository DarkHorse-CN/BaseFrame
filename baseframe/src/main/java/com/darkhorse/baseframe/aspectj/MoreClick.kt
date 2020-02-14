package com.darkhorse.baseframe.aspectj

import com.darkhorse.baseframe.utils.TaskUtils
import io.reactivex.disposables.Disposable
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

@Aspect
class MoreClick {
    private var mMoreClickDisposable: Disposable? = null
    private var mMoreClickCount = 0

    @Pointcut("execution(@com.darkhorse.baseframe.aspectj.annotation.MoreClick * *(..))")//方法切入点
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
        val moreClick = method.getAnnotation(com.darkhorse.baseframe.aspectj.annotation.MoreClick::class.java)

        val moreCount = moreClick?.value ?: 3
        val disposeTime = 500

        if (mMoreClickDisposable != null && !mMoreClickDisposable!!.isDisposed) {
            mMoreClickDisposable!!.dispose()
        }
        mMoreClickCount++
        if (mMoreClickCount == moreCount) {
            proceedingJoinPoint.proceed()
            mMoreClickCount = 0
        } else {
            mMoreClickDisposable = TaskUtils.delayAsyncTask(disposeTime.toLong(), object : TaskUtils.IDisposableTask {
                override fun run(times: Long, disposable: Disposable) {
                    mMoreClickCount = 0
                    disposable.dispose()
                }
            })
        }
    }
}