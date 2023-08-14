# blog_pass
BlogPass is a blog themed API that process new Posts data from an external resource and save the Progress Status for the UOL Compass Challenge 3.


## ✅ What BlogPass do?
This project is a RESTFUL API that handle Posts data doing the operations with HTTP verbs (GET, POST, PUT, DELETE) by consuming data with OpenFeign. Also, it apply a message brocker to receive many requests and process it accordly. While the Posts are being handle, the Progress Status is updated in history.

As an extra, it has the function to register new users that can consume the content of BlogPass.


### 🛠️ Technologies
The following technologies were used in this project:

* [Java 17](https://www.oracle.com/br/java/)
* [Spring Security](https://spring.io/projects/spring-security)
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
  
Next, you can clone this repository with the following command:

```bash
git clone https://github.com/mandis-ncs/blog_pass.git
```

Now, you can open the directory in your IDE and navigate until the main class called 'BlogPassApplication' and press the icon 'Run' to run the code!
Next, you can open your testing API tools software  and try the HTTP operations.

## ✅ Testing HTTP: Step by step ##
This are the commands you can try in your testing software. We recommend you use Postman and follow the steps in order.

(Obs: when doing the requests you have to login in Basic Auth as admin or as an user.)

### ✅ Handle Posts Data ###
* BASIC AUTH
First, when you open your testing software go to the Authorization tab and select the Type "Basic Auth", then put the admin credentials pre registered: 
```bash
Username: admin
Password: admin123
```
Now you can proceed the HTTP operations.

* ✅ POST
To process a new Post, you have to put an ID between 1 and 100 and replace where the {postId} symbol is:
```bash
http://localhost:8080/posts/{postId}
```
As example:
```bash
http://localhost:8080/posts/1
```
This will set the Progress Status as ENABLE if succed or FAILED if something goes wrong.

* ✅ GET ALL
With this command, you can see all the Posts registred, the comments associated, and their Progress Status History.
```bash
http://localhost:8080/posts
```
(Note: You can try login with user credentials. See more below.)


* ✅ PUT
With this command, the Post will be reprocessed.
```bash
http://localhost:8080/posts/{postId}
```

* ✅ DELETE
To delete an Post, you can use the same command as before. The difference, is that the Post Status will be setted as DISABLE.
```bash
http://localhost:8080/posts/{postId}
```
### 😃 Register your own User ###
As said, you can register and login with your own credentials. Therefore, the application gives you a user by default:
```bash
Username: user
Password: user123
```

* 😃 REGISTER USER
Now, to register a new User, you can go to the testing software and choose a POST method with the URL:
```bash
http://localhost:8080/blog-pass/auth/register
```

You have to provide a JSON body like the following format:
```bash
{
    "username" :  "newUser",
    "password" : "newUser123"
}       
```
Remenber that the User credentials can only access the GET ALL method to see the registered Posts. 
(Obs: As is used a embedded database with drop-create configuration, the newUser created will be erased when the application stop to running.)

* 😃 LOGIN
You can go to the Authorization tab again and put your new credentials. But before this, you can test with your credentials are valid, by provinding the JSON body as above in the URL:
```bash
http://localhost:8080/blog-pass/auth/login
```

### ❌ Exceptions
If you send a bad request or search by an ID that doesn't exist, we provide some advice to prevent errors. For example, if you try the following POST URI, the error message "The post id should be between 0 and 100." will appear.

```bash
http://localhost:8080/posts/10123
```

### 💡 New Knowledges ###
It was very interesting applying some new technologies I was not addapted to as:
*  ActiveMQ Artemis embedded message brocker, so the queues to receive requests will be created automaticaly
*  OpenFeign Client to consume data from an external resource
*  Using ENUMS to save the Progress Status while processing a Post, that with the register of time possibility compare the velocity and capacity of my API
*  Putting some basic security with Spring security

### 🧪 Tests
It was applyed some simple tests with [JUnit](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/) to test if Exceptions were throwed in error cases in the class 'Service'.
* To run the Service class tests in your repository, you can go to the 'PostServiceImplTest' and press the button to run.

(Note: the test is placed in "src/test")

### 📄 Documentation
The documentation was made automatically using Swagger.
To konw more about BlogPass API, you can access it by copying the following URL in your browser:
```bash
[localh](http://localhost:8080/swagger-ui.html)
```

(Note: You can only access it when the project is running)

### ‎😃 Creators
(insert here the nickname of each creator)
