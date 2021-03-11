const express = require('express');
const app = express();
app.use(express.json());

const MongoClient = require("mongodb").MongoClient;
const uri = "mongodb://localhost:27017";

const fcm_push = require('fcm-push');

const client = new MongoClient(uri, { useNewUrlParser: true , useUnifiedTopology: true});
client.connect(() => console.log("connected"));
const db = client.db("RunBuddy");
console.log(`connected to database ${db.databaseName}`);



app.post('/newToken/:token', (req, res) => {
    token = req.params['token'];
});

app.post("/create_user", async(req, res) => {
        try{
            const Users = db.collection("Users");
            Users.insertOne(req.body);
            console.log(req.body);
            res.send(req.body);
        } catch(err) {
            console.log("ERROR");
            res.send({ message: err });
        }
})

app.post("/:email/radius", async(req, res) => {
        try{
            const Users = db.collection("Radiuses");
            Users.insertOne(req.body);
            console.log(req.body);
            res.send(req.body);
        } catch(err) {
            console.log("ERROR");
            res.send({ message: err });
        }
})

app.post("/create_activity", async(req, res) => {
    try{
        const Users = db.collection("Activities");
        console.log(req.body);
        res.send(req.body);
    } catch(err) {
        console.log("ERROR");
        res.send({ message: err });
    }
})

app.get("/:email/check", async (req, res) => {
    const Users = db.collection("Users");
    let username = req.params.email;
    console.log(username);

    //search for username got as parameter
    const cursor = await Users.find({"username": username});
    const result = await cursor.toArray();

    //check if populated table contains a user
    if (result.length > 0){
        console.log("true");
        res.send("true");
    } else {
        console.log("false");
        res.send("false");
    }
})

app.get("/all_activities", async (req, res) => {
    const Activities = db.collection("Activities");
    const Users = db.collection("Users");

    updateActivities();

    const cursor = await Activities.find();
    const result = await cursor.toArray();
    let array = [];
    
    await result.forEach(async function(activity, index){
        let newJson = activity;
        let currMail = activity.email;
        let users = await Users.find({"email" : currMail})
        let user = await users.toArray();
    
        newJson["userName"] = user[0].firstName + " " + user[0].lastName;
        
        array.push(newJson);
        
    });
    console.log(result);
    res.send(result);
})

async function updateActivities(){
    let date = new Date();
    let month = date.getMonth + 1;
    let day = date.getDate + 1;
    let hours = date.getHours;
    let minutes = date.getMinutes;

    const Activities = db.collection("Activities");
    const cursor = await Activities.find();
    cursor.forEach((activity) => {
        let actDate = activity.date.split('/');
        let actDay = Number(actDate[0]);
        let actMonth = Number(actDate[1]);
        let actTime = activity.time.split(':');
        let actHours = Number(actTime[0]);
        let actMinutes = Number(actTime[1]);
        
        if (month > actMonth){
            Activities.deleteOne(activity);
        }
        if (month == actMonth) {
            if (day > actDay){
                Activities.deleteOne(activity);
            }
            if (day == actDay){
                if (hours > actHours){
                    Activities.deleteOne(activity);
                }
                if (hours == actHours){
                    if (minutes > actMinutes) {
                        Activities.deleteOne(activity);
                    }
                }
            }
        }
    })
}

app.get("/", (req, res) =>{
    console.log("GETGETGET");
    res.send("Got it!!!")
})

app.listen(8080, () => {
    console.log('Listening on port 8080');
});
