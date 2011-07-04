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

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// The Java class will be hosted at the URI path "/users"
@Path("/users")
public class Users {
    
    // maintain a list of users as a JSONArray for easy returning
    private static final JSONArray USER_LIST = new JSONArray();




    static {

	// put in a couple of hardcoded demo users
	JSONObject demoUser = new JSONObject();
	try {

	    demoUser.put("userID", "aadams");
	    demoUser.put("firstName", "Alice");
	    demoUser.put("lastName", "Addams");
	    demoUser.put("email", "aadams@amazon.com");
	    demoUser.put("password", "aaa");
	    //	    demoUser.put("birthday", 
	    //		    DateFormat.getDateInstance().parse("02/22/78"));

	    USER_LIST.put(demoUser);

	    demoUser = new JSONObject();
	    demoUser.put("userID", "bbrown");
	    demoUser.put("firstName", "Bob");
	    demoUser.put("lastName", "Brown");
	    demoUser.put("email", "bbrown@bn.com");
	    demoUser.put("password", "b0bpass");
	    //	    demoUser.put("birthday", 
	    // DateFormat.getDateInstance().parse("12/11/90"));
	    USER_LIST.put(demoUser);

	}
	catch (JSONException je)
	    {
		// not going to happen with this hardcoded data!
	    }
	/*	catch (java.text.ParseException pe)
	    {

		// also not going to happen
		}*/
    }


    static JSONArray getUserList(){
	return USER_LIST;
    }

    // The Java method will process HTTP GET requests
    @GET 
    // The Java method will produce content identified by the MIME Media
    // type "application/JSON"
    @Produces("application/JSON")
    public String doGetRequest() {
	if (USER_LIST != null && USER_LIST.length() == 0)
	    {
		return "{\"error\": \"user list has no entries\"}";
	    }
	if (USER_LIST != null && USER_LIST.length() != 0)
	    return USER_LIST.toString();
	else
	    return "{\"error\": \"user list not initialized\"}";
}

    // what happens if you PUT or POST here? Doesn't seem interesting. 
}
