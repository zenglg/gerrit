// Copyright (C) 2008 The Android Open Source Project
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

package com.google.gerrit.common.data;

import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Account.FieldName;
import com.google.gerrit.reviewdb.client.AccountGeneralPreferences.DownloadCommand;
import com.google.gerrit.reviewdb.client.AccountGeneralPreferences.DownloadScheme;
import com.google.gerrit.reviewdb.client.AuthType;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gwtexpui.safehtml.client.FindReplace;
import com.google.gwtexpui.safehtml.client.LinkFindReplace;
import com.google.gwtexpui.safehtml.client.RawFindReplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GerritConfig implements Cloneable {
  protected String registerUrl;
  protected String registerText;
  protected String httpPasswordUrl;
  protected String reportBugUrl;

  protected GitwebConfig gitweb;
  protected boolean useContributorAgreements;
  protected boolean useContactInfo;
  protected boolean allowRegisterNewEmail;
  protected AuthType authType;
  protected Set<DownloadScheme> downloadSchemes;
  protected Set<DownloadCommand> downloadCommands;
  protected String gitDaemonUrl;
  protected String gitHttpUrl;
  protected String sshdAddress;
  protected String editFullNameUrl;
  protected Project.NameKey wildProject;
  protected Set<Account.FieldName> editableAccountFields;
  protected boolean documentationAvailable;
  protected boolean testChangeMerge;
  protected String anonymousCowardName;
  protected int suggestFrom;

  // Hack to pass FindReplace across the JSON serialization boundary, which
  // doesn't work with interfaces.
  public static class CommentLink {
    public static CommentLink newCommentLink(String find, String link) {
      return new CommentLink(find, link, true);
    }

    public static CommentLink newRawCommentLink(String find, String repl) {
      return new CommentLink(find, repl, false);
    }

    protected String find;
    protected String repl;
    protected boolean isLink;

    protected CommentLink() {
    }

    private CommentLink(String find, String repl, boolean isLink) {
      this.find = find;
      this.repl = repl;
      this.isLink = isLink;
    }

    public FindReplace asFindReplace() {
      if (isLink) {
        return new LinkFindReplace(find, repl);
      } else {
        return new RawFindReplace(find, repl);
      }
    }
  }
  protected List<CommentLink> commentLinks;
  private transient List<FindReplace> findReplaceLinks;

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(final String u) {
    registerUrl = u;
  }

  public String getRegisterText() {
    return registerText;
  }

  public void setRegisterText(final String t) {
    registerText = t;
  }

  public String getReportBugUrl() {
    return reportBugUrl;
  }

  public void setReportBugUrl(String u) {
    reportBugUrl = u;
  }

  public String getEditFullNameUrl() {
    return editFullNameUrl;
  }

  public void setEditFullNameUrl(String u) {
    editFullNameUrl = u;
  }

  public String getHttpPasswordUrl() {
    return httpPasswordUrl;
  }

  public void setHttpPasswordUrl(String url) {
    httpPasswordUrl = url;
  }

  public AuthType getAuthType() {
    return authType;
  }

  public void setAuthType(final AuthType t) {
    authType = t;
  }

  public Set<DownloadScheme> getDownloadSchemes() {
    return downloadSchemes;
  }

  public void setDownloadSchemes(final Set<DownloadScheme> s) {
    downloadSchemes = s;
  }

  public Set<DownloadCommand> getDownloadCommands() {
    return downloadCommands;
  }

  public void setDownloadCommands(final Set<DownloadCommand> downloadCommands) {
    this.downloadCommands = downloadCommands;
  }

  public GitwebConfig getGitwebLink() {
    return gitweb;
  }

  public void setGitwebLink(final GitwebConfig w) {
    gitweb = w;
  }

  public boolean isUseContributorAgreements() {
    return useContributorAgreements;
  }

  public void setUseContributorAgreements(final boolean r) {
    useContributorAgreements = r;
  }

  public boolean isUseContactInfo() {
    return useContactInfo;
  }

  public void setUseContactInfo(final boolean r) {
    useContactInfo = r;
  }

  public String getGitDaemonUrl() {
    return gitDaemonUrl;
  }

  public void setGitDaemonUrl(String url) {
    if (url != null && !url.endsWith("/")) {
      url += "/";
    }
    gitDaemonUrl = url;
  }

  public String getGitHttpUrl() {
    return gitHttpUrl;
  }

  public void setGitHttpUrl(String url) {
    if (url != null && !url.endsWith("/")) {
      url += "/";
    }
    gitHttpUrl = url;
  }

  public String getSshdAddress() {
    return sshdAddress;
  }

  public void setSshdAddress(final String addr) {
    sshdAddress = addr;
  }

  public Project.NameKey getWildProject() {
    return wildProject;
  }

  public void setWildProject(final Project.NameKey wp) {
    wildProject = wp;
  }

  public boolean canEdit(final Account.FieldName f) {
    return editableAccountFields.contains(f);
  }

  public Set<Account.FieldName> getEditableAccountFields() {
    return editableAccountFields;
  }

  public void setEditableAccountFields(final Set<Account.FieldName> af) {
    editableAccountFields = af;
  }

  public List<FindReplace> getCommentLinks() {
    if (findReplaceLinks == null) {
      if (commentLinks != null) {
        findReplaceLinks = new ArrayList<FindReplace>(commentLinks.size());
        for (CommentLink cl : commentLinks) {
          findReplaceLinks.add(cl.asFindReplace());
        }
        findReplaceLinks = Collections.unmodifiableList(findReplaceLinks);
      }
    }
    return findReplaceLinks;
  }

  public List<CommentLink> getSerializableCommentLinks() {
    return commentLinks;
  }

  public void setSerializableCommentLinks(List<CommentLink> commentLinks) {
    findReplaceLinks = null;
    this.commentLinks = Collections.unmodifiableList(commentLinks);
  }

  public boolean isDocumentationAvailable() {
    return documentationAvailable;
  }

  public void setDocumentationAvailable(final boolean available) {
    documentationAvailable = available;
  }

  public boolean testChangeMerge() {
    return testChangeMerge;
  }

  public void setTestChangeMerge(final boolean test) {
    testChangeMerge = test;
  }

  public String getAnonymousCowardName() {
    return anonymousCowardName;
  }

  public void setAnonymousCowardName(final String anonymousCowardName) {
    this.anonymousCowardName = anonymousCowardName;
  }

  public int getSuggestFrom() {
    return suggestFrom;
  }

  public void setSuggestFrom(final int suggestFrom) {
    this.suggestFrom = suggestFrom;
  }

  public boolean siteHasUsernames() {
    if (getAuthType() == AuthType.CUSTOM_EXTENSION
        && getHttpPasswordUrl() != null
        && !canEdit(FieldName.USER_NAME)) {
      return false;
    }
    return true;
  }
}
