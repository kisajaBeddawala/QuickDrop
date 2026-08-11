# Functional Requirements

## 1. Transfer Management

* **FR-01:** The system shall allow users to create a temporary transfer.
* **FR-02:** The system shall generate a unique random transfer code for each transfer.
* **FR-03:** The system shall generate a QR code for each transfer.
* **FR-04:** The system shall allow users to join a transfer using a transfer code.
* **FR-05:** The system shall allow users to join a transfer using a QR code.
* **FR-06:** The system shall display the transfer code to users.
* **FR-07:** The system shall display the expiration time of a transfer.
* **FR-08:** The system shall allow users to view the contents of an active transfer.
* **FR-09:** The system shall prevent access to expired transfers.

## 2. Message Management

* **FR-10:** The system shall allow users to add text messages to a transfer.
* **FR-11:** The system shall allow users to view messages within a transfer.
* **FR-12:** The system shall allow users to delete messages within a transfer.
* **FR-13:** The system shall associate each message with its corresponding transfer.

## 3. File Management

* **FR-14:** The system shall allow users to upload files to a transfer.
* **FR-15:** The system shall support different file types, including images, videos, documents, archives, and other supported files.
* **FR-16:** The system shall enforce a maximum file size for uploads.
* **FR-17:** The system shall store uploaded files using external file storage.
* **FR-18:** The system shall store metadata for uploaded files.
* **FR-19:** The system shall allow users to view files belonging to a transfer.
* **FR-20:** The system shall allow users to download files from a transfer.
* **FR-21:** The system shall allow users to delete files from a transfer.
* **FR-22:** The system shall associate each file with its corresponding transfer.

## 4. Access Control and Data Isolation

* **FR-23:** The system shall validate a transfer code before granting access to a transfer.
* **FR-24:** The system shall provide temporary access credentials after successfully joining a transfer.
* **FR-25:** The system shall validate temporary access credentials for protected transfer operations.
* **FR-26:** The system shall prevent users from accessing data belonging to another transfer.
* **FR-27:** The system shall prevent access to files and messages belonging to expired transfers.

## 5. Transfer Expiration and Cleanup

* **FR-28:** The system shall automatically expire each transfer 24 hours after its creation.
* **FR-29:** The system shall automatically identify expired transfers.
* **FR-30:** The system shall delete messages associated with expired transfers.
* **FR-31:** The system shall delete file metadata associated with expired transfers.
* **FR-32:** The system shall delete files associated with expired transfers from external file storage.
* **FR-33:** The system shall delete expired transfer records from the database.
* **FR-34:** The system shall reject access attempts to expired transfers even before scheduled cleanup is completed.

## 6. Validation and Error Handling

* **FR-35:** The system shall reject invalid transfer codes.
* **FR-36:** The system shall reject access attempts using invalid temporary credentials.
* **FR-37:** The system shall reject uploads that exceed the configured file size limit.
* **FR-38:** The system shall handle file upload failures.
* **FR-39:** The system shall handle external file-storage failures.
* **FR-40:** The system shall return appropriate error responses for invalid requests.
* **FR-41:** The system shall inform users when an operation cannot be completed.

## 7. Transfer Status

* **FR-42:** The system shall maintain the status of each transfer.
* **FR-43:** The system shall identify whether a transfer is active or expired.
* **FR-44:** The system shall prevent modification of data belonging to expired transfers.
