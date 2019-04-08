package com.olegdev.connectionpool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputStreamConnection implements Connection {
  private final InputStream stream;

  public ByteArrayInputStreamConnection(byte input[]) {
    stream = new ByteArrayInputStream(input);
  }

  public int read() throws IOException {
    return stream.read();
  }

  public void close() throws IOException {
    stream.close();
  }
}
