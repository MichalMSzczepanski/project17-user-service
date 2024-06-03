package work.szczepanskimichal.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import work.szczepanskimichal.exception.MissingAuthenticationParameterException;
import work.szczepanskimichal.exception.MissingHeaderException;
import work.szczepanskimichal.exception.UnauthorizedAccessException;

import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Profile("prod")
public class HttpHeaderAspect {

    private static final String X_USER_ID = "X-User-ID";
    private static final String USER_ID = "userId";

    @Before("execution(* work.szczepanskimichal.controller.AuthenticatedUserController.*(..))")
    public void checkForUserIdHeader(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new MissingHeaderException(X_USER_ID);
        }
        HttpServletRequest request = attributes.getRequest();
        String headerUserId = request.getHeader(X_USER_ID);
        if (headerUserId == null || headerUserId.isEmpty()) {
            throw new MissingHeaderException(X_USER_ID);
        }

        UUID userIdFromHeader = UUID.fromString(headerUserId);
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables == null || !pathVariables.containsKey(USER_ID)) {
            throw new MissingAuthenticationParameterException(USER_ID);
        }

        UUID userIdFromPath = UUID.fromString(pathVariables.get(USER_ID));
        if (!userIdFromPath.equals(userIdFromHeader)) {
            throw new UnauthorizedAccessException(userIdFromPath);
        }

    }
}
