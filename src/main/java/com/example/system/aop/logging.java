package com.example.system.aop;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class logging {

    @Pointcut("within(com.example.system.service..*) || within(com.example.system.controller..*)")
    public void applicationPackagePointcut() {
    }

    @Pointcut("within(com.example.system.service..*)")
    public void appointment() {
    }

    @AfterThrowing(pointcut = "springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("Exception in " +
                joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() +
                " with cause = " + (e.getCause() != null ? e.getCause() : "NULL"));
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Enter: " + joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() + " with argument[s] = " +
                Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        System.out.println("Exit: " + joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() + " with result = " + result);

        return result;
    }
}

