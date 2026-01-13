## DIT Interview Project – Local Setup Guide
### This guide helps users run the application locally.
## Requirements
* Java 17 (Adoptium)
* Maven 3.8+ (optional if you want to run the JAR only)
* Git (optional)

### Installation Steps

#### 1. Install Java
   - Download Java 17 from:
   https://adoptium.net/temurin/releases?version=17&os=any&arch=any
   - Verify: java -version (it should return installed java version on your machine, if not then please set the path)

#### 2. Install Maven (not required if you just want to run given jar)
   - Download from: https://maven.apache.org/download.cgi
   - Verifyusing : mvn -version
   
#### 3. Get Source Code from : https://github.com/shashisharma108/dit-interview-project.git
   Unzip the project or clone repository.

#### 4. Build Project
   ```bash
      mvn clean install
   ```

#### 5. I have used H2 Database, with following details
   - Access URL : http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:test
   - Username: sa
   - Password: (empty)

#### 6. Database DDL script deployment
   - I have placed DDL script here: https://github.com/shashisharma108/dit-interview-project/blob/main/schema.sql
   - Please follow instruction #5 to connect to H2 database and run this script 



#### 7. Run Application
   - Finally, you can run the application using command 
   ```bash
   mvn spring-boot:run
   ```      


#### 8. Access Application
   - Hello API: http://localhost:8080/hello
   - Create API: http://localhost:8080/create
   - Login API: http://localhost:8080/login
   - Authenticate API: http://localhost:8080/authenticate




## 🚀 About Me
Experienced Java Full Stack Developer with over 6 years of expertise in designing,
developing, and deploying dynamic web applications and microservices in
banking, insurance and eCommerce domains.