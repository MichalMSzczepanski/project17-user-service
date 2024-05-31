package work.szczepanskimichal.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.szczepanskimichal.exception.MissingHeaderException;

@Aspect
@Component
@Profile("prod")
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

//        UUID userIdFromHeader = UUID.fromString(headerUserId);
//        String emailFromService = userService.getEmailByUserId(userIdFromHeader);
//
//        HandlerMethod handlerMethod = (HandlerMethod) joinPoint.getSignature();
//        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//
//        // Ensure the first path variable is "userId"
//        MethodParameter userIdParameter = methodParameters[0];
//        if (!userIdParameter.getParameterName().equals("userId")) {
//            throw new IllegalArgumentException("First path variable must be named 'userId'");
//        }
//
//        // Validate userId
//        String[] userIdValues = parameterMap.get("userId");
//        if (userIdValues != null && userIdValues.length > 0) {
//            UUID userIdFromPath = UUID.fromString(userIdValues[0]);
//            if (!userIdFromPath.equals(userIdFromHeader)) {
//                throw new UnauthorizedAccessException("User is not authorized to access this resource");
//            }
//        }
//
//        // Validate email
//        String[] emailValues = parameterMap.get("email");
//        if (emailValues != null && emailValues.length > 0) {
//            String email = emailValues[0];
//            if (!email.equals(emailFromService)) {
//                throw new UnauthorizedAccessException("User is not authorized to access this resource");
//            }
//        }

    }
}
