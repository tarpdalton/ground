package edu.berkeley.ground.api.usage;

import edu.berkeley.ground.db.DBClient.GroundDBConnection;
import edu.berkeley.ground.exceptions.GroundException;

import java.util.List;

public abstract class LineageEdgeFactory {
    public abstract LineageEdge create(String name) throws GroundException;

    public abstract LineageEdge retrieveFromDatabase(String name) throws GroundException;

    public abstract void update(GroundDBConnection connection, String itemId, String childId, List<String> parentIds) throws GroundException;

    public static LineageEdge construct(String id, String name) {
        return new LineageEdge(id, name);
    }
}
