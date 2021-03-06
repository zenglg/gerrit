// Copyright (C) 2018 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.reviewdb.converter;

import com.google.gerrit.proto.reviewdb.Reviewdb;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.protobuf.Parser;

public enum PatchSetIdProtoConverter implements ProtoConverter<Reviewdb.PatchSet_Id, PatchSet.Id> {
  INSTANCE;

  private final ProtoConverter<Reviewdb.Change_Id, Change.Id> changeIdConverter =
      ChangeIdProtoConverter.INSTANCE;

  @Override
  public Reviewdb.PatchSet_Id toProto(PatchSet.Id patchSetId) {
    return Reviewdb.PatchSet_Id.newBuilder()
        .setChangeId(changeIdConverter.toProto(patchSetId.getParentKey()))
        .setPatchSetId(patchSetId.get())
        .build();
  }

  @Override
  public PatchSet.Id fromProto(Reviewdb.PatchSet_Id proto) {
    return new PatchSet.Id(changeIdConverter.fromProto(proto.getChangeId()), proto.getPatchSetId());
  }

  @Override
  public Parser<Reviewdb.PatchSet_Id> getParser() {
    return Reviewdb.PatchSet_Id.parser();
  }
}
