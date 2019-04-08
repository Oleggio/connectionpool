package com.olegdev.connectionpool;

import java.io.Closeable;
import java.io.IOException;

public interface Connection extends Closeable {
  int read() throws IOException;

  void close() throws IOException;
}
