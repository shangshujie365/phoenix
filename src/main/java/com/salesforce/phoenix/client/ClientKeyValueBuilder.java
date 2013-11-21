/*******************************************************************************
 * Copyright (c) 2013, Salesforce.com, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *     Neither the name of Salesforce.com nor the names of its contributors may 
 *     be used to endorse or promote products derived from this software without 
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.salesforce.phoenix.client;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.KeyValue.Type;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;

import com.salesforce.phoenix.jdbc.PhoenixDatabaseMetaData;

/**
 * A {@link KeyValueBuilder} that builds {@link ClientKeyValue}, eliminating the extra byte copies
 * inherent in the standard {@link KeyValue} implementation.
 * <p>
 * This {@link KeyValueBuilder} is only supported in HBase 0.94.14+ (
 * {@link PhoenixDatabaseMetaData#CLIENT_KEY_VALUE_BUILDER_THRESHOLD}), with the addition of
 * HBASE-9834.
 */
public class ClientKeyValueBuilder extends KeyValueBuilder {

  public static final KeyValueBuilder INSTANCE = new ClientKeyValueBuilder();

  private ClientKeyValueBuilder() {
    // private ctor for singleton
  }

  @Override
  public KeyValue buildPut(ImmutableBytesWritable row, ImmutableBytesWritable family,
      ImmutableBytesWritable qualifier, long ts, ImmutableBytesWritable value) {
    return new ClientKeyValue(row, family, qualifier, ts, Type.Put, value);
  }

  @Override
  public KeyValue buildDeleteFamily(ImmutableBytesWritable row, ImmutableBytesWritable family,
      ImmutableBytesWritable qualifier, long ts, ImmutableBytesWritable value) {
    return new ClientKeyValue(row, family, qualifier, ts, Type.DeleteFamily, value);
  }

  @Override
  public KeyValue buildDeleteColumns(ImmutableBytesWritable row, ImmutableBytesWritable family,
      ImmutableBytesWritable qualifier, long ts, ImmutableBytesWritable value) {
    return new ClientKeyValue(row, family, qualifier, ts, Type.DeleteColumn, value);
  }

  @Override
  public KeyValue buildDeleteColumn(ImmutableBytesWritable row, ImmutableBytesWritable family,
      ImmutableBytesWritable qualifier, long ts, ImmutableBytesWritable value) {
    return new ClientKeyValue(row, family, qualifier, ts, Type.Delete, value);
  }
}