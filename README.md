# probableCausesBackend
Backend of Part IB Group Project Probable Causes

Instead of implementing backend in an external server, I chose to use localhost at the moment for easier testing and debugging.
We can just use Cambridge domain provided for free if we want to put this online.

Sample API - simple CRUD
This is sample API to test and give an example of how Java Spring passes APIs to ReactJS.
This is NOT related to our project.
All data is returned as JSON

GET
/groups
* returns all group objects
/group/{id}
* returns a group with given id

POST
/group
* creates a new group

UPDATE
/group
* updates an existing group

DELETE
/group/{id}
* deletes a group with given id
