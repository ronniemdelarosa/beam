/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.beam.sdk.extensions.sql;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.values.reflect.DefaultSchemaFactory;
import org.apache.beam.sdk.values.reflect.FieldValueGetter;
import org.apache.beam.sdk.values.reflect.SchemaFactory;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/** */
public class SqlSchemaFactoryTest {

  private static final List<FieldValueGetter> GETTERS_FOR_KNOWN_TYPES =
      ImmutableList.<FieldValueGetter>builder()
          .add(getter("byteGetter", Byte.class))
          .add(getter("shortGetter", Short.class))
          .add(getter("integerGetter", Integer.class))
          .add(getter("longGetter", Long.class))
          .add(getter("floatGetter", Float.class))
          .add(getter("doubleGetter", Double.class))
          .add(getter("bigDecimalGetter", BigDecimal.class))
          .add(getter("booleanGetter", Boolean.class))
          .add(getter("stringGetter", String.class))
          .add(getter("timeGetter", DateTime.class))
          .add(getter("dateGetter", DateTime.class))
          .build();

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testContainsCorrectFields() throws Exception {
    SchemaFactory factory = new DefaultSchemaFactory();

    Schema schema = factory.createSchema(GETTERS_FOR_KNOWN_TYPES);

    assertEquals(GETTERS_FOR_KNOWN_TYPES.size(), schema.getFieldCount());
    assertEquals(
        Arrays.asList(
            "byteGetter",
            "shortGetter",
            "integerGetter",
            "longGetter",
            "floatGetter",
            "doubleGetter",
            "bigDecimalGetter",
            "booleanGetter",
            "stringGetter",
            "timeGetter",
            "dateGetter"),
        schema.getFieldNames());
  }

  @Test
  public void testThrowsForUnsupportedTypes() throws Exception {
    thrown.expect(UnsupportedOperationException.class);

    SchemaFactory factory = new DefaultSchemaFactory();

    factory.createSchema(
        Arrays.<FieldValueGetter>asList(getter("arrayListGetter", ArrayList.class)));
  }

  private static FieldValueGetter<Object> getter(final String fieldName, final Class fieldType) {
    return new FieldValueGetter<Object>() {
      @Override
      public Object get(Object object) {
        return null;
      }

      @Override
      public String name() {
        return fieldName;
      }

      @Override
      public Class type() {
        return fieldType;
      }
    };
  }
}
