package com.example.system.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class logging {

    @Pointcut("within(com.example.system.controller..*)")
    public void appointment() {
    }

    @Around("appointment()")
    public Object loggingResults(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;

        if (joinPoint.getSignature().getName().equals("bookAppointment")){
            if (result.toString().matches(".*200 OK.*")){
                String doctorUsername = result.toString().split("username=")[1].split(",")[0] ;
                String time = result.toString().split("startTime=")[1].split(",")[0] ;
                System.out.println("\n" + username + " has booked appointment at " + time + " with Dr. " + doctorUsername + "\n") ;
            }
            else{
                System.out.println("\n" + username + " failed to book appointment. " + result.toString().split(",")[1] + "\n") ;
            }
        }
        else if (joinPoint.getSignature().getName().equals("cancelAppointment")){
            if (result == null){
                System.out.println("\n" + username + " has canceled appointment.\n") ;
            }
            else{
                System.out.println("\n" + username + " failed to cancel appointment. " + result.toString().split(",")[1] + "\n") ;
            }
        }
        else if (joinPoint.getSignature().getName().equals("addLabResult")){
            if (result.toString().matches(".*200 OK.*")){
                System.out.println("\n" + username + " added new lab result.\n") ;
            }
            else{
                System.out.println("\n" + username + " failed to add lab result. " + result.toString().split(",")[1] + "\n") ;
            }        }
        else if (joinPoint.getSignature().getName().equals("newPrescription")){
            if (result.toString().matches(".*200 OK.*")){
                System.out.println("\n" + username + " added new prescription.\n") ;
            }
            else{
                System.out.println("\n" + username + " failed to add prescription. " + result.toString().split(",")[1] + "\n") ;
            }
        }

        return result ;
    }
}

