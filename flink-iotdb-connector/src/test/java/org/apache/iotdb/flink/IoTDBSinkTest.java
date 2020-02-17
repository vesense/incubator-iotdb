/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.iotdb.flink;

import org.apache.iotdb.session.Session;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IoTDBSinkTest {

    private IoTDBSink ioTDBSink;
    private Session session;

    @Before
    public void setUp() throws Exception {
        IoTDBOptions options = new IoTDBOptions();
        IoTSerializationSchema serializationSchema = new DefaultIoTSerializationSchema();
        ioTDBSink = new IoTDBSink(options, serializationSchema);

        session = mock(Session.class);
        ioTDBSink.setSession(session);
    }

    @Test
    public void testSink() throws Exception {
        Map tuple = new HashMap();
        tuple.put("device", "D01");
        tuple.put("timestamp", "1581861293000");
        tuple.put("measurement", "temperature");
        tuple.put("value", "36.5");

        ioTDBSink.invoke(tuple, null);
        verify(session).insert(any(String.class), any(Long.class), any(List.class), any(List.class));
    }

    @Test
    public void close() throws Exception {
        ioTDBSink.close();
        verify(session).close();
    }

}