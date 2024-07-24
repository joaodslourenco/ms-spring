package com.e_commerce.users.aspects;

import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.exceptions.IllegalAccessException;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAccessAspect {
    private final UserRepository userRepository;

    @Around("@annotation(com.e_commerce.users.annotations.UserSelfDataAccess) && args(id,..)")
    public Object checkUserAccess(ProceedingJoinPoint joinPoint, UUID id) throws Throwable {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserModel) principal;
            UserModel currentUser = (UserModel) userRepository.findByEmail(userDetails.getUsername());

            if (currentUser.getId().equals(id) || currentUser.getRole() == ERole.ADMIN) {
                return joinPoint.proceed();
            }
        }
        throw new IllegalAccessException("You don't have access to this resource");
    }

}
