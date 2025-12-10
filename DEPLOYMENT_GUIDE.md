.

🚀 RideShare Backend – Deployment & Production Manual

A step-by-step guide to deploying the RideShare Backend into a stable, scalable, and secure production environment.

✅ Pre-Deployment Readiness

Before pushing the system live, ensure:

🔍 Code & Testing

All test cases pass successfully

No warnings or errors in the logs

Code review completed

Security vulnerabilities resolved

Load tests executed

Performance verified under stress

🔐 Production Preparation

Replace JWT secret with a strong random key

Secure database credentials

Configure production server

Install SSL/TLS certificates

Enable logging & monitoring

Set up automated backups

🏗️ Building the Production Artifact
1️⃣ Create the JAR
mvn clean package -DskipTests


Result:
target/rideshare-1.0.0.jar

2️⃣ Run Locally for Validation
java -jar target/rideshare-1.0.0.jar

3️⃣ Confirm Logs

You should see:

INFO  RideShareApplication : Starting...
INFO  Started RideShareApplication in 2.3 seconds
INFO  Server running on port 8081

🐳 Deploying via Docker
1️⃣ Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/rideshare-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

2️⃣ Build the Image
docker build -t rideshare-backend:1.0.0 .

3️⃣ Launch Container
docker run -d \
  --name rideshare-api \
  -p 8081:8081 \
  -e MONGODB_URI="mongodb://mongo:27017/rideshare" \
  -e JWT_SECRET="your-production-secret" \
  -e JWT_EXPIRATION="86400000" \
  --network rideshare-network \
  rideshare-backend:1.0.0

🐳 Docker Compose (Preferred)
docker-compose.yml
version: '3.8'

services:
  mongodb:
    image: mongo:5.0
    container_name: rideshare-mongo
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_DATABASE: rideshare
    volumes:
      - mongo_data:/data/db
    networks:
      - rideshare-network

  rideshare-api:
    build: .
    container_name: rideshare-api
    ports:
      - "8081:8081"
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongodb:27017/rideshare
      JWT_SECRET: your-secure-key-256bit
      JWT_EXPIRATION: 86400000
    depends_on:
      - mongodb
    networks:
      - rideshare-network

volumes:
  mongo_data:

networks:
  rideshare-network:
    driver: bridge

Run:
docker-compose up -d

☁️ Cloud Deployments
⭐ AWS Elastic Beanstalk
1️⃣ Add .ebextensions/java.config
option_settings:
  aws:autoscaling:launchconfiguration:
    InstanceType: t3.micro
  aws:elasticbeanstalk:container:java:
    JDK: openjdk17
  aws:elasticbeanstalk:application:environment:
    SPRING_DATA_MONGODB_URI: <your-atlas-uri>
    JWT_SECRET: <your-secret>

2️⃣ Deploy
pip install awsebcli
eb init -p java-17 rideshare-api
eb create rideshare-env
eb deploy

⭐ Azure App Service
az group create --name rideshare-rg --location eastus
az appservice plan create --name rideshare-plan --resource-group rideshare-rg --sku B1 --is-linux
az webapp create --resource-group rideshare-rg --plan rideshare-plan --name rideshare-api --runtime "JAVA|17-java17"
az webapp deployment source config-zip --resource-group rideshare-rg --name rideshare-api --src target/rideshare-1.0.0.jar


Set environment variables:

az webapp config appsettings set \
--resource-group rideshare-rg \
--name rideshare-api \
--settings SPRING_DATA_MONGODB_URI=<uri> JWT_SECRET=<secret>

⭐ Google Cloud Run

Build and deploy:

gcloud builds submit --tag gcr.io/PROJECT-ID/rideshare-api:1.0.0

gcloud run deploy rideshare-api \
  --image gcr.io/PROJECT-ID/rideshare-api:1.0.0 \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars=SPRING_DATA_MONGODB_URI=<uri>,JWT_SECRET=<secret>

🔒 Production Security Settings
application.properties recommendations:
server.port=8081
spring.data.mongodb.uri=<secure-mongo-uri>

jwt.secret=<strong-256-bit-secret>
jwt.expiration=86400000

logging.level.root=WARN
logging.level.org.springframework.security=WARN
logging.level.org.example.rideshare=INFO

server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.same-site=strict

📡 Monitoring & Actuator

Add dependency:

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>


Enable endpoints:

management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=always


Check:

curl http://localhost:8081/actuator/health
curl http://localhost:8081/actuator/metrics

📈 Application Scaling
Horizontal Scaling (recommended)

Run multiple instances on different ports:

java -jar rideshare.jar --server.port=8081
java -jar rideshare.jar --server.port=8082


Load balance via Nginx:

upstream rideshare {
    server localhost:8081;
    server localhost:8082;
}

Vertical Scaling

Increase CPU & RAM

Increase DB pool size

Improve index usage

🗄️ Database Backup Plans
Automated Backups (MongoDB Atlas)

Enable scheduled backups in Atlas dashboard.

Manual backup
mongodump --uri "<mongo-uri>" --out backup/rideshare_$(date +%Y%m%d)


Restore:

mongorestore --uri "<mongo-uri>" backup/rideshare_20250120

🔐 SSL/TLS (HTTPS)

Using Let’s Encrypt:

sudo apt install certbot
sudo certbot certonly --standalone -d api.rideshare.com


Convert to PKCS12:

openssl pkcs12 -export \
  -in fullchain.pem \
  -inkey privkey.pem \
  -out keystore.p12 \
  -name rideshare


Configure Spring Boot:

server.ssl.key-store=keystore.p12
server.ssl.key-store-password=<password>
server.ssl.key-store-type=PKCS12
server.port=8443

🧪 Final Production Validation
Check:

App reachable

Authentication works

Database responds

Ride creation workflow successful

Logs clean

Monitoring active

SSL certificate valid

🔄 Rollback Procedure
Roll back the application:
java -jar target/rideshare-0.9.9.jar

Roll back database:
mongorestore --uri "<mongo-uri>" backup/rideshare_backup_latest
