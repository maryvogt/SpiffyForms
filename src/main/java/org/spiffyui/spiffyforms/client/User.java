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
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

class User
{
    private String m_firstName;
    private String m_lastName;
    private String m_email;
    private String m_password;
    private Date m_bDay;
    private String m_userDesc;
    private String m_securityQuestion;
    private String m_securityAnswer;
    private String m_userId;
    
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

    public void setDate(Date d)
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
    
    /*
     * Send a request to get a list of users 
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
                        User u = new User();
                        
                        u.setFirstName(JSONUtil.getStringValue(usersArray.get(i).isObject(), "firstName"));
                        u.setLastName(JSONUtil.getStringValue(usersArray.get(i).isObject(), "lastName"));
                        u.setPassword(JSONUtil.getStringValue(usersArray.get(i).isObject(), "password"));
                        u.setUserId(JSONUtil.getStringValue(usersArray.get(i).isObject(), "userID"));
                        u.setEmail(JSONUtil.getStringValue(usersArray.get(i).isObject(), "email"));
                        u.m_isNew = false;
                        
                        users.add(u);
                    }
                    
                    callback.success(users.toArray(new User[users.size()]));
                    
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
    
    public void save(final RESTObjectCallBack<Boolean> callback)
    {
        
        JSONObject user = new JSONObject();
        
        user.put("firstName", new JSONString(getFirstName()));
        user.put("lastName", new JSONString(getLastName()));
        user.put("email", new JSONString(getEmail()));
        user.put("userID", new JSONString(getUserId()));
        user.put("password", new JSONString(getPassword()));
        
        RESTility.HTTPMethod m = RESTility.PUT;
        
        if (m_isNew) {
            /*
             If this user has never been saved then we need
             to use a POST instead of a PUT
             */
            m = RESTility.POST;
        }
        
        RESTility.callREST("api/users", user.toString(), m, new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    m_isNew = false;
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
    
    public void delete(final RESTObjectCallBack<Boolean> callback)
    {
        
        RESTility.callREST("api/users", null, RESTility.DELETE, new RESTCallback() {
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

}
