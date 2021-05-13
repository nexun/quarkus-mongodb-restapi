# Quarkus MongoDB Logstash RestAPI
A simple REST API with Quarkus, Logstash and MongoDB for BBDD2 UNLP.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ 
##1. Clone this repository
```
git clone https://github.com/nexun/quarkus-mongodb-restapi.git
```
##2. Install Logstash
https://www.elastic.co/es/downloads/logstash

##3. Install Logstash Mongodb Output Plugin.
https://www.elastic.co/guide/en/logstash/current/plugins-outputs-mongodb.html
```
bin/logstash-plugin install logstash-output-mongodb
```
##4. Create *pipeline.conf* in *logstash/bin* folder 
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
##5. Run Logstash
```
logstash -f pipeline.conf
```
##6. Finally: Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```


> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

#**_Additional Options_**
## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Be aware that it’s not an _über-jar_ as
the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/bd2api-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html
.

## Related guides

- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
