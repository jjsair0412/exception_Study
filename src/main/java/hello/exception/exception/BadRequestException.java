package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 기존에는 RuntimeException은 500상태코드가 발생해야 한다.
// 그런데 @ResponseStatus 어노테이션에 에러코드와 reason을 달아주면 상태코드를 변경시킬 수 있다.
@ResponseStatus(code = HttpStatus.BAD_REQUEST , reason = "error.bad")
public class BadRequestException extends RuntimeException{
}
