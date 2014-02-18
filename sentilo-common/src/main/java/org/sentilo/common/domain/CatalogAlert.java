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
package org.sentilo.common.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class CatalogAlert implements CatalogElement {

  private String id;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String name;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String description;

  private String entity;

  private String type;

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String trigger;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String expression;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String component;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String sensor;

  public CatalogAlert() {
    super();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getSensor() {
    return sensor;
  }

  public void setSensor(String sensor) {
    this.sensor = sensor;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  public String getEntity() {
    return entity;
  }

}
