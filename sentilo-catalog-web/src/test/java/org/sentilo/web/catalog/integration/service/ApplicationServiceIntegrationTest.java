/*
 * Sentilo
 * 
 * Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de Barcelona.
 * 
 * This program is licensed and may be used, modified and redistributed under the terms of the
 * European Public License (EUPL), either version 1.1 or (at your option) any later version as soon
 * as they are approved by the European Commission.
 * 
 * Alternatively, you may redistribute and/or modify this program under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the licenses for the specific language governing permissions, limitations and more details.
 * 
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along with this program;
 * if not, you may find them at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl http://www.gnu.org/licenses/ and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.web.catalog.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sentilo.web.catalog.domain.Application;
import org.sentilo.web.catalog.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/test-mongodb-service-context.xml")
public class ApplicationServiceIntegrationTest {

  @Autowired
  private ApplicationService appService;

  @Test
  public void create() {
    final Application app = new Application();
    app.setId("app_perikoApp");
    app.setEmail("periko@periko.com");
    app.setName("perikoApp");
    app.setDescription("Descripción de la perikoApp");

    appService.create(app);
    final Application aux = appService.find(app);

    assertTrue(aux != null);
    assertEquals(aux.getName(), app.getName());
  }

  @Test
  public void update() {
    final Application app = new Application();
    app.setId("app_perikoApp");
    app.setName("perikoApp");
    app.setEmail("newperiko@periko.com");

    appService.update(app);
    final Application aux = appService.find(app);

    assertTrue(aux != null);
    assertEquals(aux.getEmail(), app.getEmail());
  }

  @Test
  public void remove() {
    final Application app = new Application();
    app.setId("app_perikoApp");

    appService.delete(app);
    final Application aux = appService.find(app);

    assertNull(aux);
  }
}
