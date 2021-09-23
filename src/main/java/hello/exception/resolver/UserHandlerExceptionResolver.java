package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper(); // json으로 변환하기위한 ObjectMappger

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            if (ex instanceof UserException ){
                log.info("UserException resolver to 400");
                // http 헤더가 json인경우와 json이 아닌경우를 둘다 처리해주어야 한다.
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // response 상태코드를 400으로 변경
                if("application/json".equals(acceptHeader)){
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex",ex.getClass());
                    errorResult.put("message",ex.getMessage());

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");

                    String result = objectMapper.writeValueAsString(errorResult); // json을 문자로 변경

                    response.getWriter().write(result);
                    return new ModelAndView(); // 새로운 modelAndView를 반환한다.
                }else{
                    // TEXT/HTML같은게 넘어올 경우
                    return new ModelAndView("error/500"); // 템플릿에 error/500이 호출된다.
                }
            }
        }catch (IOException e){
            log.error("resolver ex",e);
        }
        return null;
    }
}
