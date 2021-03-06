package com.marklogic.client.ext.es;

import com.marklogic.client.ext.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests against the default Documents database. Doesn't clear it out before or after.
 */
public class EntityServicesTest extends AbstractIntegrationTest {

	private final static String MODEL = "{\n" +
		"  \"info\": {\n" +
		"    \"title\": \"Penny\",\n" +
		"    \"version\": \"0.0.1\",\n" +
		"    \"baseUri\": \"http://marklogic.com/examples/\"\n" +
		"  },\n" +
		"  \"definitions\": {\n" +
		"    \"employee\": {\n" +
		"      \"description\": \"Example of an employee\",\n" +
		"      \"properties\": {\n" +
		"        \"id\": { \"datatype\": \"int\" },\n" +
		"        \"last-name\": { \"datatype\": \"string\" },\n" +
		"        \"first-name\": { \"datatype\": \"string\" },\n" +
		"        \"email\": { \"datatype\": \"string\" },\n" +
		"        \"department\": { \"datatype\": \"string\" },\n" +
		"        \"salary\": { \"datatype\": \"float\" }\n" +
		"      },\n" +
		"      \"required\": [\"id\", \"last-name\", \"first-name\"],\n" +
		"      \"primaryKey\": \"id\",\n" +
		"      \"rangeIndex\": [\"salary\", \"department\"]\n" +
		"    }\n" +
		"  }\n" +
		"}";

	@Before
	public void setup() {
		client = newClient("Documents");
	}

	@Test
	public void generateCode() {
		EntityServicesManager mgr = new EntityServicesManager(client);
		String modelUri = mgr.loadModel("penny.json", MODEL);
		assertEquals("/marklogic.com/entity-services/models/penny.json", modelUri);

		GeneratedCode code = mgr.generateCode(modelUri, new CodeGenerationRequest());
		assertEquals("Penny", code.getTitle());
		assertEquals("0.0.1", code.getVersion());
		assertNotNull(code.getDatabaseProperties());
		assertNotNull(code.getExtractionTemplate());
		assertNotNull(code.getInstanceConverter());
		assertNotNull(code.getSchema());
		assertNotNull(code.getSearchOptions());
	}

	@Test
	public void generateVersionTranslator() {
		EntityServicesManager mgr = new EntityServicesManager(client);
		String modelUri = mgr.loadModel("penny.json", MODEL);

		String newModel = MODEL.replace("department", "newDepartment");
		String newModelUri = mgr.loadModel("penny2.json", MODEL);

		String output = mgr.generateVersionTranslator(modelUri, newModelUri);
		assertNotNull("Just making sure this doesn't throw an error", output);
	}
}


