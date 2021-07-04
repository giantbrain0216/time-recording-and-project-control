# WebApp for effort recording and project control

> The web app is used for tracking the employees in order to be able to better track the efforts made. In addition, the app is used for project control so that team leaders can deploy their employees optimally.

## Installation and Start

**Run de.unistuttgart.sopra.Application**
```
# Install server
- ./mvnw clean install
# Run server
- ./mvnw spring-boot:run
# install dependencies
- npm install
# if you have vulnerabilities
- npm audit fix --force
# then run
- npm run serve
```

## Use of the application

_Description of the main implemented use cases_

### Important use case 1  
- **Add customer:** It is possible to add a customer to the WebApp. This is done via a button in the **Client View**. This button opens a pop up window, which has input fields to be able to create a customer. This will then be stored in the database. 
    
### Important use case 2  
- **Delete a customer:** In the **Client View** there is a **delete** button next to each customer. which allows to delete a customer. This customer will then be removed from the database.  
  
### Important use case 3  
- **Editing a customer:** In the **Client View** there is an **edit** button next to each customer,  which allows to change the customer data afterwards. The changes are saved in the database.  
  
### Important use case 4  
- **Inserting an employee:** In the **Employee View** it is possible to add a customer with his available available working time. This is done via a button in the **Employee View**. This opens a pop up window, which has input fields to create an employee. This employee will then be stored in the database.  
  
### Important use case 5  
- **Edit employee:** In the **Employee View** it is possible to edit an employee afterwards. For this purpose there is a button next to each employee, which opens a pop up window with input fields needed for editing. needed. The changes are saved in the database.  
  
### Important use case 6  
- **Delete employee:** In the **Employee View** it is possible to delete an employee. This is done via a delete button that is positioned next to each employee. The employee will no longer exist in the database.  

### Important use case 7  
- **Add project:** In the **Project View** it is possible to create a project. This is done via a button that opens a pop up window with given input fields that are needed to store the attributes correctly in the database. The project will then be saved in the database.  
  
### Important use case 8
- **Edit project:** In the **Project View** it is possible to edit projects afterwards. This is done via an Edit button. This opens a pop up window, which allows to change the project data. The changes are saved in the database. 

### Important use case 9  
- **Delete project:** In the **Project View** it is possible to delete a project. This is done via a delete button that can be found next to each project. The project does not exist in the database no more.  

### Important use case 10
- **View of all projects:** The projects are automatically displayed in the **Project View**. The projects must be stored in the database. With a mouse click on the button ***Assign*** you can assign and/or remove employees to the project. A detailed view of the project is displayed with all data of the project. In this view, the missing competences are displayed in red, while the covered ones are displayed in green!


### Important use case 11
- **Assignment of employees to the projects** : In the **Project List View** there is a button ***Assign*** next to each project. With this button you can assign and/or remove employees to the project.

### Important use case 11  
- **View all customers:** In the **Client View** all customers are automatically displayed, which are stored in the database. With a mouse click on the name of the customer, a detailed view of the selected **customer** appears. There one can see the most important information as well as the address of the customer on a map fetched from [OpenStreetMap](https://www.openstreetmap.org/).


### Important use case 12
- **Create invoice for a customer** : In the **Client View** there will be a button ***Invoice*** in front of each customer. If you press it, a pop up window will appear where you can enter the project (searchbar with autocomplete) and the period. A CSV file is downloaded when you press accept. It contains all time registrations and the total.
  
### Important use case 13
-  **View of all employees:** In the **Employee View** all employees are automatically displayed. With a mouse click on an employee name, a detailed view of the employee appears, as well as a view to edit the assignment of the projects. projects. see feature **Assign employees to a project**.  
  
### Important use case 14
- **Time registration of an employee:** In the **Time registration View** it is possible to record working times of an employee, which is already stored in the database. The recorded time is then displayed in the detailed view of the employee in the form of a chart. in the form of a chart, but only for the last 5 days.

### Important use case 15 
- **Time registration from CSV file** : In the **Time registration view** there is a button labeled : ***Import Time Registration From CSV File***. If you click on it, you will land in a new view. On the view there is a ***CSV file template*** available, which can be downloaded with a mouse click. You can upload the CSV file and then the time registrations will be displayed in a preview table, where you can delete or undo them and after pressing the Submit button the respective time registrations will be saved in the database.
