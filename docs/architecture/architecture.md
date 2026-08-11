# QuickDrop System Architecture

## 1. Architecture Overview

QuickDrop uses a **monolithic layered architecture**.

The system consists of:

* React frontend
* Spring Boot backend
* PostgreSQL database
* Cloudinary file storage

```text
┌─────────────────────────────┐
│      Mobile / Desktop       │
│          Browser            │
└──────────────┬──────────────┘
               │
               │ HTTPS / REST
               ▼
┌─────────────────────────────┐
│       React Frontend        │
│     TypeScript + UI         │
└──────────────┬──────────────┘
               │
               │ REST API
               ▼
┌─────────────────────────────┐
│      Spring Boot Backend    │
│                             │
│ ┌─────────────────────────┐ │
│ │      Controllers        │ │
│ └────────────┬────────────┘ │
│              ▼              │
│ ┌─────────────────────────┐ │
│ │        Services         │ │
│ └────────────┬────────────┘ │
│              ▼              │
│ ┌─────────────────────────┐ │
│ │       Repositories       │ │
│ └────────────┬────────────┘ │
└──────────────┼──────────────┘
               │
               ▼
        ┌─────────────┐
        │ PostgreSQL  │
        └─────────────┘

Spring Boot
     │
     ▼
┌─────────────┐
│ Cloudinary  │
│ File Store  │
└─────────────┘
```

---

## 2. Architectural Style

The backend follows a layered architecture:

```text
Controller
     ↓
Service
     ↓
Repository
     ↓
Database
```

External file operations follow:

```text
Controller
     ↓
Service
     ↓
Cloudinary
```

### Controller Layer

Responsible for:

* Receiving HTTP requests
* Validating request structure
* Calling appropriate services
* Returning HTTP responses

### Service Layer

Responsible for:

* Business logic
* Transfer validation
* Access validation
* File operations
* Message operations
* Expiration logic

### Repository Layer

Responsible for:

* Database operations
* Querying entities
* Saving entities
* Updating entities
* Deleting entities

### Model Layer

Contains the application's database entities:

* Transfer
* Message
* File

---

## 3. Main Components

### Frontend

Technology:

* React
* TypeScript
* Tailwind CSS

Responsibilities:

* Display user interface
* Create transfers
* Join transfers
* Display QR codes
* Manage messages
* Upload files
* Download files
* Display expiration countdown
* Communicate with the backend API

### Backend

Technology:

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven

Responsibilities:

* REST API
* Business logic
* Transfer management
* Access control
* File management
* Message management
* Database communication
* Cloudinary integration
* Automatic cleanup

### PostgreSQL

Stores:

* Transfer records
* Message records
* File metadata
* Transfer status and timestamps

The actual uploaded files are not stored in PostgreSQL.

### Cloudinary

Stores:

* Uploaded images
* Videos
* Documents
* Archives
* Other uploaded files

PostgreSQL stores the corresponding Cloudinary metadata and identifiers.

---

## 4. Transfer Lifecycle

```text
              CREATE
                 │
                 ▼
              ACTIVE
                 │
                 │ 24 hours
                 ▼
              EXPIRED
                 │
                 ▼
              CLEANUP
                 │
        ┌────────┼────────┐
        ▼        ▼        ▼
    Messages   Metadata  Files
     Delete     Delete   Delete
                          │
                          ▼
                      Cloudinary
```

The system checks `expires_at` whenever a transfer is accessed.

Therefore, an expired transfer cannot be accessed even if the scheduled cleanup task has not yet removed its records.

---

## 5. Transfer Access

QuickDrop does not use permanent user accounts.

The access flow is:

```text
Create Transfer
       │
       ▼
Generate Random Code
       │
       ▼
Generate Temporary Access Credential
       │
       ▼
Display Code + QR
```

Another device can join using:

```text
Transfer Code
      OR
QR Code
```

After joining:

```text
Temporary Access Credential
          ↓
Protected API Requests
          ↓
Transfer Validation
          ↓
Allow / Reject
```

---

## 6. File Upload Flow

```text
User selects file
       │
       ▼
React Frontend
       │
       │ multipart/form-data
       ▼
FileController
       │
       ▼
FileService
       │
       ├── Validate access
       ├── Check expiration
       └── Validate file size
       │
       ▼
Cloudinary
       │
       ▼
Upload successful
       │
       ▼
Save file metadata
       │
       ▼
PostgreSQL
```

---

## 7. File Download Flow

```text
User clicks Download
       │
       ▼
React
       │
       ▼
FileController
       │
       ▼
FileService
       │
       ├── Validate access
       ├── Check expiration
       └── Find file metadata
       │
       ▼
Cloudinary
       │
       ▼
File delivered to user
```

---

## 8. Message Flow

### Add Message

```text
React
  ↓
MessageController
  ↓
MessageService
  ↓
MessageRepository
  ↓
PostgreSQL
```

### Read Messages

```text
React
  ↓
MessageController
  ↓
MessageService
  ↓
MessageRepository
  ↓
PostgreSQL
  ↓
Messages
```

---

## 9. Automatic Cleanup

The backend uses a scheduled task to find expired transfers.

```text
Spring Scheduler
       │
       ▼
Find expired transfers
       │
       ▼
Find associated files
       │
       ▼
Delete files from Cloudinary
       │
       ▼
Delete messages
       │
       ▼
Delete file metadata
       │
       ▼
Delete transfer
```

Cleanup failures should be handled so that one failed transfer does not stop the processing of other expired transfers.

---

## 10. Security Boundaries

The system enforces isolation at the backend.

Every protected resource operation should verify:

```text
1. Access credential is valid
2. Credential belongs to the requested transfer
3. Transfer exists
4. Transfer has not expired
5. Requested resource belongs to the transfer
```

Example:

```text
Transfer A
├── file-a.zip
└── message-a

Transfer B
├── file-b.zip
└── message-b
```

A participant of Transfer A must not be able to request:

```text
Transfer B
     ↓
file-b.zip
```

---

## 11. Architectural Decisions

| Decision               | Choice                      |
| ---------------------- | --------------------------- |
| Architecture           | Monolithic                  |
| Backend architecture   | Layered                     |
| API style              | REST                        |
| Frontend               | React + TypeScript          |
| Backend                | Spring Boot                 |
| Database               | PostgreSQL                  |
| ORM                    | Spring Data JPA / Hibernate |
| File storage           | Cloudinary                  |
| QR generation          | ZXing                       |
| Authentication         | Temporary transfer access   |

---

## 12. Future Architecture Extensions

The architecture can later be extended with:

* WebSocket-based real-time updates
* Redis caching
* Rate limiting infrastructure
* Direct/local-network file transfer
* Password-protected transfers
* End-to-end encryption
* Background job processing
* Larger-scale deployment
