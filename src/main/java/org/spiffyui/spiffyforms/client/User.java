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
package org.spiffyui.spiffyforms.client;

import java.util.ArrayList;
import java.util.Date;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

import com.google.gwt.http.client.URL;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * This class is our RESTBean for managing users.  It handles create, read, update, and
 * delete of users from the server.  This class encapsulates all of the REST functions
 * of our application and is responsible for turning JSON data into a Java object.
 */
class User
{
    private String m_firstName = "";
    private String m_lastName = "";
    private String m_email = "";
    private String m_password = "";
    private Date m_bDay = null;
    private String m_userDesc = "";
    private String m_userId = "";
    private String m_gender = "male";
    
    private boolean m_isNew = true;

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String s)
    {
        m_firstName = s;
    }


    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String s)
    {
        m_lastName = s;
    }

    public String getEmail()
    {
        return m_email;
    }

    public void setEmail(String s)
    {
        m_email = s;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword(String s)
    {
        m_password = s;
    }

    public String getUserDesc()
    {
        return m_userDesc;
    }

    public void setUserDesc(String s)
    {
        m_userDesc = s;
    }

    public Date getBirthday()
    {
        return m_bDay;
    }

    public void setBirthday(Date d)
    {
        m_bDay = d;
    }
    
    public String getUserId()
    {
        return m_userId;
    }

    public void setUserId(String id)
    {
        m_userId = id;
    }
    
    public String getGender()
    {
        return m_gender;
    }

    public void setGender(String gender)
    {
        m_gender = gender;
    }
    
    public boolean isNew()
    {
        return m_isNew;
    }
    
    /**
     * Get the list of users from the server
     * 
     * @param callback the callback for getting the list of users
     */
    public static void getUsers(final RESTObjectCallBack<User[]> callback)
    {
        RESTility.callREST("api/users", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    JSONArray usersArray = val.isArray();
                    ArrayList<User> users = new ArrayList<User>();
                    
                    for (int i = 0; i < usersArray.size(); i++) {
                        if (usersArray.get(i).isNull() != null) {
                            continue;
                        }
                        
                        User u = new User();
                        
                        u.setFirstName(JSONUtil.getStringValue(usersArray.get(i).isObject(), "firstName"));
                        u.setLastName(JSONUtil.getStringValue(usersArray.get(i).isObject(), "lastName"));
                        u.setPassword(JSONUtil.getStringValue(usersArray.get(i).isObject(), "password"));
                        u.setUserId(JSONUtil.getStringValue(usersArray.get(i).isObject(), "userID"));
                        u.setEmail(JSONUtil.getStringValue(usersArray.get(i).isObject(), "email"));
                        u.setUserDesc(JSONUtil.getStringValue(usersArray.get(i).isObject(), "desc"));
                        u.setGender(JSONUtil.getStringValue(usersArray.get(i).isObject(), "gender"));
                        u.setBirthday(JSONUtil.getDateValue(usersArray.get(i).isObject(), "birthday"));
                        u.m_isNew = false;
                        
                        users.add(u);
                    }
                    
                    callback.success(users.toArray(new User[users.size()]));
                    
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    callback.error(errorResponse);
                }
                
                @Override
                public void onError(RESTException e)
                {
                    callback.error(e);
                }
	
	    });
    }
    
    /**
     * Save the specified user to the server.  This method handles new users as well as updating
     * existing users.
     * 
     * @param callback the callback to get the status of the save operation
     */
    public void save(final RESTObjectCallBack<Boolean> callback)
    {
        
        JSONObject user = new JSONObject();
        
        user.put("firstName", new JSONString(getFirstName()));
        user.put("lastName", new JSONString(getLastName()));
        user.put("email", new JSONString(getEmail()));
        user.put("birthday", new JSONString("" + getBirthday().getTime()));
        user.put("desc", new JSONString(getUserDesc()));
        user.put("gender", new JSONString(getGender()));
        user.put("password", new JSONString(getPassword()));
        user.put("userID", new JSONString(getUserId()));
        
        RESTility.HTTPMethod m = RESTility.PUT;
        
        if (m_isNew) {
            /*
             If this user has never been saved then we need
             to use a POST instead of a PUT
             */
            m = RESTility.POST;
        }
        
        RESTility.callREST(getURIForID(getUserId()), user.toString(), m, new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    m_isNew = false;
                    callback.success(Boolean.TRUE);
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    callback.error(errorResponse);
                }
                
                @Override
                public void onError(RESTException e)
                {
                    callback.error(e);
                }
	
	    });
    }
    
    /**
     * Delete this user from the server.
     * 
     * @param callback the callback to indicate the success of the delete call
     */
    public void delete(final RESTObjectCallBack<Boolean> callback)
    {
        RESTility.callREST(getURIForID(getUserId()),
			   null, RESTility.DELETE, new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    callback.success(Boolean.TRUE);
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    MessageUtil.showError("Error.  Status Code: " + statusCode + " " + errorResponse);
                }
                
                @Override
                public void onError(RESTException e)
                {
                    MessageUtil.showError(e.getReason());
                }
	
	    });
    }
    
    /**
     * This is a helper function to determine if the specified username is in use.  It makes
     * a GET call to get the specified user information and returns true if the username is
     * already used and false otherwise.
     * 
     * @param callback the callback
     * @param username the username to check
     */
    public static void isUsernameInUse(final RESTObjectCallBack<Boolean> callback, String username)
    {
        
        RESTility.callREST(getURIForID(username),
			   new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    callback.success(Boolean.TRUE);
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    callback.error(errorResponse);
                }
                
                @Override
                public void onError(RESTException e)
                {
                    if (e.getCode().equals("Not Found")) {
                        /*
                         Then our username doesn't exist on the server which means the
                         name isn't in use.
                         */
                        callback.success(Boolean.FALSE);
                    } else {
                        callback.error(e);
                    }
                }
	
	    });
    }

    private static String getURIForID(String userid)
    {
	return "api/users/" + URL.encodeComponent(userid);
    }
	
}
