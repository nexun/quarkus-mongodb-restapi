# Quarkus MongoDB Logstash RestAPI
A simple REST API with Quarkus, Logstash and MongoDB for BBDD2 UNLP.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ 


# 1. Clone this repository
```
git clone https://github.com/nexun/quarkus-mongodb-restapi.git
```
# 2. Install Logstash
https://www.elastic.co/es/downloads/logstash

# 3. Install Logstash Mongodb Output Plugin.
https://www.elastic.co/guide/en/logstash/current/plugins-outputs-mongodb.html
```
bin/logstash-plugin install logstash-output-mongodb
```

# 4. Create *pipeline.conf* in *logstash/bin* folder 
*Important: Set Input path and Output format, also specify your Mongo database*
```
input {
    file {
         path => "C:\AccidentsFileHere.csv"
         start_position => beginning
    }
}

filter{
    csv{
        columns => ["ID","Source","TMC","Severity","Start_Time","End_Time","Start_Lat","Start_Lng","End_Lat","End_Lng","Distance(mi)","Description","Number","Street","Side","City","County","State","Zipcode","Country","Timezone","Airport_Code","Weather_Timestamp","Temperature(F)","Wind_Chill(F)","Humidity(%)","Pressure(in)","Visibility(mi)","Wind_Direction","Wind_Speed(mph)","Precipitation(in)","Weather_Condition","Amenity","Bump","Crossing","Give_Way","Junction","No_Exit","Railway","Roundabout","Station","Stop","Traffic_Calming","Traffic_Signal","Turning_Loop","Sunrise_Sunset","Civil_Twilight","Nautical_Twilight","Astronomical_Twilight"]
        
               }
                mutate{
                    add_field => [ "[geometry]", "%{Start_Lng}" ]
                    add_field => [ "[geometry]", "%{Start_Lat}" ]
                    remove_field => ["message","path","host","Start_Time","End_Time","Start_Lat","Start_Lng","End_Lat","End_Lng"]
                    }
                mutate{
                    convert => [ "[geometry]", "float" ] 
                }    
    
    }

output{
    mongodb{
         uri => "mongodb://127.0.0.1:27017" //Your database here.
         database => "converts"
         collection => "accidents"
    }
}
```
# 5. Run Logstash
```
logstash -f pipeline.conf
```
# 6. Finally: Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```
# 7. Api Endpoints
You have 3 endpoints

1)List all accidents
```shell script
/accidents/start-end

/accidents/1-100
```
In this path you should specify GET method, the starting and the end value, the returning values are the accidents contained in the interval.


2)Accidents inside a polygon
```shell script
/accidents/polygon
[
   {
      "long":-122.66328563750216,
      "lat":38.332236587402136
   },
   {
      "long":-122.36538569651253,
      "lat":37.95741805152669
   },
   {
      "long":-121.9878627298206,
      "lat":38.35485829578574
   },
   {
      "long":-122.66328563750216,
      "lat":38.332236587402136
   }
]
Code snippet to test the endpoint

```
In this path, you should specify POST method and send the body in Json format with the coordinates and it will return the accidents within.


3)Accidents inside radius

```shell script
accidents/radio
[
   {
      "long":-82.3508587993436,
      "lat":34.6372515048226,
      "distanceInKm":100
   }
]
Code snippet to test the endpoint
```
In this path, you should specify POST method and send the body in Json format with the coordinates of a point and radius, it will return the accidents within.


4)Accidents Statistics
```shell script


/analyze/{atribute}/{start}-{end}

/accidents/analyze/Airport_Code/100000-300000
Code snippet to test the endpoint
```

In this path, you should specify POST method and send the atribute which you would like to get statistics, with the the range of elements to check, it will return statistics of the atribute.


