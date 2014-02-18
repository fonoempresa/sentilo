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
package org.sentilo.platform.common.exception;

import org.apache.http.HttpStatus;

/**
 * Excepción propia del proceso de conversion que se produce cuando hay un error al parsear los
 * datos de entrada en formato JSON a un objeto Java, o bien al hacer el proceso inverso de
 * conversion del objeto Java asociado a la respuesta a formato JSON.
 */
public class JsonConverterException extends PlatformException {

  private static final long serialVersionUID = 1L;

  public JsonConverterException(final String message, final Throwable cause, final boolean input) {
    super(message, cause);

    // En funcion de si es un error al parsear los datos de entrada o las datos de respuesta,
    // el codigo de error a retornar es diferente.
    if (input) {
      setHttpStatus(HttpStatus.SC_BAD_REQUEST);
    } else {
      setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
