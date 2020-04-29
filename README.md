# cs122b-spring20-team-69
cs122b-spring20-team-69 created by GitHub Classroom

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
### Project 1:
- John: Frontend/Angular coding and setup 
- Alexis: Backend/Spring Boot JDBC coding and setup
</details>

## PROJECT 2

## VIDEO
Link: https://youtu.be/IjvZFraVNKs

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
