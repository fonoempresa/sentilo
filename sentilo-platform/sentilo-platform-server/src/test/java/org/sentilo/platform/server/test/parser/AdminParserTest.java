/*
 * Sentilo
 *  
 * Original version 1.4 Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de Barcelona.
 * Modified by Opentrends adding support for multitenant deployments and SaaS. Modifications on version 1.5 Copyright (C) 2015 Opentrends Solucions i Sistemes, S.L.
 * 
 *   
 * This program is licensed and may be used, modified and redistributed under the
 * terms  of the European Public License (EUPL), either version 1.1 or (at your 
 * option) any later version as soon as they are approved by the European 
 * Commission.
 *   
 * Alternatively, you may redistribute and/or modify this program under the terms
 * of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either  version 3 of the License, or (at your option) any later 
 * version. 
 *   
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. 
 *   
 * See the licenses for the specific language governing permissions, limitations 
 * and more details.
 *   
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along 
 * with this program; if not, you may find them at: 
 *   
 *   https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *   http://www.gnu.org/licenses/ 
 *   and 
 *   https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.platform.server.test.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sentilo.common.domain.PlatformActivity;
import org.sentilo.common.domain.PlatformMetricsMessage;
import org.sentilo.common.domain.PlatformPerformance;
import org.sentilo.platform.common.domain.AdminInputMessage;
import org.sentilo.platform.common.domain.AdminInputMessage.AdminType;
import org.sentilo.platform.common.domain.Statistics;
import org.sentilo.platform.common.domain.Statistics.Events;
import org.sentilo.platform.common.domain.Statistics.Performance;
import org.sentilo.platform.common.exception.JsonConverterException;
import org.sentilo.platform.server.parser.AdminParser;
import org.sentilo.platform.server.request.SentiloRequest;
import org.sentilo.platform.server.request.SentiloResource;
import org.sentilo.platform.server.response.SentiloResponse;

public class AdminParserTest {

  private AdminParser parser;
  @Mock
  private SentiloRequest sentiloRequest;
  @Mock
  private SentiloResource resource;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    parser = new AdminParser();

    when(sentiloRequest.getResource()).thenReturn(resource);
  }

  @Test
  public void parseStatsRequest() throws Exception {
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.stats.name());

    final AdminInputMessage message = parser.parseGetRequest(sentiloRequest);

    assertEquals(AdminType.stats, message.getType());
    assertNull(message.getEntity());
  }

  @Test
  public void parseStatsWriteResponse() throws Exception {
    final Events events = new Events(new Long(10), new Long(3), new Long(4), new Long(3));
    final Performance performance = new Performance(new Float(54.84), new Float(14.65), new Float(784.84));
    final Statistics stats = new Statistics(events, performance);

    final SentiloResponse response = SentiloResponse.build(new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0, 200, "")));
    parser.writeStatsResponse(sentiloRequest, response, stats);

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ((ByteArrayEntity) response.getHttpResponse().getEntity()).writeTo(baos);
    final String expected =
        "{\"events\":{\"total\":10,\"observations\":4,\"alarms\":3,\"orders\":3},\"performance\":{\"instantAvg\":54.84,\"dailyAvg\":14.65,\"maxAvg\":784.84}}";
    assertEquals(expected, baos.toString());
  }

  @Test(expected = JsonConverterException.class)
  public void parseWrongDeleteRequest() throws Exception {
    final String json = "{\"resources\":[\"RE001\",\"RE002\",\"RE003\"]}";

    when(sentiloRequest.getBody()).thenReturn(json);
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.delete.name());

    parser.parsePutRequest(sentiloRequest);
  }

  @Test
  public void parseDeleteSensorsRequest() throws Exception {
    final String json = "{\"sensors\":[{\"provider\":\"PRV001\",\"sensor\":\"RE001\"},{\"provider\":\"PRV001\",\"sensor\":\"RE002\"}]}";

    when(sentiloRequest.getBody()).thenReturn(json);
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.delete.name());

    final AdminInputMessage message = parser.parsePutRequest(sentiloRequest);

    assertNotNull(message.getSensors());
    assertNull(message.getProviders());
    assertEquals(2, message.getSensors().size());
  }

  @Test
  public void parseDeleteProvidersRequest() throws Exception {
    final String json = "{\"providers\":[{\"provider\":\"PRV001\"},{\"provider\":\"PRV002\"}]}";

    when(sentiloRequest.getBody()).thenReturn(json);
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.delete.name());

    final AdminInputMessage message = parser.parsePutRequest(sentiloRequest);

    assertNotNull(message.getProviders());
    assertNull(message.getSensors());
    assertEquals(2, message.getProviders().size());
  }

  @Test
  public void parseActivityRequest() throws Exception {
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.activity.name());

    final AdminInputMessage message = parser.parseGetRequest(sentiloRequest);

    assertEquals(AdminType.activity, message.getType());
    assertNull(message.getEntity());
  }

  @Test
  public void parsePerformanceRequest() throws Exception {
    when(sentiloRequest.getResourcePart(0)).thenReturn(AdminType.performance.name());

    final AdminInputMessage message = parser.parseGetRequest(sentiloRequest);

    assertEquals(AdminType.performance, message.getType());
    assertNull(message.getEntity());
  }

  @Test
  public void parseActivityWriteResponse() throws Exception {
    final PlatformActivity masterActivity = new PlatformActivity(null, 1000, 950, 3, 47, 1447164322214l, true);
    final PlatformActivity tenantActivity = new PlatformActivity("mockTenant", 1000, 950, 3, 47, 1447164322214l, false);
    final Collection<PlatformActivity> globalActivity = Arrays.asList(new PlatformActivity[] {masterActivity, tenantActivity});
    final PlatformMetricsMessage metrics = new PlatformMetricsMessage();
    metrics.setActivity(globalActivity);

    final SentiloResponse response = SentiloResponse.build(new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0, 200, "")));
    parser.writeMetricsResponse(sentiloRequest, response, metrics);

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ((ByteArrayEntity) response.getHttpResponse().getEntity()).writeTo(baos);

    final String expected =
        "{\"activity\":[{\"totalRequests\":1000,\"totalObs\":950,\"totalOrders\":3,\"totalAlarms\":47,\"timestamp\":1447164322214,\"isMaster\":true},{\"tenant\":\"mockTenant\",\"totalRequests\":1000,\"totalObs\":950,\"totalOrders\":3,\"totalAlarms\":47,\"timestamp\":1447164322214,\"isMaster\":false}]}";

    assertEquals(expected, baos.toString());
  }

  @Test
  public void parsePerformanceWriteResponse() throws Exception {
    final PlatformActivity activity = new PlatformActivity(null, 1000, 950, 3, 47, 1447164322214l, true);
    final PlatformPerformance performance = new PlatformPerformance(activity, 3.45f, 34.32f, 132.87f, 1447164322214l);
    final Collection<PlatformPerformance> globalPerformance = Arrays.asList(new PlatformPerformance[] {performance});
    final PlatformMetricsMessage metrics = new PlatformMetricsMessage();
    metrics.setPerformance(globalPerformance);

    final SentiloResponse response = SentiloResponse.build(new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0, 200, "")));
    parser.writeMetricsResponse(sentiloRequest, response, metrics);

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ((ByteArrayEntity) response.getHttpResponse().getEntity()).writeTo(baos);

    final String expected =
        "{\"performance\":[{\"instantAvg\":3.45,\"maxDailyAvg\":34.32,\"maxAvg\":132.87,\"timestamp\":1447164322214,\"totalRequests\":1000,\"totalObs\":950,\"totalOrders\":3,\"totalAlarms\":47,\"isMaster\":true}]}";

    assertEquals(expected, baos.toString());
  }

}
