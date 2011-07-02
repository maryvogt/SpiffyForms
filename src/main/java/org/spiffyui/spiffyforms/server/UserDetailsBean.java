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
import java.text.ParseException;

import java.util.Date;


class UserDetailsBean{
    private String m_userID;
    private String m_firstName;
    private String m_lastName;
    private String m_email;
    private String m_password;
    private Date m_bDay;
    private String m_userDesc;


    public UserDetailsBean() {
    }

    public UserDetailsBean(String userID, String first, String last, String email, String password, String date){

        m_userID = userID;
        m_firstName = first;
        m_lastName = last;
        m_email = email;
        m_password = password;
        try{
            m_bDay = DateFormat.getDateInstance().parse(date);
        }
        catch (ParseException e)
        {
            // ignore it for now
            m_bDay = null;
        }
        

    }

    public String getUserID(){
        return m_userID;
    }

    public void setUserID(String s){
        m_userID = s;
    }

    public String getFirstName(){
        return m_firstName;
    }

    public void setFirstName(String s){
        m_firstName = s;
    }

    public String getLastName(){
        return m_lastName;
    }

    public void setLastName(String s){
        m_lastName = s;
    }

    public String getEmail(){
        return m_email;
    }

    public void setEmail(String s){
        m_email = s;
    }

    public String getPassword(){
        return m_password;
    }

    public void setPassword(String s){
        m_password = s;
    }

    public Date getBirthday()
    {
        return m_bDay;
    }

    public void setBirthday(Date d)
    {   
        m_bDay = d;
    }

    


}

