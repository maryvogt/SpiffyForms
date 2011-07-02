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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;


import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

// The Java class will be hosted at the URI path "/users"
@Path("/users/{arg1}")
public class User {

    HashMap m_users; 

    // Constructor
    User()
    {
        m_users = new HashMap(); 
        // add a couple of hardcoded demo users
        UserDetailsBean alice = new UserDetailsBean( "aadams", 
                                                     "Alice", 
                                                     "Adams", 
                                                     "aadams@amazon.com", 
                                                     "passw0rd",
                                                     "04/01/1974");
        UserDetailsBean bob = new UserDetailsBean("bbrown", 
                                                  "Bob",
                                                  "Brown", 
                                                  "bbrown@bn.com",
                                                  "b0bpass",
                                                  "10/21/1981");
        m_users.put(alice.getUserID(), alice);
        m_users.put(bob.getUserID(), bob);




    }

    @Context UriInfo uriInfo;

    // The Java method will process HTTP GET requests
    @GET 
    // The Java method will produce content identified by the MIME Media
    // type "application/JSON"
    @Produces("application/JSON")
    // This method returns a JSONObject containing the user info 
    // for the user named in the arg1 parameter on the URL
    public UserDetailsBean getUserInfo() {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userid= params.getFirst("arg1");

        UserDetailsBean user = (UserDetailsBean)m_users.get(userid);
        if (user != null) {
            // found this user, return info
            return user;
        }
        else
        {
            // 404
            // If you want to add more information to the error, then write a subclass of 
            // WebApplicationException and throw that. 
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @POST 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    // This method attempts to create new user info based on the info in the input string
    public String createUser(String input) {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String arg1 = params.getFirst("arg1");

        // Return some cliched textual content
        return "You're creating " + arg1 + "with data: " + input;
    }

    @PUT 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String updateUser(String input) {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String arg1 = params.getFirst("arg1");

        // Return some cliched textual content
        return "you updated user " + arg1 + " with: " + input;
    }

    @DELETE 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String deleteUser(String input) {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String arg1 = params.getFirst("arg1");

        // Return some cliched textual content
        return "you deleted user " + arg1 + " with: " + input;
    }
}
