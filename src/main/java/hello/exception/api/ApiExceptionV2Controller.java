package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    // IllegalArgumentException이 발생하면, ExceptionResolver에게 처리를 물어본다.
    // 이때 ExceptionResolver는 컨트롤러에 @ExceptionHandler가 존재하는지 여부를 판단하고,
    // 존재한다면 해당 메서드를 실행한다.
    // 그후 반환타입으로 ErrorResult를 주는데, 해당 code값과 에러메시지는 그대로 json으로 변환되어 클라이언트에게 보내지게 된다.
    // 여기서 ErrorResult는 멤버변수들을 getter, setter해주는 프로퍼티 객체이다.
    // @ExceptionHandler의 value로 에러들을 넣어주고, 해당 에러의 json 반환값을 여기서 정해주면 된다.
    // 얘는 상태코드를 변경해주지 않는다면 http 상태코드가 200, 그니까 MVC 정상흐름이 되기때문에 상태코드를 지정해주어야 한다.
    // @ResponseStatus를 통해서 상태코드를 넣어주면 된다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHander(IllegalArgumentException e){
        log.error("[exceptionHandler]ex ",e );
        return new ErrorResult("BAD",e.getMessage());
    }

    // 아래의 예처럼 @ExceptionHandler의 value로 넣어주지 않고, 해당 메서드의 인자값으로 두어도 정상처리된다.
    // 또한 ResponseEntity<>를 반환시킬 수도 있다.
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userHandler(UserException e){
        log.info("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    // 인자값으로 UserException을 준다면, userException의 자식타입들도 모두 잡아준다.
    // 인자값으로 IllegalArgumentException를 준다면, IllegalArgumentException의 자식타입들도 모두 잡아준다.
    // 그런데 이둘에서도 잡지못햇던 예외들은 Exception이 잡아준다.
    // 그냥 Exception을 넣어준다면, 모든 예외들을 처리하지 못했다면 Exception을 넣어준 예외들이 다 이쪽으로 온다.
    // 예외 공통처리부분이라고 생각하면 될듯
    public ErrorResult exHandler(Exception e){
        log.info("[exceptionHandler] ex",e);
        return new ErrorResult("EX","내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id,"hello"+id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
