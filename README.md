# blog_pass
BlogPass is an API designed around a blog theme, which processes new post data from an external resource and tracks the Progress Status for the UOL Compass Challenge 3.


## ✅ What BlogPass do?
BlogPass is a RESTful API that handles post data operations using HTTP verbs (GET, POST, PUT, DELETE) by consuming data with OpenFeign. Additionally, it employs a message broker to handle multiple requests and processes them accordingly. While managing the posts, the Progress Status is updated and recorded in the history. 

As an added feature, it allows the registration of new users who can access the content provided by BlogPass.


### 🛠️ Technologies
The following technologies were utilized in this project:

* [Java 17](https://www.oracle.com/br/java/)
* [Spring Security](https://spring.io/projects/spring-security)
* [Mockito](https://site.mockito.org/)
* [H2 Database](https://www.h2database.com/html/main.html)
* [ActiveMQ Artemis](https://activemq.apache.org/components/artemis/)
* [Git](https://git-scm.com/)
  
External data resource from:
* [{JSON} Placeholder](https://jsonplaceholder.typicode.com/)

### 🚀 Starting
To test this project in your computer, you should have the following tools installed:
* Git Bash, to clone this project
* An IDE (like IntelliJ or Eclipse) to run the Spring Java code
* An software testing tools (like Postman or Insomnia)
  
Follow these steps:

1. Clone this repository in Git Bash using the following command:
```bash
git clone https://github.com/mandis-ncs/blog_pass.git
```

2. Open the cloned directory in your IDE and locate the main class called 'BlogPassApplication'. Click the 'Run' icon to execute the code.
3. Open your testing API software and try out the HTTP operations.

### ⚠️⚠️⚠️ Atention: ⚠️⚠️⚠️
The clone will redirect you to the main branch, where basic security is enabled. If you want to test the version ⚠️WITHOUT SECURITY:
1. In Git Bash copy and paste to switch to another branch:
```bash
git checkout -b without_security origin/without_security
```
2. Verify if the message "Switch to new branch [without_security](https://site.mockito.org/)" appears. You are done :)

## ✅ Testing HTTP: Step by step ##
Here are the commands to use in your testing software (preferably Postman). Please follow these steps in order:

(Obs: For each request, log in using Basic Auth as either an admin or an user, unless you have changed to whithout_security branch.)

### ✅ Handle Posts Data ###
* BASIC AUTH ->
In your testing software, go to the Authorization tab and select the "Basic Auth" type. Use the pre-registered admin credentials:
```bash
Username: admin
Password: admin123
```
(Note: You have to put the credentials in every Endpoint, to identify yourself)

* ✅ POST -> 
To process a new post, use an ID between 1 and 100 in place of {postId}:
```bash
http://localhost:8080/posts/{postId}
```
As example:
```bash
http://localhost:8080/posts/1
```
This will set the Progress Status to ENABLE if successful or FAILED if there's an issue.

* ✅ GET ALL -> 
Retrieve all registered posts, associated comments, and their Progress Status History using:
```bash
http://localhost:8080/posts
```
(Note: You can also log in with user credentials. More below.)


* ✅ PUT -> 
Reprocess a post using:
```bash
http://localhost:8080/posts/{postId}
```

* ✅ DELETE ->
Delete a post using the same command as before. The post's status will be set to DISABLE.
```bash
http://localhost:8080/posts/{postId}
```
### 😃 Register Your Own User ###
As mentioned, you can register and log in with your own credentials. By default, the application provides the following user:
```bash
Username: user
Password: user123
```

* 😃 REGISTER USER -> 
To register a new user, send a POST request to:
```bash
http://localhost:8080/blog-pass/auth/register
```

You have to provide a JSON body in the following format:
```bash
{
    "username" :  "newUser",
    "password" : "newUser123"
}       
```
Note that user credentials can only access the GET ALL method to view registered posts.

(Obs: As is used a embedded database with drop-create configuration, the newUser created will be erased when the application stop to running.)

* 😃 LOGIN -> 
In the Authorization tab, use your new credentials. You can also test whether your credentials are valid by sending a POST request with the JSON body mentioned above to:
```bash
http://localhost:8080/blog-pass/auth/login
```

### ❌ Exceptions
If you send a bad request or search by an ID that doesn't exist, it is provided some advice to prevent errors. For example, if you try the following POST URI, the error message "The post id should be between 0 and 100." will appear.

```bash
http://localhost:8080/posts/10123
```

### 💡 New Knowledge Gained ###
It was fascinating to apply new technologies that I wasn't previously accustomed to, such as:

* Using ActiveMQ Artemis embedded message broker to create queues automatically for receiving requests
* Utilizing OpenFeign Client to consume data from an external resource
* Employing ENUMS to save the Progress Status during post processing, allowing for time-based comparisons of my API's velocity and capacity
* Implementing basic security measures with Spring Security

### 🧪 Tests
Simple tests using [JUnit](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/) were performed to ensure that exceptions are thrown in error cases in the 'Service' class.
* To run these tests, navigate to the 'PostServiceImplTest' class in your repository and click the run button.

(Note: the test is placed in "src/test")

### 📄 Documentation
The documentation was made automatically using Swagger.
To konw more about BlogPass API, you can access it by copying the following URL in your browser:
```bash
[localh](http://localhost:8080/swagger-ui.html)
```

(Note: You can only access it when the project is running)
