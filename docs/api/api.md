# QuickDrop API Documentation

## 1. API Overview

Base URL:

```text
/api
```

API style:

```text
REST
```

Content types:

```text
application/json
multipart/form-data
```

QuickDrop does not require permanent user accounts.

Access to a transfer is controlled using a temporary access credential.

---

# 2. Transfer API

## 2.1 Create Transfer

Creates a new temporary transfer.

### Request

```http
POST /api/transfers
```

### Request Body

No request body required.

### Response

**201 Created**

```json
{
  "code": "K7M4X9P2Q8",
  "expiresAt": "2026-08-12T21:00:00Z",
  "joinUrl": "https://quickdrop.app/join/K7M4X9P2Q8",
  "accessToken": "temporary-access-token"
}
```

### Description

The server:

1. Generates a random transfer code.
2. Creates a transfer.
3. Sets the expiration time to 24 hours.
4. Generates a temporary access credential.
5. Returns the transfer information.

---

# 3. Join Transfer API

## 3.1 Join Transfer

Allows another device to join an existing transfer.

### Request

```http
POST /api/transfers/join
```

### Request Body

```json
{
  "code": "K7M4X9P2Q8"
}
```

### Response

**200 OK**

```json
{
  "code": "K7M4X9P2Q8",
  "expiresAt": "2026-08-12T21:00:00Z",
  "accessToken": "temporary-access-token"
}
```

### Possible Errors

```text
400 Bad Request
```

Invalid request.

```text
404 Not Found
```

Transfer does not exist.

```text
410 Gone
```

Transfer has expired.

---

# 4. Get Transfer

## 4.1 Get Transfer Information

Returns information about an active transfer.

### Request

```http
GET /api/transfers/{code}
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**200 OK**

```json
{
  "code": "K7M4X9P2Q8",
  "expiresAt": "2026-08-12T21:00:00Z",
  "status": "ACTIVE"
}
```

---

# 5. Message API

## 5.1 Add Message

Adds a message to a transfer.

### Request

```http
POST /api/transfers/{code}/messages
```

### Headers

```http
Authorization: Bearer <access-token>
Content-Type: application/json
```

### Request Body

```json
{
  "content": "Here is the project ZIP file."
}
```

### Response

**201 Created**

```json
{
  "id": "message-uuid",
  "content": "Here is the project ZIP file.",
  "createdAt": "2026-08-11T21:15:00Z"
}
```

---

## 5.2 Get Messages

Returns all messages belonging to a transfer.

### Request

```http
GET /api/transfers/{code}/messages
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**200 OK**

```json
[
  {
    "id": "message-uuid-1",
    "content": "Here is the project.",
    "createdAt": "2026-08-11T21:15:00Z"
  },
  {
    "id": "message-uuid-2",
    "content": "Download the ZIP file.",
    "createdAt": "2026-08-11T21:16:00Z"
  }
]
```

---

## 5.3 Delete Message

Deletes a message from a transfer.

### Request

```http
DELETE /api/transfers/{code}/messages/{messageId}
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**204 No Content**

---

# 6. File API

## 6.1 Upload File

Uploads a file to a transfer.

### Request

```http
POST /api/transfers/{code}/files
```

### Headers

```http
Authorization: Bearer <access-token>
Content-Type: multipart/form-data
```

### Form Data

```text
file: <selected-file>
```

### Response

**201 Created**

```json
{
  "id": "file-uuid",
  "originalName": "project.zip",
  "contentType": "application/zip",
  "size": 18500000,
  "createdAt": "2026-08-11T21:20:00Z"
}
```

### Possible Errors

```text
400 Bad Request
```

Invalid file upload.

```text
401 Unauthorized
```

Invalid access credential.

```text
410 Gone
```

Transfer has expired.

```text
413 Payload Too Large
```

File exceeds the configured maximum size.

```text
500 Internal Server Error
```

File storage failure.

---

# 7. Get Files

## 7.1 List Transfer Files

Returns all files belonging to a transfer.

### Request

```http
GET /api/transfers/{code}/files
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**200 OK**

```json
[
  {
    "id": "file-uuid-1",
    "originalName": "project.zip",
    "contentType": "application/zip",
    "size": 18500000,
    "createdAt": "2026-08-11T21:20:00Z"
  },
  {
    "id": "file-uuid-2",
    "originalName": "screenshot.png",
    "contentType": "image/png",
    "size": 850000,
    "createdAt": "2026-08-11T21:21:00Z"
  }
]
```

---

# 8. Download File

## 8.1 Download File

Downloads a file belonging to the transfer.

### Request

```http
GET /api/transfers/{code}/files/{fileId}/download
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**200 OK**

The server returns the requested file.

Example:

```http
Content-Type: application/zip
Content-Disposition: attachment; filename="project.zip"
```

---

# 9. Delete File

## 9.1 Delete File

Deletes a file from the transfer.

### Request

```http
DELETE /api/transfers/{code}/files/{fileId}
```

### Headers

```http
Authorization: Bearer <access-token>
```

### Response

**204 No Content**

The system should:

1. Delete the file from Cloudinary.
2. Delete the file metadata from PostgreSQL.

---

# 10. Health Check

The backend provides a health endpoint for monitoring.

### Request

```http
GET /actuator/health
```

### Response

**200 OK**

```json
{
  "status": "UP"
}
```

---

# 11. Authentication / Authorization

QuickDrop does not use traditional user accounts.

Instead, it uses temporary transfer access credentials.

### Flow

```text
Create Transfer
      │
      ▼
Temporary Access Credential
      │
      ▼
Client stores credential
      │
      ▼
Protected API request
      │
      ▼
Authorization header
```

Example:

```http
Authorization: Bearer <access-token>
```

The backend validates:

```text
1. Token exists
2. Token is valid
3. Token belongs to requested transfer
4. Transfer exists
5. Transfer is not expired
```

---

# 12. Error Response Format

The API should use a consistent error response.

Example:

```json
{
  "timestamp": "2026-08-11T21:30:00Z",
  "status": 404,
  "error": "TRANSFER_NOT_FOUND",
  "message": "Transfer does not exist."
}
```

Another example:

```json
{
  "timestamp": "2026-08-11T21:30:00Z",
  "status": 410,
  "error": "TRANSFER_EXPIRED",
  "message": "This transfer has expired."
}
```

---

# 13. HTTP Status Codes

| Status | Meaning                                  |
| -----: | ---------------------------------------- |
|  `200` | Successful request                       |
|  `201` | Resource created                         |
|  `204` | Successful request with no response body |
|  `400` | Invalid request                          |
|  `401` | Missing or invalid authentication        |
|  `403` | Access denied                            |
|  `404` | Resource not found                       |
|  `410` | Transfer expired                         |
|  `413` | File too large                           |
|  `429` | Too many requests                        |
|  `500` | Internal server error                    |
|  `502` | External service failure                 |

---

# 14. API Summary

## Transfer

```text
POST   /api/transfers
POST   /api/transfers/join
GET    /api/transfers/{code}
```

## Messages

```text
POST   /api/transfers/{code}/messages
GET    /api/transfers/{code}/messages
DELETE /api/transfers/{code}/messages/{messageId}
```

## Files

```text
POST   /api/transfers/{code}/files
GET    /api/transfers/{code}/files
GET    /api/transfers/{code}/files/{fileId}/download
DELETE /api/transfers/{code}/files/{fileId}
```

## Monitoring

```text
GET /actuator/health
```

---

# 15. API Development Order

The API should be implemented in this order:

```text
1. Create Transfer
        ↓
2. Join Transfer
        ↓
3. Get Transfer
        ↓
4. Add Message
        ↓
5. Get Messages
        ↓
6. Upload File
        ↓
7. Get Files
        ↓
8. Download File
        ↓
9. Delete File
        ↓
10. Delete Message
        ↓
11. Automatic Cleanup
        ↓
12. Security hardening
```

This order allows the backend to be developed and tested incrementally.
