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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This servlet returns TODO
 * GET - get list of users or get individual user
 * POST - modify existing user
 * PUT - add new user
 * DELETE - delete a user
 * 
 * 
 *  */
public class UserServlet extends HttpServlet
{

    private static final long serialVersionUID = -1L;
    private static final JSONArray USERS;
    
    public UserServlet()
    {
        
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        String user = request.getPathInfo();


        if (user.startsWith("/")) {    // it is supposed to
            user = path.substring(1);
        }


        if (user.equals("") {
            // they are asking for the list of users
            // which we are just keeping in memory in this servlet
            // because this is a sample app

        } else {
            // they are asking for one user
            
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        /*
           so what are we going to return?
         */

        // do they want all the users or just a single user? The URL tells us.
        // .../users
        // .../users/Mary
        

    }
}
