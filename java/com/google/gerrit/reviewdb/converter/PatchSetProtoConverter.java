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
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.client.RevId;
import com.google.protobuf.Parser;
import java.sql.Timestamp;
import java.util.List;

public enum PatchSetProtoConverter implements ProtoConverter<Reviewdb.PatchSet, PatchSet> {
  INSTANCE;

  private final ProtoConverter<Reviewdb.PatchSet_Id, PatchSet.Id> patchSetIdConverter =
      PatchSetIdProtoConverter.INSTANCE;
  private final ProtoConverter<Reviewdb.RevId, RevId> revIdConverter = RevIdProtoConverter.INSTANCE;
  private final ProtoConverter<Reviewdb.Account_Id, Account.Id> accountIdConverter =
      AccountIdProtoConverter.INSTANCE;

  @Override
  public Reviewdb.PatchSet toProto(PatchSet patchSet) {
    Reviewdb.PatchSet.Builder builder =
        Reviewdb.PatchSet.newBuilder().setId(patchSetIdConverter.toProto(patchSet.getId()));
    RevId revision = patchSet.getRevision();
    if (revision != null) {
      builder.setRevision(revIdConverter.toProto(revision));
    }
    Account.Id uploader = patchSet.getUploader();
    if (uploader != null) {
      builder.setUploaderAccountId(accountIdConverter.toProto(uploader));
    }
    Timestamp createdOn = patchSet.getCreatedOn();
    if (createdOn != null) {
      builder.setCreatedOn(createdOn.getTime());
    }
    List<String> groups = patchSet.getGroups();
    if (!groups.isEmpty()) {
      builder.setGroups(PatchSet.joinGroups(groups));
    }
    String pushCertificate = patchSet.getPushCertificate();
    if (pushCertificate != null) {
      builder.setPushCertificate(pushCertificate);
    }
    String description = patchSet.getDescription();
    if (description != null) {
      builder.setDescription(description);
    }
    return builder.build();
  }

  @Override
  public PatchSet fromProto(Reviewdb.PatchSet proto) {
    PatchSet patchSet = new PatchSet(patchSetIdConverter.fromProto(proto.getId()));
    if (proto.hasRevision()) {
      patchSet.setRevision(revIdConverter.fromProto(proto.getRevision()));
    }
    if (proto.hasUploaderAccountId()) {
      patchSet.setUploader(accountIdConverter.fromProto(proto.getUploaderAccountId()));
    }
    if (proto.hasCreatedOn()) {
      patchSet.setCreatedOn(new Timestamp(proto.getCreatedOn()));
    }
    if (proto.hasGroups()) {
      patchSet.setGroups(PatchSet.splitGroups(proto.getGroups()));
    }
    if (proto.hasPushCertificate()) {
      patchSet.setPushCertificate(proto.getPushCertificate());
    }
    if (proto.hasDescription()) {
      patchSet.setDescription(proto.getDescription());
    }
    return patchSet;
  }

  @Override
  public Parser<Reviewdb.PatchSet> getParser() {
    return Reviewdb.PatchSet.parser();
  }
}
