package gunjoon98.scoreboard.web.ErrorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse serverErrorHandler(Exception e) {
        e.printStackTrace();
        return new ErrorResponse("sever error", 500);
    }

    /***
    @RequstParma - MethodArgumentTypeMismatchException(parameter convent fail exception)
    @RequestBody - HttpMessageNotReadableException(json convert fail exception)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ParameterException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorResponse parameterErrorHandler(Exception e) {
        return new ErrorResponse("input error", 400);
    }
}