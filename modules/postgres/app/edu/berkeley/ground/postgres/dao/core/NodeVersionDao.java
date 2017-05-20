package edu.berkeley.ground.postgres.dao.core;

import com.fasterxml.jackson.databind.JsonNode;
import edu.berkeley.ground.common.exception.GroundException;
import edu.berkeley.ground.common.factory.core.NodeVersionFactory;
import edu.berkeley.ground.common.model.core.NodeVersion;
import edu.berkeley.ground.common.model.core.RichVersion;
import edu.berkeley.ground.common.util.IdGenerator;
import edu.berkeley.ground.postgres.dao.version.ItemDao;
import edu.berkeley.ground.postgres.dao.version.TagDao;
import edu.berkeley.ground.postgres.dao.version.VersionHistoryDagDao;
import edu.berkeley.ground.postgres.dao.version.VersionSuccessorDao;
import edu.berkeley.ground.postgres.utils.PostgresStatements;
import edu.berkeley.ground.postgres.utils.PostgresUtils;
import java.util.List;
import play.db.Database;
import play.libs.Json;

public class NodeVersionDao extends RichVersionDao<NodeVersion> implements NodeVersionFactory {

  public NodeVersionDao(Database dbSource, IdGenerator idGenerator) {
    super(dbSource, idGenerator);
  }

  @Override
  public NodeVersion create(final NodeVersion nodeVersion, List<Long> parentIds)
    throws GroundException {

    //Call super.create to ensure that versions exist
    final long uniqueId = idGenerator.generateVersionId();
    NodeVersion newNodeVersion = new NodeVersion(uniqueId, nodeVersion.getTags(),
      nodeVersion.getStructureVersionId(),
      nodeVersion.getReference(), nodeVersion.getParameters(), nodeVersion.getNodeId());

    VersionSuccessorDao versionSuccessorDao = new VersionSuccessorDao(dbSource, idGenerator);
    VersionHistoryDagDao versionHistoryDagDao = new VersionHistoryDagDao(dbSource,
      versionSuccessorDao);
    TagDao tagDao = new TagDao(dbSource, idGenerator);

    ItemDao itemDao = new ItemDao(dbSource, idGenerator, versionHistoryDagDao, tagDao);
    PostgresStatements updateVersionList = itemDao
      .update(newNodeVersion.getNodeId(), newNodeVersion.getId(), parentIds);

    try {
      PostgresStatements statements = super.insert(newNodeVersion);
      statements.append(String.format(
        "insert into node_version (id, node_id) values (%d,%d)",
        uniqueId, nodeVersion.getNodeId()));
      statements.merge(updateVersionList);

      PostgresUtils.executeSqlList(dbSource, statements);
    } catch (Exception e) {
      e.printStackTrace();
      throw new GroundException(e);
    }
    return newNodeVersion;
  }

  @Override
  public NodeVersion retrieveFromDatabase(long id) throws GroundException {
    String sql = String.format("select * from node_version where id=%d", id);
    JsonNode json = Json.parse(PostgresUtils.executeQueryToJson(dbSource, sql));
    if (json.size() == 0) {
      throw new GroundException(String.format("Node Version with id %d does not exist.", id));
    }
    NodeVersion nodeVersion = Json.fromJson(json.get(0), NodeVersion.class);
    RichVersion richVersion = super.retrieveFromDatabase(id);
    return new NodeVersion(id, richVersion.getTags(), richVersion.getStructureVersionId(),
      richVersion.getReference(),
      richVersion.getParameters(), nodeVersion.getNodeId());
  }
}
