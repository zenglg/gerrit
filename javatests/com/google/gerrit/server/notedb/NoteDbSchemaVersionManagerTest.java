package com.google.gerrit.server.notedb;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;
import static com.google.gerrit.reviewdb.client.RefNames.REFS_VERSION;

import com.google.gerrit.server.config.AllProjectsName;
import com.google.gerrit.server.git.GitRepositoryManager;
import com.google.gerrit.testing.GerritBaseTests;
import com.google.gerrit.testing.InMemoryRepositoryManager;
import com.google.gwtorm.server.OrmException;
import org.eclipse.jgit.junit.TestRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

public class NoteDbSchemaVersionManagerTest extends GerritBaseTests {
  private NoteDbSchemaVersionManager manager;
  private TestRepository<?> tr;

  @Before
  public void setUp() throws Exception {
    AllProjectsName allProjectsName = new AllProjectsName("The-Projects");
    GitRepositoryManager repoManager = new InMemoryRepositoryManager();
    tr = new TestRepository<>(repoManager.createRepository(allProjectsName));
    manager = new NoteDbSchemaVersionManager(allProjectsName, repoManager);
  }

  @Test
  public void readMissing() throws Exception {
    assertThat(manager.read()).isEqualTo(0);
  }

  @Test
  public void read() throws Exception {
    tr.update(REFS_VERSION, tr.blob("123"));
    assertThat(manager.read()).isEqualTo(123);
  }

  @Test
  public void readInvalid() throws Exception {
    ObjectId blobId = tr.blob(" 1 2 3 ");
    tr.update(REFS_VERSION, blobId);
    try {
      manager.read();
      assert_().fail("expected OrmException");
    } catch (OrmException e) {
      assertThat(e)
          .hasMessageThat()
          .isEqualTo("invalid value in refs/meta/version blob at " + blobId.name());
    }
  }

  @Test
  public void incrementFromMissing() throws Exception {
    manager.increment(123);
    assertThat(manager.read()).isEqualTo(124);
  }

  @Test
  public void increment() throws Exception {
    tr.update(REFS_VERSION, tr.blob("123"));
    manager.increment(123);
    assertThat(manager.read()).isEqualTo(124);
  }

  @Test
  public void incrementWrongOldVersion() throws Exception {
    tr.update(REFS_VERSION, tr.blob("123"));
    try {
      manager.increment(456);
      assert_().fail("expected OrmException");
    } catch (OrmException e) {
      assertThat(e)
          .hasMessageThat()
          .isEqualTo("Expected old version 456 for refs/meta/version, found 123");
    }
  }
}
