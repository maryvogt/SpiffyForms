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


import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

// The Java class will be hosted at the URI path "/users"
@Path("/users/{arg1}")
public class User {
    @Context UriInfo uriInfo;

    // The Java method will process HTTP GET requests
    @GET 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String arg1 = params.getFirst("arg1");

        // Return some cliched textual content
        return "You asked for user: " + arg1;
    }

    @POST 
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
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
