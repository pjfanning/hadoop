/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.nodemanager.webapp;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *  Container shell client socket interface.
 */
public class ContainerShellClientSocketTest implements Session.Listener.AutoDemanding {
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerShellClientSocketTest.class);
  private Session session;
  private CountDownLatch latch = new CountDownLatch(1);

  @Override
  public void onWebSocketText(String message) {
    LOG.info("Message received from server:" + message);
  }

  @Override
  public void onWebSocketOpen(Session session) {
    LOG.info("Connected to server");
    this.session = session;
    latch.countDown();
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    session.close();
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    cause.printStackTrace(System.err);
  }

  public void sendMessage(String str) {
    try {
      session.getRemote().sendString(str);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      LOG.error("Failed to sent message to server", e);
    }
  }

  public CountDownLatch getLatch() {
    return latch;
  }

  public void setLatch(CountDownLatch latch) {
    this.latch = latch;
  }
}

