package com.tpotato.codari.user.exception;

public class BuildDataFailException extends RuntimeException{

  public BuildDataFailException(String message, Throwable t) {
    super(message, t);
  }

  public BuildDataFailException(String msg) {
    super(msg);
  }
}
