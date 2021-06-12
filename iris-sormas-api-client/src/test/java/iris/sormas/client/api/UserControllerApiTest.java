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

import iris.sormas.client.model.PageUserDto;
import iris.sormas.client.model.UserDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UserControllerApi
 */
@Ignore
public class UserControllerApiTest {

    private final UserControllerApi api = new UserControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAll11Test() {
        Long since = null;
        List<UserDto> response = api.getAll11(since);

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
    public void getAllUuids19Test() {
        List<String> response = api.getAllUuids19();

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
    public void getByUuids29Test() {
        List<String> body = null;
        List<UserDto> response = api.getByUuids29(body);

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
    public void getIndexList6Test() {
        Integer offset = null;
        Integer size = null;
        PageUserDto response = api.getIndexList6(offset, size);

        // TODO: test validations
    }
}