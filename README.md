# QuickDrop

> Temporary, private file and message sharing between devices.

QuickDrop is a temporary cross-device file and message sharing web application designed to make transferring files and text between devices simple and fast.

Instead of sending a file through messaging applications, email, or permanent cloud storage, users can create a temporary transfer room and share it with another device using a randomly generated code or QR code.

All transfer data is automatically deleted after 24 hours.

---

## Features

### Transfer Rooms

* Create a temporary transfer room.
* Generate a unique random transfer code.
* Generate a QR code for quickly joining a transfer.
* Join a transfer using a transfer code.
* Join a transfer by scanning a QR code.
* Display the remaining transfer lifetime.

### Message Sharing

* Add text messages to a transfer.
* View messages in a transfer.
* Delete messages.

### File Sharing

* Upload files to a transfer.
* Download files from a transfer.
* Delete files.
* Support general file types such as:

  * Images
  * Videos
  * Documents
  * ZIP/archives
  * Text files
  * Other supported file types
* Enforce a maximum file size per upload.

### Privacy and Temporary Storage

* Each transfer has its own isolated data.
* Users cannot access data belonging to another transfer.
* Transfers expire automatically after 24 hours.
* Expired messages are deleted.
* Expired file metadata is deleted.
* Expired files are removed from external file storage.

---


## System Architecture

QuickDrop uses a simple monolithic architecture.

```text
┌───────────────────────┐
│   Mobile / Desktop    │
│       Browser         │
└───────────┬───────────┘
            │
            │ HTTPS / REST
            ▼
┌───────────────────────┐
│    React Frontend     │
│   TypeScript + UI     │
└───────────┬───────────┘
            │
            │ REST API
            ▼
┌───────────────────────┐
│    Spring Boot API    │
│                       │
│ Controller            │
│      ↓                │
│ Service               │
│      ↓                │
│ Repository            │
└───────┬─────────┬─────┘
        │         │
        ▼         ▼
┌────────────┐  ┌────────────┐
│ PostgreSQL │  │ Cloudinary │
│            │  │            │
│ Metadata   │  │ Actual     │
│            │  │ Files      │
└────────────┘  └────────────┘
```

---

## Technology Stack

### Frontend

* React
* TypeScript
* Tailwind CSS
* REST API communication

### Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Bean Validation
* Maven

### Database

* PostgreSQL

### File Storage

* Cloudinary

### QR Code

* ZXing

### Development

* Git
* GitHub
* IntelliJ IDEA / VS Code
* Postman or similar API testing tool

---

## Project Structure

```text
quickdrop/
│
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/quickdrop/
│       │   │   ├── controller/
│       │   │   ├── service/
│       │   │   ├── repository/
│       │   │   ├── model/
│       │   │   ├── dto/
│       │   │   ├── security/
│       │   │   ├── config/
│       │   │   └── exception/
│       │   │
│       │   └── resources/
│       │
│       └── test/
│
├── frontend/
│   ├── package.json
│   ├── public/
│   └── src/
│       ├── components/
│       ├── pages/
│       ├── hooks/
│       ├── services/
│       ├── types/
│       ├── utils/
│       └── lib/
│
├── docs/
│   ├── requirements/
│   ├── architecture/
│   └── api/
│
└── README.md
```

## API Overview

### Transfers

```text
POST   /api/transfers
POST   /api/transfers/join
GET    /api/transfers/{code}
```

### Messages

```text
POST   /api/transfers/{code}/messages
GET    /api/transfers/{code}/messages
DELETE /api/transfers/{code}/messages/{messageId}
```

### Files

```text
POST   /api/transfers/{code}/files
GET    /api/transfers/{code}/files
GET    /api/transfers/{code}/files/{fileId}/download
DELETE /api/transfers/{code}/files/{fileId}
```

---

## Security

QuickDrop does not require permanent user accounts.

Access is based on a temporary transfer credential.

The system will:

* Generate unpredictable transfer codes.
* Validate transfer access for protected operations.
* Isolate data between transfers.
* Prevent access to expired transfers.
* Enforce file size limits.
* Keep Cloudinary credentials on the backend.
* Use HTTPS in production.
* Apply rate limiting to transfer access attempts.

---

## Data Retention

QuickDrop is designed as a temporary data-sharing service.

Each transfer has a maximum lifetime of:

```text
24 hours
```

After expiration, all associated data should be removed.

QuickDrop is **not intended to be used as permanent cloud storage or backup storage**.

---

## Development Goals

This project is also intended as a practical Java and Spring Boot learning project.

The development process will focus on understanding:

* Core Java
* Object-oriented programming
* Collections
* Exceptions
* Interfaces
* Maven
* Spring Boot
* REST APIs
* Dependency Injection
* Spring Data JPA
* Hibernate
* PostgreSQL
* File uploads
* External API integration
* Scheduled tasks
* Backend security
* Git and GitHub

---

## Future Improvements

Potential future features include:

* Real-time transfer updates
* Drag-and-drop uploads
* Download all files
* Password-protected transfers
* More granular permissions
* Local-network/direct device transfer
* End-to-end encryption
* Transfer activity information
* Larger file support
* Improved file previews

These features are outside the initial V1 scope.

---

## V1 Scope

The initial version focuses on:

```text
Create Transfer
       ↓
Generate Code + QR
       ↓
Join Transfer
       ↓
Upload / Download Files
       +
Add / Read Messages
       ↓
24 Hours
       ↓
Automatic Cleanup
```

The goal is to build a functional, secure, and understandable MVP rather than adding unnecessary complexity.

---


