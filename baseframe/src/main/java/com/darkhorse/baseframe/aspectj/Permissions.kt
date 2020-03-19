package com.darkhorse.baseframe.aspectj

import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.aspectj.annotation.SingleClick
import com.darkhorse.baseframe.extension.getString
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.utils.PermissionsUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

@Aspect
class Permissions {

    @Pointcut("execution(@com.darkhorse.baseframe.aspectj.annotation.Permissions * *(..))")       //权限申请方法切入点
    fun permissionsAnnotated() {
    }

    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("permissionsAnnotated()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(proceedingJoinPoint: ProceedingJoinPoint) {
        val methodSignature = proceedingJoinPoint.signature as MethodSignature
        val method = methodSignature.method

        val permissions = method.getAnnotation(com.darkhorse.baseframe.aspectj.annotation.Permissions::class.java)
        val permissionsCode = permissions?.value ?: 0

        if (permissionsCode == 0) {
            toast(getString(R.string.please_set_permissions))
        } else {
            if (PermissionsUtils.hasPermission(permissionsCode)) {
                proceedingJoinPoint.proceed()
            } else {
                PermissionsUtils.requestPermission(permissionsCode)
            }
        }
    }
}