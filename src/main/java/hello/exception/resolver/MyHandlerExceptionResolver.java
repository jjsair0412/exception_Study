package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    // 전달받은 값을보고 ModelAndView를 반환해준다.
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if(ex instanceof IllegalArgumentException){// Exception이 IllegalArgumentException이라면 해당 if문 실행
                // IllegalArgumentException이 발생하면 400에러로 변경하고 새로운 ModelAndView를 반환한다.
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,ex.getMessage()); // 400로 바꿔버린다.
                return new ModelAndView(); // 새로운 ModelAndView를 반환해준다.
            }
        }catch (IOException e){
            log.error("resolver ex",e);
        }
        return null; // null이 반환되면 다음 HandlerExceptionResolver을 찾는다. 그런데 다음게 없다면 기존 에러처리처럼 반환된다.
    }
}
