# File processng service
An application that exposes endpoint to uploaded files that can be stored in the file system and in-memory datatype can be used to persist the processed files.


## How to run
Prerequisite: `JAVA_HOME` is set

This is a spring boot application and can be run directly from source directory using java command. The project should start on port `8080`
```shell
 java -jar target/demo-0.0.1-SNAPSHOT.jar 
```
From any terminal , run below command to upload a file from local. for example to upload a file from /tmp/something.txt, run below command
```shell
curl -X POST -F "file=@/tmp/something.txt" http://localhost:8080/api/upload

```
or
```shell
curl --location 'http://localhost:8080/api/upload' \
--form 'file=@"/tmp/test.csv"'
```
```
For every file, a unique ID is generated. You should see an output like this - 

```text
File upload received. Processing is in progress with ID: e98e4944-38a1-4384-a3cc-05434da0c0a6.txt%
```
![img.png](img.png)
```shell
curl http://localhost:8080/api/download/792235ac-5000-4d96-87df-6a6c2f4ade45.txt
```

If you try to download the file using download API
```shell
curl http://localhost:8080/api/download/792235ac-5000-4d96-87df-6a6c2f4ade45.txt
```
you should see this response
```text
File is still processing. Please try again later
```
![img_1.png](img_1.png)

once the file is processed and downloaded, you should be able to download the file by hitting this command
```shell
curl --location 'http://localhost:8080/api/download/85f2f41b-340b-4c1d-bf89-d4be11ddafc3.txt'
```
where `85f2f41b-340b-4c1d-bf89-d4be11ddafc3.txt` is the file name generated after storing the file.



## HTTP Status codes
| HTTP Status Code | Message               |
|------------------|-----------------------|
| 200              | OK                    |
| 201              | Accepted              |
| 400              | Bad Request           |
| 404              | Not Found             |
| 500              | Internal Server Error |
| 503              | Service Unavailable   |
| 423              | In progress           |


## Error handling
All specific and generic exceptions are being handled via `@ControllerAdvice`

