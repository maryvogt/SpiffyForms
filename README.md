[Spiffy UI](http://www.spiffyui.org) - GWT made simple
==================================================

This sample shows you how to create, read, update, and delete with Spiffy UI forms.  It includes a simple REST server running on Java with [Jersey](http://jersey.java.net/).

Everything starts with a [REST bean](http://www.spiffyui.org/!#rest) named `User.java`.  This bean handles the [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations of our application.  The UI is handled in `Index.java`.

This application shows you how to make the full range of REST requests and build complex form layouts with Spiffy UI.  All data is stored in memory on the server so there's no additional setup.  Just build it and run it and you'll get an application that looks like this:

<img src="http://github.com/maryvogt/SpiffyForms/raw/master/screenshot.png" />

Building and Running SpiffyForms
--------------------------------------

This project is built with [Apache Maven](http://maven.apache.org/).  
    
Go to your project's root directory and run the following command:

    mvn package jetty:run
        
This will download the required libraries, build your project, and run it.  You can access the running application here:

    http://localhost:8080
    

Debugging through Eclipse
--------------------------------------

See [Spiffy UI's GWT Dev Mode page](http://www.spiffyui.org/#!hostedMode) for more information.

The REST API
--------------------------------------

This application exposes a simple REST API for editing users:

*GET /api/users* - Get a list of users including user details.  

    [
        {
            "birthday": "-11995200000",
            "lastName": "Addams",
            "userID": "aadams",
            "desc": "Alice Addams is just some girl",
            "email": "aadams@amazon.com",
            "gender": "female",
            "firstName": "Alice",
            "password": "aaa"
        },
        {
            "birthday": "269582400000",
            "lastName": "Brown",
            "userID": "bbrown",
            "desc": "Bob Brown is just some guy",
            "email": "bbrown@bn.com",
            "gender": "male",
            "firstName": "Bob",
            "password": "b0bpass"
        }
    ]
    
*GET /api/users/<user id> - Get the data about the specified user.

    {
        "birthday": "-11995200000",
        "lastName": "Addams",
        "userID": "aadams",
        "desc": "Alice Addams is just some girl",
        "email": "aadams@amazon.com",
        "gender": "female",
        "firstName": "Alice",
        "password": "aaa"
    }
    
Returns a 404 if the specified user ID doesn't exist

*POST /api/users/<user id> - Add the specified user.

Input:

    {
        "birthday": "-11995200000",
        "lastName": "Addams",
        "userID": "aadams",
        "desc": "Alice Addams is just some girl",
        "email": "aadams@amazon.com",
        "gender": "female",
        "firstName": "Alice",
        "password": "aaa"
    }
    
Output:

    {"success": true}
    
*PUT /api/users/<user id> - Update information about the specified user.

Input:

    {
        "birthday": "-11995200000",
        "lastName": "Addams",
        "userID": "aadams",
        "desc": "Alice Addams is just some girl",
        "email": "aadams@amazon.com",
        "gender": "female",
        "firstName": "Alice",
        "password": "aaa"
    }
    
Output:

    {"success": true}
    
*DELETE /api/users/<user id> - Delete the specified user from the system

Output:

    {"success": true}
    
Returns a 404 if the specified user ID doesn't exist


License
--------------------------------------

Spiffy UI is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
