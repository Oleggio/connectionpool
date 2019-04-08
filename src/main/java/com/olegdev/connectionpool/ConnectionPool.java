package com.olegdev.connectionpool;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ConnectionPool {
  // private static long expTime = 6000; // 6 seconds
  private static Queue<Connection> available = new LinkedList<Connection>();
  private static Set<Connection> inUse = new HashSet<Connection>();
  private static ConnectionPool connectionPool;

  private ConnectionPool() {}

  public static ConnectionPool getInstance() {
    if (connectionPool != null) return connectionPool;
    return new ConnectionPool();
  }

  // We need synchronized here because of the if() statement. Concurrent threads may fail to get
  // connection if another thread has done it before.
  public static synchronized Connection acquireConnection(byte input[]) {
    if (!available.isEmpty()) {
      return new PoolableConnection(available.poll());
    }
    // either no PooledObject is available or each has expired, so return a new one
    return new PoolableConnection(createConnection(input));
  }

  // We need synchronized here for future needs and checks
  private static synchronized Connection createConnection(byte input[]) {
    Connection c = new ByteArrayInputStreamConnection(input);
    inUse.add(c);
    return c;
  }

  protected static void releaseConnection(Connection c) {
    available.add(c);
    inUse.remove(c);
  }

  protected int getUsedConnectionsNumber() {
    return inUse.size();
  }

  protected int getAvailableConnectionsNumber() {
    return available.size();
  }

  protected void clear() {
    available.clear();
    inUse.clear();
  }
}
