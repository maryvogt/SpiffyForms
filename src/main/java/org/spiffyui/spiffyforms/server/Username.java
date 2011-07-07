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
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

// The Java class will be hosted at the URI path "/users"
@Path("/usernames/{arg1}")
public class Username {
    static final String RESULT_TRUE = "{\"exists\" : true}";
	static final String RESULT_FALSE = "{\"exists\" : false}";

    @Context UriInfo uriInfo;

    // The Java method will process HTTP GET requests
    @GET 
    // The Java method will produce content identified by the MIME Media
    // type "application/JSON"
    //    @Produces("application/JSON")
    // This method returns a JSONObject containing the user info 
    // for the userID passed in the arg1 parameter on the URL
    public String getUsername() {
        MultivaluedMap<String, String> params = uriInfo.getPathParameters();
        String userid = params.getFirst("arg1");
	
		if (userid == null) {
			throw new WebApplicationException(400);
		}
	
		JSONObject user = User.findUserInArray(userid);
		if (user == null) {
			return RESULT_FALSE;
		} else {
			return RESULT_TRUE;
		}
    }
}
