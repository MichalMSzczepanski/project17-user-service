package work.szczepanskimichal.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.szczepanskimichal.exception.MissingHeaderException;

@Aspect
@Component
public class HttpHeaderAspect {

    @Before("execution(* work.szczepanskimichal.controller.AuthenticatedUserController.*(..))")
    public void checkForUserIdHeader(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String headerUserId = request.getHeader("X-User-Id");
            if (headerUserId == null || headerUserId.isEmpty()) {
                throw new MissingHeaderException("X-User-Id");
            }
        }
    }
}
