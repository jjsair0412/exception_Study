package hello.exception.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
// @RestControllerAdvice  해당 어노테이션을 달아주면, 다른 모든 컨트롤러에도 api 에러처리부분을 적용시킬 수 있다. 결론은 분리가 가능하다
// @ControllerAdvice와 @RestController를 합쳐준게 @RestControllerAdvice이다.
// 대상을 적용하지 않았기 때문에 모든 컨트롤러에 적용된다.
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHander(IllegalArgumentException e){
        log.error("[exceptionHandler]ex ",e );
        return new ErrorResult("BAD",e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userHandler(UserException e){
        log.info("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.info("[exceptionHandler] ex",e);
        return new ErrorResult("EX","내부 오류");
    }
}
