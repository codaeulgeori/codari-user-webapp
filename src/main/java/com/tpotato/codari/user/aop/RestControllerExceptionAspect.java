package com.tpotato.codari.user.aop;

import com.tpotato.codari.user.domain.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionAspect {

  @ExceptionHandler(value = RuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseData badRequestHandler(RuntimeException e) {
    log.error("badRequestHandler : ", e);
    return ResponseData.builder().statusCause(e.getCause().toString()).statusMsg(e.getMessage()).build();
  }
}
