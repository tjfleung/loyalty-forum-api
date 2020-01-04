# Loyalty Forum API

Backend webservice for custom forum application

# Setup

This backend app is developed using Spring Boot.
Maven and at least Java 1.8 are required.

## Building the app

Run: 

```mvn clean package```

## Running the app

After building the app, navigate to the *target* directory and run:

```java -jar forum-api-0.0.1-SNAPSHOT.jar```

Alternatively, use the provided run.sh script:

(Note: may need to add execute privileges to the script: ```chmod +x run.sh```)

```./run.sh```

# Challenges

## Step 1: Hello world wide web
Create a ‘hello world’ web page that runs on a local application server.
Helpful tutorials are available here: http://www.w3schools.com/

## Step 2: Build the back-end
Create a REST web service with a method that returns the text that is passed to it.
Create a few unit tests to verify your application. You may use any unit test framework.

## Step 3: Connect your front and back ends
Create a web form that has a text box, and a ‘Done’ button. 
When ‘Done’ is clicked make AJAX call, passing the entered text to the web service you created in the previous step. 
Display the response from the web service call below the text box on the form.
Extend your tests to verify new components.

## Step 4: Bring in the database
Extend your web service to add a method to store text passed from the form into the database using design patterns where appropriate.
In addition to saving the text, store the date and time when the data was saved.

## Step 5: Include the user info
Extend the form to capture the user name and a list of all the submissions by user. 
Add another method to the service to return all text submissions and display them at the bottom of the page.

## Step 6: Replying to text
Extend your solution from previous challenge to allow “responding” to already posted texts and display responses, indented, 
below the related text entry.
As always, extend your tests.

## Step 7: Capture more data
Capture user’s location (user enters the city) on the form. 
For every post, include city, city’s latitude and longitude and current temperature. 
Display this information on the form, next to each post.

## Step 8: Secure your application with https
This should be easy!
