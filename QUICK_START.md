🚗 RideShare Backend – Quick Start (5-Minute Setup)

Get the backend running instantly with this beginner-friendly setup guide!

⚡ Start in 5 Minutes
Step 1: Install & Verify Requirements (1 minute)

Make sure the following are installed and running:

Java 17+

Maven 3.6+

MongoDB (running via mongod)

Check the versions:

java -version
mvn -version

Step 2: Build the Project (2 minutes)

Navigate to your project folder:

cd c:\Users\LENOVO\Desktop\springbootproj
mvn clean install

Step 3: Launch the Application (1 minute)
mvn spring-boot:run

Step 4: Confirm Successful Startup

You should see logs similar to:

Started RideShareApplication in X.XXX seconds
Server started on port 8081


✔ Application running here: http://localhost:8081

🧪 Quick Functional Test (2 minutes)
1️⃣ User Registration
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d "{\"username\":\"testuser\",\"password\":\"test123\",\"role\":\"ROLE_USER\"}"


Expected output:
✔ “User registered successfully”

2️⃣ Login to Get JWT Token
curl -X POST http://localhost:8081/api/auth/login \
-H "Content-Type: application/json" \
-d "{\"username\":\"testuser\",\"password\":\"test123\"}"


Expected:
✔ A JWT token string

3️⃣ Create a Ride (Replace TOKEN)
curl -X POST http://localhost:8081/api/v1/rides \
-H "Authorization: Bearer TOKEN" \
-H "Content-Type: application/json" \
-d "{\"pickupLocation\":\"Location A\",\"dropLocation\":\"Location B\"}"


Expected:
✔ Ride generated with status "REQUESTED"

📚 Helpful Documents
File	What it Explains	Time
README.md	Complete API usage	~15 min
TESTING_GUIDE.md	Full test workflow	~20 min
DEPLOYMENT_GUIDE.md	Production setup tips	~30 min
PROJECT_SUMMARY.md	Overview of entire system	~10 min
🎯 Features Implemented

✔ User Registration & Login

✔ JWT Auth Flow

✔ Ride Creation & Tracking

✔ Role-Based Permissions (User/Driver)

✔ Strong Input Validation

✔ Unified Error Handling

✔ MongoDB Integration

✔ Structured, Maintainable Architecture

🚀 What to Do Next

Want to test more? → TESTING_GUIDE.md

Ready to deploy? → DEPLOYMENT_GUIDE.md

Need API details? → README.md

Want full overview? → PROJECT_SUMMARY.md

🆘 Common Issues & Fixes
Problem	Fix
Port 8081 busy	Update port in application.properties
Can't connect to MongoDB	Start Mongo (mongod)
Maven build errors	Run mvn clean install
Token not accepted	Login again for a new JWT
📁 Folder Layout
c:\Users\LENOVO\Desktop\springbootproj\
├── pom.xml
├── src/main/java/org/example/rideshare/
├── src/main/resources/application.properties
├── README.md
├── TESTING_GUIDE.md
├── DEPLOYMENT_GUIDE.md
├── PROJECT_SUMMARY.md
├── RideShare_API_Collection.postman_collection.json
└── .gitignore

🏛 Architecture Overview
Client Request
      ↓
JWT Authentication Filter
      ↓
REST Controllers
      ↓
Service Layer (Business Rules)
      ↓
Repository Layer
      ↓
MongoDB Database

📊 Quick Look at API Endpoints
Authentication

POST /api/auth/register

POST /api/auth/login

Passenger

POST /api/v1/rides

GET /api/v1/user/rides

POST /api/v1/rides/{id}/complete

Driver

GET /api/v1/driver/rides/requests

POST /api/v1/driver/rides/{id}/accept

POST /api/v1/rides/{id}/complete

✨ Project Completion Status

✔ Code finalized
✔ Documentation fully written
✔ Testing done
✔ Production-ready build available

🎉 You're Ready to Go!

Your backend is ready for development, testing, or deployment.
Start with the quick tests, and explore the deeper guides when needed.

Last Updated: January 2025
Version: 1.0.0
Status: ✔ Ready for Production