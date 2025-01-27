# cs122b-spring20-team-69
cs122b-spring20-team-69 created by GitHub Classroom

## LATE-GRADING DEMO (Projects 3 and 5)
Link: https://drive.google.com/file/d/17XvjlG4kg6QrApd71nN1rioyqUVTRfTA/view?usp=sharing
- 0:00 - Project 3
- 16:55 - Project 5
  * No HTTPS for master/slave

## PROJECT 1
<details><summary>click for more</summary>
  
## VIDEO
Link: https://youtu.be/j0mx1oNTue0

## DEPLOYMENT
1) Clone this Git repo
2) Create database and its tables
3) Import data into database
4) Change to correct MySQL credentials in application.properties (in cs122b-spring20-team-69/fabflix-backend/src/main/resources)
5) Build .war in fabflix-backend/ directory 
<code>mvn package</code>
6) Copy .war file to ~/tomcat/webapps
<code>cp ./target/*.war /home/ubuntu/tomcat/webapps</code>
7) Navigate to fabflix-frontend/ directory
8) Run <code>npm install</code>
9) Run <code>ng build --base-href=. --prod</code>
10) Go to dist/ directory in the same directory
11) Rename directory if desired; default is fabflix-frontend
12) Copy directory to ~/tomcat/webapps
<code>cp <name_of_directory> /home/ubuntu/tomcat/webapps</code>
13) Navigate to ~/tomcat/webapps
14) Rename fabflix-0.0.1-SNAPSHOT.war to fabflix-backend.war
15) In browser, navigate to Tomcat manager page at ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/manager/html
16) Click on "/fabflix" under "Paths"

## CONTRIBUTION
- John: Frontend/Angular coding and setup 
- Alexis: Backend/Spring Boot JDBC coding and setup
</details>

## PROJECT 2
<details><summary>click for more</summary>

## VIDEO
Link: https://youtu.be/IjvZFraVNKs
- Demo was done on 4/28/20, but sales entries were 4/29/20, due to time of server instance
- At the last few minutes, had to use MySQL CLI to correctly query for the sales records

## DEPLOYMENT
1) Prepare database and tables
2) Clone this Git Repo
3) Change to correct MySQL credentials in cs122b-spring20-team-69/fabflix-backend/src/main/resources/application.properties
4) Build .war in fabflix-backend/ directory 
<code>mvn package</code>
5) Copy .war file to ~/tomcat/webapps
<code>cp ./target/*.war /home/ubuntu/tomcat/webapps</code>
6) Navigate to fabflix-frontend/ directory
7) Run <code>npm install</code>
8) Run <code>ng build --base-href=. --prod</code>
9) Go to dist/ directory in the same directory
10) Rename directory if desired; default is fabflix-frontend
11) Copy directory to ~/tomcat/webapps
<code>cp <name_of_directory> /home/ubuntu/tomcat/webapps</code>
12) Navigate to ~/tomcat/webapps
13) Rename fabflix-0.0.1-SNAPSHOT.war to fabflix-backend.war
14) In browser, navigate to Tomcat manager page at ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/manager/html
15) Click on "/fabflix" under "Paths"

## SUBSTRING MATCH DESIGN
We implemented the substring matching using the ".... LIKE %substring%" method.

## CONTRIBUTIONS
- John: Browse By Genre and Title, Movie List functionalities, Cart
- Alexis: HTML for most, CSS for all; Login, Checkout, Post Payment
</details>

## PROJECT 3
<details><summary>click for more</summary>
  
## VIDEO
Note: The parser successfully finished, but afterwards, near the end of the demo our database connection was lost and our site wouldn't load results where it previously did as seen in the video. We apologize for this and are uploading whatever is relevant.

Link: https://drive.google.com/open?id=1g8g-FTEfmuYspeVtGtnn8RDDmDMsq7yi

## DEPLOYMENT
1) Prepare database and tables
2) Clone this Git Repo
3) Copy correct MySQL credentials into cs122b-spring20-team-69/fabflix-backend/src/main/resources
<code> cp ~/application.properties ~/cs122b-spring20-team-69/fabflix-backend/src/main/resources </code>
4) cd into backedn directory
<code>cd ~/cs122b-spring20-team-69/fabflix-backend </code>
5) Build .war in fabflix-backend/ directory 
<code>mvn clean package</code>
5) Copy .war file to ~/tomcat/webapps
<code>cp ./target/*.war /home/ubuntu/tomcat/webapps</code>
6) cd into frontend directory
<code>cd  ~/cs122b-spring20-team-69/fabflix-frontend</code>
7) Install dependencies
Run <code>npm install</code>
8) Build files
Run <code>ng build --base-href=. --prod</code>
9) cd into target file directory
<code>cd dist</code>
10) Rename the directory to fabflix
11) Create WEB-INF directory in fabflix/
<code>mkdir fabflix/WEB-INF</code>
12) Copy web.xml into fabflix/WEB-INF
<code>cp ~/web.xml ./WEB-INF</code>
13) Copy fabflix/ directory into ~/tomcat/webapps
<code>cp fabflix /home/ubuntu/tomcat/webapps</code>
14) cd to ~/tomcat/webapps
<code>cd ~/tomcat/webapps</code>
15) Rename fabflix-0.0.1-SNAPSHOT.war to fabflix-backend.war
<code>mv fabflix-0.0.1-SHANPSHOT.war fabflix-backend.war</code>
16) Wait until fabflix-backend directory exists
17) Copy web.xml into fabflix-backend/WEB-INF
<code>cp ~/web.xml fabflix-backend/WEB-INF</code>
18) In browser, navigate to Tomcat manager page at ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/manager/html
19) Click on "/fabflix" under "Paths"

## PREPARED STATEMENTS
- All prepared statements can be found in the following files:
1. JdbcMovieRepository (https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-backend/src/main/java/com/fabflix/fabflix/repository/JdbcMovieRepository.java)
2. MovieParser (https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-backend/src/main/java/com/fabflix/fabflix/MovieParser.java)
3. StarParser (https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-backend/src/main/java/com/fabflix/fabflix/StarParser.java)
4. CastParser (https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-backend/src/main/java/com/fabflix/fabflix/CastParser.java)

## PARSING OPTIMIZATIONS
1. Validating data as they're found when parsing any XML, we make checks at each stage before passing on the data to the next, until it's created and fully processed to store into a List to later iterate over to add to our database;
2. Gather genres within xml, normalize them using the given key, then using that to generate a subset of genres not already included in table and then adding them to the database in a separate function. This prevents us having to check if the genre found already exists in our genres table for each movie.

## DATA INCONSISTENCIES REPORT
- All XML inconsistencies reported to user through console.

## CONTRIBUTIONS
John: HTTPS, Encryption, Dashboard
Alexis: ReCAPTCHA, Stored Procedure, XML
</details>

## PROJECT 4
<details><summary>click for more</summary>
  
## VIDEO
Link: https://drive.google.com/file/d/1qmxKobkbLzCit7Z36kI5WUzRwQIRisGm/view?usp=sharing

## DEPLOYMENT: Webpage
1) Prepare database and tables
2) Clone this Git Repo
3) Copy correct MySQL credentials into cs122b-spring20-team-69/fabflix-backend/src/main/resources
<code> cp ~/application.properties ~/cs122b-spring20-team-69/fabflix-backend/src/main/resources </code>
4) cd into backend directory
<code>cd ~/cs122b-spring20-team-69/fabflix-backend </code>
5) Build .war in fabflix-backend/ directory 
<code>mvn clean package</code>
5) Copy .war file to ~/tomcat/webapps
<code>cp ./target/*.war /home/ubuntu/tomcat/webapps</code>
6) cd into frontend directory
<code>cd  ~/cs122b-spring20-team-69/fabflix-frontend</code>
7) Install dependencies
Run <code>npm install</code>
8) Build files
Run <code>ng build --base-href=. --prod</code>
9) cd into target file directory
<code>cd dist</code>
10) Rename the directory to fabflix
11) Create WEB-INF directory in fabflix/
<code>mkdir fabflix/WEB-INF</code>
12) Copy web.xml into fabflix/WEB-INF
<code>cp ~/web.xml ./WEB-INF</code>
13) Copy fabflix/ directory into ~/tomcat/webapps
<code>cp fabflix /home/ubuntu/tomcat/webapps</code>
14) cd to ~/tomcat/webapps
<code>cd ~/tomcat/webapps</code>
15) Rename fabflix-0.0.1-SNAPSHOT.war to fabflix-backend.war
<code>mv fabflix-0.0.1-SNAPSHOT.war fabflix-backend.war</code>
16) Wait until fabflix-backend directory exists
17) Copy web.xml into fabflix-backend/WEB-INF
<code>cp ~/web.xml fabflix-backend/WEB-INF</code>
18) In browser, navigate to Tomcat manager page at ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/manager/html
19) Click on "/fabflix" under "Paths"

## DEPLOYMENT: Android App
1) Clone this Git Repo
2) Import into Intellij IDEA
3) Allow Gradle import to finish
4) Edit configuration -> Add Android App configuration -> set module as "app"
5) Navigate to Build > Build Bundle(s) & APKs > Build APKs
6) Once build finishes, locate APK (message notifying location appears at lower right)
7) Drag and drop APK file onto local emulator

## CONTRIBUTIONS
John: full text search, autocomplete
Alexis: android app backend/frontend
</details>

## PROJECT 5
<details> <summary>click for more</summary>

## VIDEO
Link: https://drive.google.com/file/d/12HuAdzQusow_huejls7S3j4UMtxbNYP5/view?usp=sharing
  
## DEPLOYMENT
1) Prepare database and tables
2) Clone this Git Repo
3) Copy correct MySQL credentials into cs122b-spring20-team-69/fabflix-backend/src/main/resources
<code> cp ~/application.properties ~/cs122b-spring20-team-69/fabflix-backend/src/main/resources </code>
4) cd into backend directory
<code>cd ~/cs122b-spring20-team-69/fabflix-backend </code>
5) Build .war in fabflix-backend/ directory 
<code>mvn clean package</code>
5) Copy .war file to ~/tomcat/webapps
<code>cp ./target/*.war /home/ubuntu/tomcat/webapps</code>
6) cd into frontend directory
<code>cd  ~/cs122b-spring20-team-69/fabflix-frontend</code>
7) Install dependencies
Run <code>npm install</code>
8) Build files
Run <code>ng build --base-href=. --prod</code>
9) cd into target file directory
<code>cd dist</code>
10) Rename the directory to fabflix
11) Create WEB-INF directory in fabflix/
<code>mkdir fabflix/WEB-INF</code>
12) Copy web.xml into fabflix/WEB-INF
<code>cp ~/web.xml ./WEB-INF</code>
13) Copy fabflix/ directory into ~/tomcat/webapps
<code>cp fabflix /home/ubuntu/tomcat/webapps</code>
14) cd to ~/tomcat/webapps
<code>cd ~/tomcat/webapps</code>
15) Rename fabflix-0.0.1-SNAPSHOT.war to fabflix-backend.war
<code>mv fabflix-0.0.1-SNAPSHOT.war fabflix-backend.war</code>
16) Wait until fabflix-backend directory exists
17) Copy web.xml into fabflix-backend/WEB-INF
<code>cp ~/web.xml fabflix-backend/WEB-INF</code>
18) In browser, navigate to Tomcat manager page at ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/manager/html
19) Click on "/fabflix" under "Paths"

## CONTRIBUTIONS
John: master/slave and load balancing
Alexis: connection pooling and time log processing

## CONNECTION POOLING
- Path of Connection Pooling Configuration: fabflix-backend/src/main/resources/application.properties

- Explain how Connection Pooling is utilized in the Fabflix code.
	1. spring.datasource.hikari.connection-timeout = 20000 
		Client will wait a maximum of 20,000 ms when requesting a DB connection, if time exceeds a SQL exception will be thrown.
	2. spring.datasource.hikari.minimum-idle= 10 
		A minimum of 10 idle connections will be maintained in the pool. If # of idle connections dips below this and total connections less than max pool size, the pool will add connections accordingly.
	3. spring.datasource.hikari.maximum-pool-size= 10
		Connection pool will have maximum of 10 connections, this includes the # of idle/in-use connections. This is kept the same as the # minimum idle connections. 
	4. spring.datasource.hikari.idle-timeout=10000
		Connection can be idle for a maximum of 10,000 ms, but setting only applies when minimum # of idle connections < maximum pool size.
	5. spring.datasource.hikari.auto-commit =true
		Auto-commit behavior of a returned connection = true.
    
- Explain how Connection Pooling works with two backend SQL.

We are using Spring Boot, which by default uses Hikari Connection Pooling for each instance. Each request (read or write) is directed to the appropriate server, and Spring Boot will handle the Connection Pooling for that server's database.
    

## Master/Slave
- Path of all code/configuration files in GitHub of routing queries to Master/Slave SQL:

https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/server.ts
This file is an enum of the master/slave servers exported to all service files below.

 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/auth.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/browse.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/employee.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/movie.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/search.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/server-cache.service.ts
 * https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-69/blob/master/fabflix-frontend/src/app/services/shopping.service.ts

- How read/write requests were routed to Master/Slave SQL?

In the frontend code of master/slave, the server IP's were hardcoded into each API call; read endpoints used the respective local server's API, while write endpoints used only the master server's API.

In the files linked above, for example, the slave instance would have <code>read = "http://<slave_ip>:8080"</code> and <code>write = "http://<master_ip>:8080" </code>. The master instance would have <code>read = "http://<master_ip>:8080"</code> and <code>write = "http://<master_ip>:8080".
    

## JMeter TS/TJ Time Logs
Instructions of how to use the `log_processing.*` script to process the JMeter logs.
  1. Navigate to "test logs" folder
  2. Run command: python3 log_processing.py "log_file_name.txt"

## JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/graph1.png)   | 60                       | 3.26                                  | 3.26                        | Despite number of users, TS ~= TJ. On average, single user's average query time is 60ms, but throughput is comparably lower than when multiple users are connected.          |
| Case 2: HTTP/10 threads                        | ![](/img/graph2.png)   | 52                         | 2.43                                  | 2.42                        | Average performance slightly higher than HTTPS, most likely due to need for redirection. Improved throughput compared to one thread simulation, but TS still ~= TJ.           |
| Case 3: HTTPS/10 threads                       | ![](/img/graph3.png)   | 51                         | 1.90                                  | 1.90                        | Best in terms of average performance for multiple users, TS ~= TJ.            |
| Case 4: HTTP/10 threads/No connection pooling  | ![](/img/graph4.png)   | 70                         | 1.82                                  | 1.81                        | Lower performance for average query time, but average TS/TJ improved compared to all other test cases. Throughput remained virtually the same compared to previous multiple thread simulations.      |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/graph5.png)   | 50                         | 9.67                                  | 9.67                        | On average, single user's average query time is 50ms using load balancer. Improved greatly compared to no load balancing in terms of throughput and average query time. However, TS & TJ still nearly identical but much higher compared to single instance.           |
| Case 2: HTTP/10 threads                        | ![](/img/graph6.png)   | 53                         | 14.78                                  | 14.77                        | Slightly higher in average performance overall. However, has improved throughput.           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](/img/graph7.png)   | 74                         | 13.28                                  | 13.28                        | Similar to single instance cases, average query time is much higher than other cases with connection pooling. However, TS and TJ are slightly improved to its connection-pooled counterpart with same number of threads. Throughput also dipped in terms of performance significantly.           |
</details>
