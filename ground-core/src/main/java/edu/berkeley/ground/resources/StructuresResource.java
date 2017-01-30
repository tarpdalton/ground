/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.ground.resources;

import com.codahale.metrics.annotation.Timed;
import edu.berkeley.ground.api.models.Structure;
import edu.berkeley.ground.api.models.StructureFactory;
import edu.berkeley.ground.api.models.StructureVersion;
import edu.berkeley.ground.api.models.StructureVersionFactory;
import edu.berkeley.ground.exceptions.GroundException;
import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/structures")
@Api(value = "/structures", description = "Interact with the structures in the graph")
@Produces(MediaType.APPLICATION_JSON)
public class StructuresResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructuresResource.class);

    private StructureFactory structureFactory;
    private StructureVersionFactory structureVersionFactory;
    public StructuresResource(StructureFactory structureFactory, StructureVersionFactory structureVersionFactory) {
        this.structureFactory = structureFactory;
        this.structureVersionFactory = structureVersionFactory;
    }

    @GET
    @Timed
    @Path("/{name}")
    public Structure getStructure(@PathParam("name") String name) throws GroundException {
        LOGGER.info("Retrieving structure " + name + ".");
        return this.structureFactory.retrieveFromDatabase(name);
    }

    @GET
    @Timed
    @Path("/versions/{id}")
    public StructureVersion getStructureVersion(@PathParam("id") String id) throws GroundException {
        LOGGER.info("Retrieving structure version " + id + ".");
        return this.structureVersionFactory.retrieveFromDatabase(id);
    }

    @POST
    @Timed
    @Path("/{name}")
    public Structure createStructure(@PathParam("name") String name) throws GroundException {
        LOGGER.info("Creating structure " + name + ".");
        return this.structureFactory.create(name);
    }

    @POST
    @Timed
    @Path("/versions")
    public StructureVersion createStructureVersion(@Valid StructureVersion structureVersion, @QueryParam("parent") List<String> parentIds) throws GroundException {
        LOGGER.info("Creating structure version in structure " + structureVersion.getStructureId() + ".");
        return this.structureVersionFactory.create(structureVersion.getStructureId(),
                                                   structureVersion.getAttributes(),
                                                   parentIds);
    }

    @GET
    @Timed
    @Path("/{name}/latest")
    public List<String> getLatestVersions(@PathParam("name") String name) throws GroundException {
        LOGGER.info("Retrieving the latest version of node " + name + ".");
        return this.structureFactory.getLeaves(name);
    }
}
