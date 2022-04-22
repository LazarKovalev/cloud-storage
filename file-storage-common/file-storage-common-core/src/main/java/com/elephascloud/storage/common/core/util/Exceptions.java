package com.elephascloud.storage.common.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class Exceptions {

  /**
   * convert CheckedException to UncheckedException.
   *
   * @param e Throwable
   * @return {RuntimeException}
   */
  public static RuntimeException unchecked(Throwable e) {
    if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
        || e instanceof NoSuchMethodException) {
      return new IllegalArgumentException(e);
    } else if (e instanceof InvocationTargetException) {
      return new RuntimeException(((InvocationTargetException) e).getTargetException());
    } else if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    } else {
      return new RuntimeException(e);
    }
  }

  /**
   * unwrap exception
   *
   * @param wrapped wrapped exception
   * @return unwrapped exception
   */
  public static Throwable unwrap(Throwable wrapped) {
    Throwable unwrapped = wrapped;
    while (true) {
      if (unwrapped instanceof InvocationTargetException) {
        unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
      } else if (unwrapped instanceof UndeclaredThrowableException) {
        unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
      } else {
        return unwrapped;
      }
    }
  }
}
