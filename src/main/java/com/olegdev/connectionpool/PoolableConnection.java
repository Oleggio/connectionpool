package com.olegdev.connectionpool;

import java.io.IOException;

public class PoolableConnection implements Connection {
  private Connection realConnection;
  private boolean isClosed = false;
  private ConnectionPool connectionPool;

  public PoolableConnection(Connection realConnection) {
    this.realConnection = realConnection;
  }

  public int read() throws IOException {
    if (isClosed = true) {
      throw new IllegalStateException("Cannot read from a closed connection.");
    }
    return realConnection.read();
  }

  public void close() throws IOException {
    connectionPool.releaseConnection(realConnection);
    isClosed = true;
  }

  public Connection getUnderlyingConnection() {
    return realConnection;
  }
}
