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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spiffyui.client.MainFooter;
import org.spiffyui.client.MainHeader;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.widgets.DatePickerTextBox;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.FancyButton;
import org.spiffyui.client.widgets.button.FancySaveButton;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class is the main entry point for our GWT module.
 */
public class Index implements EntryPoint, ClickHandler, KeyPressHandler, KeyUpHandler 
{

    private static final String WIDE_TEXT_FIELD = "wideTextField";
    private static final SpiffyUiHtml STRINGS = (SpiffyUiHtml) GWT.create(SpiffyUiHtml.class);

    private static Index g_index;
    private HTMLPanel m_panel;

    private TextBox m_firstName;
    private FormFeedback m_firstNameFeedback;

    private TextBox m_lastName;
    private FormFeedback m_lastNameFeedback;

    private TextBox m_email;
    private FormFeedback m_emailFeedback;

    private TextBox m_password;
    private FormFeedback m_passwordFeedback;

    private TextBox m_passwordRepeat;
    private FormFeedback m_passwordRepeatFeedback;

    private DatePickerTextBox m_bDay;
    private FormFeedback m_bDayFeedback;

    private TextArea m_userDesc;
    private FormFeedback m_userDescFeedback;

    private FancyButton m_save;

    private List<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();
    
    private Map<String, Anchor> m_anchors = new HashMap<String, Anchor>();

    /**
     * The Index page constructor
     */
    public Index()
    {
        g_index = this;
    }


    @Override
    public void onModuleLoad()
    {
        /*
         This is where we load our module and create our dynamic controls.  The MainHeader
         displays our title bar at the top of our page.
         */
        MainHeader header = new MainHeader();
        header.setHeaderTitle("SpiffyForms Sample App");
        
        /*
         The main footer shows our message at the bottom of the page.
         */
        MainFooter footer = new MainFooter();
        footer.setFooterString("SpiffyForms was built with the <a href=\"http://www.spiffyui.org\">Spiffy UI Framework</a>");

        getUsers();
        
        buildFormUI();

        
    }
    
    private void buildFormUI()
    {
        /*
         This HTMLPanel holds most of our content.
         MainPanel_html was built in the HTMLProps task from MainPanel.html, which allows you to use large passages of html
         without having to string escape them.
         */
        m_panel = new HTMLPanel(STRINGS.MainPanel_html());
        RootPanel.get("mainContent").add(m_panel);
        
        /*
         First name
         */
        m_firstName = new TextBox();
        m_firstName.addKeyUpHandler(this);
        m_firstName.getElement().setId("firstNameTxt");
        m_firstName.getElement().addClassName(WIDE_TEXT_FIELD);
        m_panel.add(m_firstName, "firstName");

        m_firstNameFeedback = new FormFeedback();
        m_feedbacks.add(m_firstNameFeedback);
        m_panel.add(m_firstNameFeedback, "firstNameRow");

        /*
         Last name
         */
        m_lastName = new TextBox();
        m_lastName.addKeyUpHandler(this);
        m_lastName.getElement().setId("lastNameTxt");
        m_lastName.getElement().addClassName(WIDE_TEXT_FIELD);
        m_panel.add(m_lastName, "lastName");

        m_lastNameFeedback = new FormFeedback();
        m_feedbacks.add(m_lastNameFeedback);
        m_panel.add(m_lastNameFeedback, "lastNameRow");

        /*
         email
         */
        m_email = new TextBox();
        m_email.addKeyUpHandler(this);
        m_email.getElement().setId("emailTxt");
        m_email.getElement().addClassName(WIDE_TEXT_FIELD);
        m_panel.add(m_email, "email");

        m_emailFeedback = new FormFeedback();
        m_feedbacks.add(m_emailFeedback);
        m_panel.add(m_emailFeedback, "emailRow");

        /*
         User's birthdate
         */
        m_bDay = new DatePickerTextBox("userBdayTxt");
        m_bDay.setMaximumDate(new Date()); //user cannot be born tomorrow
        m_bDay.addKeyUpHandler(this);
        m_bDay.getElement().addClassName("slimTextField");
        m_panel.add(m_bDay, "userBday");

        m_bDayFeedback = new FormFeedback();
        m_panel.add(m_bDayFeedback, "userBdayRow");

        /*
         User's gender
         */
        RadioButton female = new RadioButton("userGender", "Female");
        m_panel.add(female, "userGender");

        RadioButton male = new RadioButton("userGender", "Male");
        male.addStyleName("radioOption");
        male.setValue(true);
        male.getElement().setId("userMale");
        m_panel.add(male, "userGender");

        /*
         User description
         */
        m_userDesc = new TextArea();
        m_userDesc.addKeyUpHandler(this);
        m_userDesc.getElement().setId("userDescTxt");
        m_userDesc.getElement().addClassName(WIDE_TEXT_FIELD);
        m_panel.add(m_userDesc, "userDesc");

        m_userDescFeedback = new FormFeedback();
        m_feedbacks.add(m_userDescFeedback);
        m_panel.add(m_userDescFeedback, "userDescRow");

        /*
         Password
         */
        m_password = new PasswordTextBox();
        m_password.addKeyUpHandler(this);
        m_password.getElement().setId("passwordTxt");
        m_password.getElement().addClassName("slimTextField");
        m_panel.add(m_password, "password");

        m_passwordFeedback = new FormFeedback();
        m_feedbacks.add(m_passwordFeedback);
        m_panel.add(m_passwordFeedback, "passwordRow");

        /*
         Password repeat
         */
        m_passwordRepeat = new PasswordTextBox();
        m_passwordRepeat.addKeyUpHandler(this);
        m_passwordRepeat.getElement().setId("passwordRepeatTxt");
        m_passwordRepeat.getElement().addClassName("slimTextField");
        m_panel.add(m_passwordRepeat, "passwordRepeat");

        m_passwordRepeatFeedback = new FormFeedback();
        m_feedbacks.add(m_passwordRepeatFeedback);
        m_panel.add(m_passwordRepeatFeedback, "passwordRepeatRow");



        /*
         The big save button
         */
        m_save = new FancySaveButton("Save");
        m_save.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                   // save();
                }
            });

        m_panel.add(m_save, "buttons");
        //updateFormStatus(null);
    }
    
    private void getUsers()
    {
        User.getUsers(new RESTObjectCallBack<User[]>() {
                public void success(User[] users)
                {
                    showUsers(users);
                }
    
                public void error(String message)
                {
                    MessageUtil.showFatalError(message);
                }
    
                public void error(RESTException e)
                {
                    MessageUtil.showFatalError(e.getReason());
                }
            });
    }
    
    private void showUsers(User users[])
    {
        StringBuffer userHTML = new StringBuffer();
        
        userHTML.append("<div class=\"gridlist\">");
        
        for (int i = 0; i < users.length; i++) {
            User u = users[i];
            if (i % 2 == 0) {
                userHTML.append("<div class=\"gridlistitem evenrow\">");
            } else {
                userHTML.append("<div class=\"gridlistitem oddrow\">");
            }
            
            String id = HTMLPanel.createUniqueId();
            
            /*
             The user id
             */
            userHTML.append("<div id=\"" + id + "\" class=\"useridcol\"></div>");
            
            /*
             The user's name
             */
            userHTML.append("<div class=\"userfullnamecol\">" + u.getFirstName() + " " + u.getLastName() + "</div>");
            
            /*
             The email address
             */
            userHTML.append("<div class=\"useremailcol\">" + u.getEmail() + "</div>");
            
            userHTML.append("</div>");
            
            Anchor a = new Anchor(u.getUserId(), "#");
            a.getElement().setPropertyObject("user", u);
            a.addClickHandler(this);
            m_anchors.put(id, a);
        }
        
        m_panel.getElementById("userListGrid").setInnerHTML(userHTML.toString());
        
        /*
         Now that we've added the elements to the DOM we can add the
         anchors
         */
        for (String id : m_anchors.keySet()) {
            m_panel.add(m_anchors.get(id), id);
        }
    }
    
    @Override
    public void onClick(ClickEvent event)
    {
        event.preventDefault();
        
        if (event.getSource() instanceof Anchor) {
            showUser((User) ((Anchor) event.getSource()).getElement().getPropertyObject("user"));
        }
    }
    
    private void showUser(User user)
    {
        m_firstName.setText(user.getFirstName());
    }

    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        
    }

    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            // updateFormStatus((Widget) event.getSource());
        }
    }

}

