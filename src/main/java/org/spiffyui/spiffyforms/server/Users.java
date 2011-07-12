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

import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class supports our REST endpoint for getting a list of the users.  It serves at
 * /users and only supports GET requests.
 */
@Path("/users")
public class Users
{
    /**
     * This single variable is our server-side storage.  In a real application you would
     * probably write these users to a database or an LDAP server, but this sample just
     * holds all the users in memory as a JSONArray.  That makes it easy to return them
     * to the client.
     */
    private static final JSONArray USER_LIST = new JSONArray();
    
    private static final Logger LOGGER = Logger.getLogger(Users.class.getName());

    static {

        // put in a couple of hardcoded demo users
        JSONObject demoUser = new JSONObject();
        try {

            demoUser.put("userID", "aadams");
            demoUser.put("firstName", "Alice");
            demoUser.put("lastName", "Addams");
            demoUser.put("email", "aadams@amazon.com");
            demoUser.put("password", "aaa");
            demoUser.put("birthday", "-11995200000");
            demoUser.put("gender", "female");
            demoUser.put("desc", "Alice Addams is just some girl");

            USER_LIST.put(demoUser);

            demoUser = new JSONObject();
            demoUser.put("userID", "bbrown");
            demoUser.put("firstName", "Bob");
            demoUser.put("lastName", "Brown");
            demoUser.put("email", "bbrown@bn.com");
            demoUser.put("password", "b0bpass");
            demoUser.put("birthday", "269582400000");
            demoUser.put("gender", "male");
            demoUser.put("desc", "Bob Brown is just some guy");
            USER_LIST.put(demoUser);

        } catch (JSONException je) {
            LOGGER.throwing(Users.class.getName(), "initialization", je);
        }
        /*
         * catch (java.text.ParseException pe) { // also not going to happen }
         */
    }

    static JSONArray getUserList()
    {
        return USER_LIST;
    }

    
    /**
     * Get the list of users currently on the server.
     */
    @GET
    @Produces("application/JSON")
    public String doGetRequest()
    {
        return USER_LIST.toString();
    }
}
