# Non-Functional Requirements

## 1. Performance

* **NFR-01:** The system should provide responsive responses for normal transfer, message, and metadata operations.
* **NFR-02:** The system should support multiple users accessing different transfers concurrently.
* **NFR-03:** The system should handle file uploads and downloads efficiently.
* **NFR-04:** The system should avoid unnecessary database queries when processing transfer requests.

## 2. Security

* **NFR-05:** Transfer codes shall be generated using a cryptographically secure random mechanism.
* **NFR-06:** Transfer codes shall contain sufficient randomness to make unauthorized guessing impractical.
* **NFR-07:** The system shall isolate data between different transfers.
* **NFR-08:** Temporary access credentials shall be securely generated and validated.
* **NFR-09:** Cloudinary credentials and other sensitive configuration values shall not be exposed to the frontend.
* **NFR-10:** Sensitive configuration values shall be stored using environment variables or secure deployment configuration.
* **NFR-11:** The production application shall use HTTPS.
* **NFR-12:** The system should protect transfer access endpoints against excessive guessing attempts.
* **NFR-13:** The system shall validate file size and relevant upload metadata before storing files.
* **NFR-14:** The system shall prevent access to expired transfer data.

## 3. Privacy and Data Retention

* **NFR-15:** Transfer data shall be temporary and shall not be retained beyond the defined 24-hour lifetime.
* **NFR-16:** Expired transfer data shall be removed from the application database.
* **NFR-17:** Expired files shall be removed from external file storage.
* **NFR-18:** The system shall not require permanent user accounts for basic file and message sharing.
* **NFR-19:** The system shall not expose transfer contents through publicly accessible global listings.

## 4. Reliability

* **NFR-20:** The system shall maintain consistency between file metadata in PostgreSQL and files stored in external storage.
* **NFR-21:** Failed file-storage operations shall not result in invalid file records being permanently stored in the database.
* **NFR-22:** The cleanup process should safely handle failures without causing data corruption.
* **NFR-23:** The system should continue operating when an individual transfer contains multiple files and messages.

## 5. Availability

* **NFR-24:** The production application should be available whenever the hosting services are operational.
* **NFR-25:** The system should recover gracefully from temporary external-service failures.
* **NFR-26:** The application should provide a health-check mechanism for monitoring its availability.

## 6. Usability

* **NFR-27:** The application shall provide a responsive interface suitable for both mobile and desktop browsers.
* **NFR-28:** Users should be able to create a transfer with minimal steps.
* **NFR-29:** Users should be able to join a transfer using either a code or QR code.
* **NFR-30:** The transfer code shall be clearly displayed and easy to copy.
* **NFR-31:** The QR code shall be clearly displayed and scannable using common mobile devices.
* **NFR-32:** The remaining transfer lifetime shall be clearly visible to users.
* **NFR-33:** The application shall provide understandable feedback for successful and failed operations.

## 7. Maintainability

* **NFR-34:** The backend shall follow a layered architecture separating controllers, services, repositories, and models.
* **NFR-35:** The frontend and backend shall be separated into independent applications.
* **NFR-36:** The application shall use meaningful package, class, method, and variable names.
* **NFR-37:** The project shall use Git for version control.
* **NFR-38:** The project shall maintain documentation for system architecture, requirements, and API endpoints.
* **NFR-39:** The application configuration shall be separated from application source code.

## 8. Scalability

* **NFR-40:** The application architecture should allow the backend to support increasing numbers of concurrent transfers.
* **NFR-41:** File storage should be independently scalable from application and database storage.
* **NFR-42:** The architecture should allow future integration of caching or real-time communication without major restructuring.

## 9. Compatibility

* **NFR-43:** The application shall support modern desktop web browsers.
* **NFR-44:** The application shall support modern mobile web browsers.
* **NFR-45:** The application shall provide a consistent experience across supported screen sizes.

## 10. Data Integrity

* **NFR-46:** Every message shall belong to exactly one transfer.
* **NFR-47:** Every file record shall belong to exactly one transfer.
* **NFR-48:** The system shall maintain referential integrity between transfers and their associated messages and files.
* **NFR-49:** The system shall prevent operations on resources that do not belong to the requested transfer.
