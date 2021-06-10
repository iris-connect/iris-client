/*
 * SORMAS REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.61.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package iris.sormas.client.api;

import iris.sormas.client.model.OutbreakDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for OutbreakControllerApi
 */
@Ignore
public class OutbreakControllerApiTest {

    private final OutbreakControllerApi api = new OutbreakControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getActiveSinceTest() {
        Long since = null;
        List<OutbreakDto> response = api.getActiveSince(since);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getActiveUuidsTest() {
        List<String> response = api.getActiveUuids();

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getInactiveUuidsSinceTest() {
        Long since = null;
        List<String> response = api.getInactiveUuidsSince(since);

        // TODO: test validations
    }
}
