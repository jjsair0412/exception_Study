package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// WebServerFactoryCustomizer<ConfigurableWebServerFactory>를 구현한 클래스 생성
//@Component // 스프링 bean으로 등록해준다.
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    // customize 오버라이드
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 간단하게 말하면, Http상태코드가 NOT FOUND로 발생하면, error-page/400으로 가라 라는 뜻이다.
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        // RuntimeException의 자식타입 예외들은 전부다 아래의 errorPageEx가 호출된다.
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class,"/error-page/500");

        // 위에 초기화해둿던 세가지 에러페이지를 펙토리에 등록한다.
        factory.addErrorPages(errorPage404,errorPage500, errorPageEx);
    }
}
