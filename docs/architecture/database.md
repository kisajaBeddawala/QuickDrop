# QuickDrop Database Design

## 1. Database

QuickDrop uses **PostgreSQL** as its relational database.

The database stores application data and file metadata. Actual uploaded files are stored in Cloudinary.

---

## 2. Entity Relationship

The V1 database contains three primary entities:

```text
                 ┌──────────────┐
                 │   Transfer   │
                 └──────┬───────┘
                        │
               ┌────────┴────────┐
               │                 │
               │ 1               │ 1
               │                 │
               │ *               │ *
               ▼                 ▼
        ┌──────────────┐  ┌──────────────┐
        │   Message    │  │     File     │
        └──────────────┘  └──────────────┘
```

A transfer can contain multiple messages and multiple files.

---

## 3. Transfer

The `transfers` table represents a temporary transfer room.

### Fields

| Field        | Type        | Constraints      | Description                |
| ------------ | ----------- | ---------------- | -------------------------- |
| `id`         | UUID        | Primary Key      | Unique transfer identifier |
| `code`       | VARCHAR(12) | Unique, Not Null | Public transfer code       |
| `created_at` | TIMESTAMP   | Not Null         | Creation time              |
| `expires_at` | TIMESTAMP   | Not Null         | Expiration time            |
| `status`     | VARCHAR(20) | Not Null         | Current transfer status    |

### Example

```text
id:
550e8400-e29b-41d4-a716-446655440000

code:
K7M4X9P2Q8

created_at:
2026-08-11 21:00:00

expires_at:
2026-08-12 21:00:00

status:
ACTIVE
```

---

## 4. Message

The `messages` table stores text messages shared within a transfer.

### Fields

| Field         | Type      | Constraints | Description               |
| ------------- | --------- | ----------- | ------------------------- |
| `id`          | UUID      | Primary Key | Unique message identifier |
| `transfer_id` | UUID      | Foreign Key | Associated transfer       |
| `content`     | TEXT      | Not Null    | Message content           |
| `created_at`  | TIMESTAMP | Not Null    | Creation time             |

Relationship:

```text
Transfer 1 ─────────── * Message
```

---

## 5. File

The `files` table stores metadata about uploaded files.

The actual file is stored in Cloudinary.

### Fields

| Field                  | Type      | Constraints | Description              |
| ---------------------- | --------- | ----------- | ------------------------ |
| `id`                   | UUID      | Primary Key | Unique file identifier   |
| `transfer_id`          | UUID      | Foreign Key | Associated transfer      |
| `original_name`        | VARCHAR   | Not Null    | Original filename        |
| `cloudinary_public_id` | VARCHAR   | Not Null    | Cloudinary identifier    |
| `resource_type`        | VARCHAR   | Not Null    | Cloudinary resource type |
| `content_type`         | VARCHAR   | Nullable    | MIME type                |
| `size`                 | BIGINT    | Not Null    | File size in bytes       |
| `created_at`           | TIMESTAMP | Not Null    | Upload time              |

Relationship:

```text
Transfer 1 ─────────── * File
```

---

## 6. Complete Database Structure

```text
┌─────────────────────────────────┐
│           transfers             │
├─────────────────────────────────┤
│ PK id UUID                      │
│    code VARCHAR(12) UNIQUE     │
│    created_at TIMESTAMP         │
│    expires_at TIMESTAMP         │
│    status VARCHAR(20)           │
└───────────────┬─────────────────┘
                │
        ┌───────┴────────┐
        │                │
        │ 1              │ 1
        │                │
        │ *              │ *
        ▼                ▼
┌────────────────┐  ┌──────────────────────────┐
│    messages    │  │          files           │
├────────────────┤  ├──────────────────────────┤
│ PK id UUID     │  │ PK id UUID               │
│ FK transfer_id │  │ FK transfer_id           │
│ content TEXT   │  │ original_name             │
│ created_at     │  │ cloudinary_public_id     │
└────────────────┘  │ resource_type             │
                    │ content_type              │
                    │ size BIGINT               │
                    │ created_at                │
                    └──────────────────────────┘
```

---

## 7. Foreign Keys

### Messages

```text
messages.transfer_id
        ↓
transfers.id
```

### Files

```text
files.transfer_id
        ↓
transfers.id
```

Both relationships ensure that messages and files belong to valid transfers.

---

## 8. Indexes

The following indexes should be created:

```text
transfers.code
```

Unique index for fast transfer lookup.

```text
transfers.expires_at
```

Index for efficiently finding expired transfers.

```text
messages.transfer_id
```

Index for retrieving messages belonging to a transfer.

```text
files.transfer_id
```

Index for retrieving files belonging to a transfer.

---

## 9. Data Deletion

When a transfer expires:

```text
Transfer
   │
   ├── Messages ──────► DELETE
   │
   └── Files
        │
        ├── Cloudinary file ──► DELETE
        │
        └── File metadata ───► DELETE
```

Finally:

```text
Transfer ──► DELETE
```

Foreign-key relationships should be configured appropriately so that associated records cannot remain orphaned.

---

## 10. Storage Separation

QuickDrop separates metadata and file storage.

```text
                 QuickDrop
                    │
             ┌──────┴──────┐
             │             │
             ▼             ▼
       PostgreSQL       Cloudinary
             │             │
             │             │
        Metadata        Actual files
             │             │
       ┌─────┼─────┐       │
       ▼     ▼     ▼       ▼
    Transfer Msg   File   ZIP
                      │   Image
                      │   Video
                      └── Document
```

PostgreSQL does not store binary file contents.

---

## 11. Data Lifecycle

```text
Create Transfer
      │
      ▼
Store Transfer
      │
      ▼
Upload Files / Add Messages
      │
      ▼
Transfer remains active
      │
      │ 24 hours
      ▼
Transfer expires
      │
      ▼
Delete Cloudinary files
      │
      ▼
Delete file metadata
      │
      ▼
Delete messages
      │
      ▼
Delete transfer
```
