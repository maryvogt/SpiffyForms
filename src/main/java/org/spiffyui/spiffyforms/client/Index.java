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
import java.util.List;

import org.spiffyui.client.widgets.DatePickerTextBox;
import org.spiffyui.client.widgets.button.FancyButton;
import org.spiffyui.client.widgets.button.FancySaveButton;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MainFooter;
import org.spiffyui.client.MainHeader;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTility;
import org.spiffyui.client.widgets.LongMessage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Button;
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
    private TextBox m_text = new TextBox();
    private LongMessage m_longMessage = new LongMessage("longMsgPanel");   

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

    private TextArea m_securityQuestion;
    private FormFeedback m_securityQuestionFeedback;

    private TextBox m_securityAnswer;
    private FormFeedback m_securityAnswerFeedback;

    private FancyButton m_save;

    private List<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();

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
        header.setHeaderTitle("Hello Spiffy SpiffyForms!");
        
        /*
         The main footer shows our message at the bottom of the page.
         */
        MainFooter footer = new MainFooter();
        footer.setFooterString("SpiffyForms was built with the <a href=\"http://www.spiffyui.org\">Spiffy UI Framework</a>");
        
        /*
         This HTMLPanel holds most of our content.
         MainPanel_html was built in the HTMLProps task from MainPanel.html, which allows you to use large passages of html
         without having to string escape them.
         */
        HTMLPanel panel = new HTMLPanel(STRINGS.MainPanel_html())
        {
            @Override
            public void onLoad()
            {
                super.onLoad();
                /*
                 Let's set focus into the text field when the page first loads
                 */
                m_text.setFocus(true);
            }
        };
        
        RootPanel.get("mainContent").add(panel);
        
        /*
         These dynamic controls add interactivity to our page.
         */
        panel.add(m_longMessage, "longMsg");
        panel.add(m_text, "nameField");
        final Button button = new Button("Submit");
        panel.add(button, "submitButton");
        
        button.addClickHandler(this);
        m_text.addKeyPressHandler(this);

        
        /*
         First name
         */
        m_firstName = new TextBox();
        m_firstName.addKeyUpHandler(this);
        m_firstName.getElement().setId("firstNameTxt");
        m_firstName.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_firstName, "firstName");

        m_firstNameFeedback = new FormFeedback();
        m_feedbacks.add(m_firstNameFeedback);
        panel.add(m_firstNameFeedback, "firstNameRow");

        /*
         Last name
         */
        m_lastName = new TextBox();
        m_lastName.addKeyUpHandler(this);
        m_lastName.getElement().setId("lastNameTxt");
        m_lastName.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_lastName, "lastName");

        m_lastNameFeedback = new FormFeedback();
        m_feedbacks.add(m_lastNameFeedback);
        panel.add(m_lastNameFeedback, "lastNameRow");

        /*
         email
         */
        m_email = new TextBox();
        m_email.addKeyUpHandler(this);
        m_email.getElement().setId("emailTxt");
        m_email.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_email, "email");

        m_emailFeedback = new FormFeedback();
        m_feedbacks.add(m_emailFeedback);
        panel.add(m_emailFeedback, "emailRow");

        /*
         User's birthdate
         */
        m_bDay = new DatePickerTextBox("userBdayTxt");
        m_bDay.setMaximumDate(new Date()); //user cannot be born tomorrow
        m_bDay.addKeyUpHandler(this);
        m_bDay.getElement().addClassName("slimTextField");
        panel.add(m_bDay, "userBday");

        m_bDayFeedback = new FormFeedback();
        panel.add(m_bDayFeedback, "userBdayRow");

        /*
         User's gender
         */
        RadioButton female = new RadioButton("userGender", "Female");
        panel.add(female, "userGender");

        RadioButton male = new RadioButton("userGender", "Male");
        male.addStyleName("radioOption");
        male.setValue(true);
        male.getElement().setId("userMale");
        panel.add(male, "userGender");

        /*
         User description
         */
        m_userDesc = new TextArea();
        m_userDesc.addKeyUpHandler(this);
        m_userDesc.getElement().setId("userDescTxt");
        m_userDesc.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_userDesc, "userDesc");

        m_userDescFeedback = new FormFeedback();
        m_feedbacks.add(m_userDescFeedback);
        panel.add(m_userDescFeedback, "userDescRow");

        /*
         Password
         */
        m_password = new PasswordTextBox();
        m_password.addKeyUpHandler(this);
        m_password.getElement().setId("passwordTxt");
        m_password.getElement().addClassName("slimTextField");
        panel.add(m_password, "password");

        m_passwordFeedback = new FormFeedback();
        m_feedbacks.add(m_passwordFeedback);
        panel.add(m_passwordFeedback, "passwordRow");

        /*
         Password repeat
         */
        m_passwordRepeat = new PasswordTextBox();
        m_passwordRepeat.addKeyUpHandler(this);
        m_passwordRepeat.getElement().setId("passwordRepeatTxt");
        m_passwordRepeat.getElement().addClassName("slimTextField");
        panel.add(m_passwordRepeat, "passwordRepeat");

        m_passwordRepeatFeedback = new FormFeedback();
        m_feedbacks.add(m_passwordRepeatFeedback);
        panel.add(m_passwordRepeatFeedback, "passwordRepeatRow");

        /*
         Security Question
         */
        m_securityQuestion = new TextArea();
        m_securityQuestion.addKeyUpHandler(this);
        m_securityQuestion.getElement().setId("securityQuestionTxt");
        m_securityQuestion.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_securityQuestion, "securityQuestion");

        m_securityQuestionFeedback = new FormFeedback();
        m_feedbacks.add(m_securityQuestionFeedback);
        panel.add(m_securityQuestionFeedback, "securityQuestionRow");

        /*
         Security answer
         */
        m_securityAnswer = new TextBox();
        m_securityAnswer.addKeyUpHandler(this);
        m_securityAnswer.getElement().setId("securityAnswerTxt");
        m_securityAnswer.getElement().addClassName(WIDE_TEXT_FIELD);
        panel.add(m_securityAnswer, "securityAnswer");

        m_securityAnswerFeedback = new FormFeedback();
        m_feedbacks.add(m_securityAnswerFeedback);
        panel.add(m_securityAnswerFeedback, "securityAnswerRow");



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

        panel.add(m_save, "page2Buttons");
        //updateFormStatus(null);

        
    }
    
    @Override
    public void onClick(ClickEvent event)
    {
        sendRequest();
    }

    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        /*
         We want to submit the request if the user pressed enter
         */
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            sendRequest();
        }
    }
    
    /**
     * Send the REST request to the server and read the response back.
     */
    private void sendRequest()
    {
        String q = m_text.getValue().trim();
        if (q.equals("")) {
            MessageUtil.showWarning("Please enter your name in the text field.", false);
            return;
        }
        RESTility.callREST("simple/" + q, new RESTCallback() {
            
            @Override
            public void onSuccess(JSONValue val)
            {
                showSuccessMessage(val);
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
     * Show the successful message result of our REST call.
     * 
     * @param val    the value containing the JSON response from the server
     */
    private void showSuccessMessage(JSONValue val)
    {
        JSONObject obj = val.isObject();
        String name = JSONUtil.getStringValue(obj, "user");
        String browser = JSONUtil.getStringValue(obj, "userAgent");
        String server = JSONUtil.getStringValue(obj, "serverInfo");
        
        String message = "Hello, " + name + "!  <br/>You are using " + browser + ".<br/>The server is " + server + ".";
        m_longMessage.setHTML(message);
    }

    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            // updateFormStatus((Widget) event.getSource());
        }
    }

}

