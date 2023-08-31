# Simple SFTP gateway service

This is a simple webflux API implementation of sftp upload and download binary files from SFTP Server.

Binary files that downloaded from/ uploaded to SFTP Server are handled as BASE64 encoded payload in WebLayer Request/Response json data.

### Upload Request

`POST` /upload
```json
{
    "name": "file name with file extension", // asdf.pdf
    "path": "SFTP file path this file can be saved", // /home/documents/sftp/
    "payload": "BASE64 encoded data string"
}
```
`200`
```json
    "result": boolean, // true or false
    "location": "saved file's absolute path" // /home/documents/sftp/asdf.pdf
```
`error`
```json
{
    "error": "error status phrase", // Bad Request
    "message": "detail message" // Request Body is missing
}
```  
<br>

### Download Request

`POST` /upload
```json
{
    "name": "file name with file extension", // asdf.pdf
    "path": "SFTP file path this file could be placed", // /home/documents/sftp/
}
```
`200`
```json
    "result": boolean, // true or false
    "payload": "loaded file's BASE64 encoded data strng" // null if result is false
```
`error`
```json
{
    "error": "error status phrase", // Bad Request
    "message": "detail message" // Request Body is missing
}
```

### dependencies

- Java 11
- spring-boot: 2.7.15
    - webflux
    - jdbc
    - data-jpa
- com.jcraft.jsch: 0.1.55
- h2
- lombok