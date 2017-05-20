package edu.berkeley.ground.postgres.utils;

import edu.berkeley.ground.common.exception.GroundException;
import edu.berkeley.ground.common.util.IdGenerator;
import edu.berkeley.ground.postgres.dao.core.EdgeDao;
import edu.berkeley.ground.postgres.dao.core.EdgeVersionDao;
import edu.berkeley.ground.postgres.dao.core.GraphDao;
import edu.berkeley.ground.postgres.dao.core.GraphVersionDao;
import edu.berkeley.ground.postgres.dao.core.NodeDao;
import edu.berkeley.ground.postgres.dao.core.NodeVersionDao;
import edu.berkeley.ground.postgres.dao.core.RichVersionDao;
import edu.berkeley.ground.postgres.dao.core.StructureDao;
import edu.berkeley.ground.postgres.dao.core.StructureVersionDao;
import edu.berkeley.ground.postgres.dao.usage.LineageEdgeDao;
import edu.berkeley.ground.postgres.dao.usage.LineageEdgeVersionDao;
import edu.berkeley.ground.postgres.dao.usage.LineageGraphDao;
import edu.berkeley.ground.postgres.dao.usage.LineageGraphVersionDao;
import edu.berkeley.ground.postgres.dao.version.ItemDao;
import edu.berkeley.ground.postgres.dao.version.TagDao;
import edu.berkeley.ground.postgres.dao.version.VersionDao;
import edu.berkeley.ground.postgres.dao.version.VersionHistoryDagDao;
import edu.berkeley.ground.postgres.dao.version.VersionSuccessorDao;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.db.Database;

@Singleton
public class Daos {

  private final StructureDao structureDao;
  private final StructureVersionDao structureVersionDao;
  private final EdgeDao edgeDao;
  private final EdgeVersionDao edgeVersionDao;
  private final GraphDao graphDao;
  private final GraphVersionDao graphVersionDao;
  private final NodeDao nodeDao;
  private final NodeVersionDao nodeVersionDao;
  private final RichVersionDao richVersionDao;

  private final LineageEdgeDao lineageEdgeDao;
  private final LineageEdgeVersionDao lineageEdgeVersionDao;
  private final LineageGraphDao lineageGraphDao;
  private final LineageGraphVersionDao lineageGraphVersionDao;

  private final ItemDao itemDao;
  private final VersionDao versionDao;


  /**
   * Create the  factories.
   */
  @Inject
  public Daos(Database dbSource, IdGenerator idGenerator) throws GroundException {

    VersionSuccessorDao versionSuccessorDao = new VersionSuccessorDao(dbSource, idGenerator);
    VersionHistoryDagDao versionHistoryDagDao = new VersionHistoryDagDao(dbSource,
      versionSuccessorDao);
    TagDao tagDao = new TagDao(dbSource, idGenerator);

    this.structureDao = new StructureDao(dbSource, idGenerator);
    this.structureVersionDao = new StructureVersionDao(dbSource, idGenerator);
    this.edgeDao = new EdgeDao(dbSource, idGenerator);
    this.edgeVersionDao = new EdgeVersionDao(dbSource, idGenerator);
    this.richVersionDao = new RichVersionDao(dbSource, idGenerator);
    this.graphDao = new GraphDao(dbSource, idGenerator);
    this.graphVersionDao = new GraphVersionDao(dbSource, idGenerator);
    this.nodeDao = new NodeDao(dbSource, idGenerator);
    this.nodeVersionDao = new NodeVersionDao(dbSource, idGenerator);

    this.lineageEdgeDao = new LineageEdgeDao(dbSource, idGenerator);
    this.lineageEdgeVersionDao = new LineageEdgeVersionDao(dbSource, idGenerator);
    this.lineageGraphDao = new LineageGraphDao(dbSource, idGenerator);
    this.lineageGraphVersionDao = new LineageGraphVersionDao(dbSource, idGenerator);

    this.itemDao = new ItemDao(dbSource, idGenerator, versionHistoryDagDao, tagDao);
    this.versionDao = new VersionDao(dbSource, idGenerator);
  }

  public EdgeDao getEdgeDao() {
    return this.edgeDao;
  }

  public EdgeVersionDao getEdgeVersionDao() {
    return this.edgeVersionDao;
  }

  public GraphDao getGraphDao() {
    return this.graphDao;
  }

  public GraphVersionDao getGraphVersionDao() {
    return this.graphVersionDao;
  }

  public NodeDao getNodeDao() {
    return this.nodeDao;
  }

  public NodeVersionDao getNodeVersionDao() {
    return this.nodeVersionDao;
  }

  public LineageEdgeDao getLineageEdgeDao() {
    return this.lineageEdgeDao;
  }

  public LineageEdgeVersionDao getLineageEdgeVersionDao() {
    return this.lineageEdgeVersionDao;
  }

  public StructureDao getStructureDao() {
    return this.structureDao;
  }

  public StructureVersionDao getStructureVersionDao() {
    return this.structureVersionDao;
  }

  public LineageGraphDao getLineageGraphDao() {
    return this.lineageGraphDao;
  }

  public LineageGraphVersionDao getLineageGraphVersionDao() {
    return this.lineageGraphVersionDao;
  }

  public RichVersionDao getRichVersionDao() {
    return this.richVersionDao;
  }

  public ItemDao getItemDao() {
    return this.itemDao;
  }

  public VersionDao getVersionDao() {
    return this.versionDao;
  }

}
