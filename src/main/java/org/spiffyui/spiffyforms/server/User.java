/*******************************************************************************
 *
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
 *
 ******************************************************************************/
package org.spiffyui.spiffyforms.server;

import java.net.URI;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class supports our REST endpoint for getting user data, updating users, 
 * creating new users, and deleting existing users.  It serves at  /user/{userid}.
 */
@Path("/users/{arg1}")
public class User
{
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    
    static private final String RESULT_SUCCESS = "{\"result\" : \"success\"}";
    static private final String USER_NOT_FOUND = "Userid not found: ";

    @Context
    UriInfo uriInfo;

    // this Java method finds a particular user in the list of users
    private static JSONObject findUserInArray(String userID)
    {
        JSONArray users = Users.getUserList();
        if (users == null)
            return null;

        int len = users.length();
        try {
            for (int i = 0; i < len; i++) {
                if (!users.isNull(i) && userID.equals(users.getJSONObject(i).getString("userID"))) {
                    // found it!
                    return users.getJSONObject(i);
                }
            }
        } catch (JSONException je) {
            LOGGER.throwing(User.class.getName(), "findUserInArray", je);
        }

        // if we got here then we didn't find it
        return null;
    }

    // this Java method finds a particular user in the list of users
    private static int findUserIndexInArray(String userID)
    {
        JSONArray users = Users.getUserList();
        if (users == null)
            return -1;

        int len = users.length();
        try {
            for (int i = 0; i < len; i++) {
                if (!users.isNull(i) && userID.equals(users.getJSONObject(i).getString("userID"))) {
                    // found it!
                    return i;
                }
            }
        } catch (JSONException je) {
            LOGGER.throwing(User.class.getName(), "findUserIndexArray", je);
        }

        // if we got here then we didn't find it
        return -1;
    }

    /**
     * Get details about the specified user or return a 404 if the user isn't found.
     */
    @GET
    @Produces("application/json")
    public String getUserInfo()
    {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userid = params.getFirst("arg1");
        // we know that userid is not null because of the @path annotation

        JSONObject user = findUserInArray(userid);
        if (user == null) {
            throw buildException(Response.Status.NOT_FOUND, USER_NOT_FOUND + userid);
        }

        return user.toString();
    }

    /**
     * This method handles creating new users on the server.
     * 
     * @param input  a JSON string containing information about the new user
     * 
     * @return a success message indicating the new user was added or an error 
     *         message indicating why the weren't
     */
    @POST
    @Produces("application/JSON")
    public Response createUser(String input)
    {
        Response resp;
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userID = params.getFirst("arg1");
        // we know that userID is not null because of the Path annotation
        Response.ResponseBuilder rb;

        // userids can't have spaces
        if (userID.indexOf(' ') != -1) {
            throw buildException(Response.Status.BAD_REQUEST, "UserIDs cannot have spaces. UserID: " + userID);
        }

        // do we already have this user?
        if (findUserInArray(userID) != null) {
            throw buildException(Response.Status.NOT_FOUND, USER_NOT_FOUND + userID);
        }

        try {
            JSONArray userList = Users.getUserList();
            if (userList != null) {
                userList.put(new JSONObject(input));
            }
        } catch (JSONException je) {
            LOGGER.throwing(User.class.getName(), "createUser", je);
            // input string was probably not correctly formatted JSON.
            throw buildException(Response.Status.BAD_REQUEST, "Could not parse JSON data: " + input);
        }

        rb = Response.created(URI.create(userID));
        rb.entity(RESULT_SUCCESS);
        return rb.build();
    }

    /**
     * This method updates the information about the specified user.
     * 
     * @param input  a string of JSON containing the updated information about the user
     * 
     * @return the response to the client indicating that the user was successfully 
     *         updated or that the user couldn't be found
     */
    @PUT
    @Produces("application/json")
    public String updateUser(String input)
    {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userID = params.getFirst("arg1");
        // we know userID is non-null
        JSONObject storedUser = findUserInArray(userID);
        if (storedUser != null) {
            try {
                JSONObject inputUser = new JSONObject(input);
                Iterator iter = inputUser.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    storedUser.put(key, inputUser.get(key));
                }
            } catch (JSONException je) {
                LOGGER.throwing(User.class.getName(), "updateUser", je);
                // generic error
                throw buildException(Response.Status.BAD_REQUEST, "Could not modify user info: " + userID);
            }
        } else {
            throw buildException(Response.Status.NOT_FOUND, USER_NOT_FOUND + userID);
        }
        return RESULT_SUCCESS;
    }

    /**
     * This method deletes the specified user from the server.  If the user is found then
     * we return a 200 and a simple success response.  If the user is not found then we
     * return a 404 and a specific error message.
     * 
     * @return the response for the client
     */
    @DELETE
    @Produces("application/JSON")
    public String deleteUser()
    {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userID = params.getFirst("arg1");
        // we know userID is non-null
        int i = findUserIndexInArray(userID);
        if (i != -1) { // -1 means not found
            JSONArray userList = Users.getUserList();
            try {
                userList.put(i, (Object) null);
            } catch (JSONException je) {
                LOGGER.throwing(User.class.getName(), "deleteUser", je);
            }
            return "{\"success\":true}";
        } else {
            throw buildException(Response.Status.NOT_FOUND, USER_NOT_FOUND + userID);

        }
    }

    /*
     * Private methods
     */

    private static WebApplicationException buildException(Response.Status code, String reason)
    {
        Response r = buildErrorResponse(code, reason);
        return new WebApplicationException(r);
    }

    /**
     * This method builds a JSON error response in the specific format Spiffy knows how to
     * deal with.  You don't have to use this error format, but it makes your application
     * easier if you do.
     * 
     * @param code   the main error code
     * @param reason the reason for the error
      
     * @return a response containing the error
     */
    private static Response buildErrorResponse(Response.Status code, String reason)
    {
        JSONObject root = null;
        Response.ResponseBuilder rb = Response.status(code);

        try {
            JSONObject r = new JSONObject();
            r.put("Text", reason);
            JSONObject subcode = new JSONObject();
            subcode.put("Value", "0");
            JSONObject c = new JSONObject();
            c.put("Subcode", subcode);
            c.put("Value", code);
            JSONObject f = new JSONObject();
            f.put("Code", c);
            f.put("Reason", r);


            root = new JSONObject();
            root.put("Fault", f);
        } catch (JSONException je) {
            LOGGER.throwing(User.class.getName(), "buildErrorResponse", je);
        }

        if (root != null) {
            rb.entity(root.toString());
        }
        
        return rb.build();
    }
}
