/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.ground.common.model.core;

import static edu.berkeley.ground.common.util.ModelTestUtils.convertFromClassToString;
import static edu.berkeley.ground.common.util.ModelTestUtils.convertFromStringToClass;
import static edu.berkeley.ground.common.util.ModelTestUtils.readFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import edu.berkeley.ground.common.model.version.GroundType;
import edu.berkeley.ground.common.model.version.Tag;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class NodeTest {

  @Test
  public void serializesToJSON() throws Exception {
    Map<String, Tag> tagsMap = new HashMap<>();
    tagsMap.put("testtag", new Tag(1, "testtag", "tag", GroundType.STRING));

    final Node node = new Node(1, "test", "testKey", tagsMap);

    final String expected = convertFromClassToString(convertFromStringToClass(readFromFile
      ("test/resources/fixtures/core/node.json"), Node.class));
    assertEquals(convertFromClassToString(node), expected);
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    Map<String, Tag> tagsMap = new HashMap<>();
    tagsMap.put("testtag", new Tag(1, "testtag", "tag", GroundType.STRING));

    final Node node = new Node(1, "test", "testKey", tagsMap);
    assertEquals(convertFromStringToClass(
      readFromFile("test/resources/fixtures/core/node.json"), Node.class), node);
  }

  @Test
  public void testNodeNotEquals() throws Exception {
    Node truth = new Node(1, "name", "sourceKey", new HashMap<>());
    assertFalse(truth.equals("notNode"));

    Node differentId = new Node(2, "name", "sourceKey", new HashMap<>());
    assertFalse(truth.equals(differentId));

    Node differentName = new Node(1, "notName", "sourceKey", new HashMap<>());
    assertFalse(truth.equals(differentName));

    Node differentKey = new Node(1, "name", "notSourceKey", new HashMap<>());
    assertFalse(truth.equals(differentKey));

    Map<String, Tag> tags = new HashMap<>();
    tags.put("test", new Tag(1, "test", 1L, GroundType.LONG));
    Node differentTags = new Node(1, "name", "sourceKey", tags);
    assertFalse(truth.equals(differentTags));
  }
}
