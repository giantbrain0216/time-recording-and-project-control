<template>
  <div >
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="projectSelected ? 6 : 12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Project list </h2>
            <div class="float-right mb-1">
              <vs-button @click="activePrompt = true" color="primary" icon="add" type="filled">Add Project</vs-button>
            </div>
            <div class="d-flex align-items-center dropdownbtn-alignment m-2 ml-3 float-left">
              <div>Only see projects from:</div>
              <vs-dropdown class="ml-1">
                <a class="a-icon" href="#">
                  {{ this.selectedClientNameEdit }}
                  <vs-icon class="" icon="expand_more"></vs-icon>
                </a>
                <vs-dropdown-menu>
                  <vs-dropdown-item @click='updateSelectedClientEdit(0,"All Clients")'>
                    All Clients
                  </vs-dropdown-item>
                  <vs-dropdown-item @click="updateSelectedClientEdit(client.clientID,client.name)"
                                    v-for="client in clients" :key="client.clientID"
                  >
                    {{ client.name }}
                  </vs-dropdown-item>
                </vs-dropdown-menu>
              </vs-dropdown>
            </div>
          </div>
          <div class="table-responsive">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0" style="color: cornflowerblue">Name</th>
              <th class="border-top-0" style="color: cornflowerblue">ID of the Client</th>
              <th class="border-top-0" style="color: cornflowerblue">Deadline</th>
              <th class="border-top-0" style="color: cornflowerblue">Progress</th>
              <th class="border-top-0" style="color: cornflowerblue">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="project in projects" :key="project.projectNumber">
              <td>
                <div class="d-flex align-items-center">
                  <div class="">
                    <a @click="updateProjectDetails(project.projectNumber)" class="m-b-0"
                       style="font-weight: bold; font-size: 15px; cursor:pointer">
                      {{ project.projectName }}
                      <b-card style="font-size:10px;" class="text-success"
                              v-if="calculateStatus(project.projectNumber)">running
                      </b-card>
                      <b-card style="font-size:10px;" class="text-secondary"
                              v-else-if="!calculateStatus(project.projectNumber)">cancelled
                      </b-card>

                    </a>
                  </div>
                </div>
              </td>
              <td>{{ project.clientID }}</td>
              <!-- <td>{{ project.projectName }}</td> -->
              <td> {{ project.plannedEnd.substring(0, 10) }}</td>
              <td style="text-align: center">
                <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                </vs-progress>
                <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
              <td>
                <div>
                  <vs-button @click="showDeletePrompt(project.projectNumber) " class="m-1 fa fa-trash" color="danger"
                             icon="delete" type="filled">
                    Delete
                  </vs-button>
                  <vs-button @click="updateProjectID(project.projectNumber)" class=" m-1
                  " color="warning" icon="edit" type="filled">
                    Edit
                  </vs-button>
                  <vs-button class=" m-1" @click="updateeProjectID(project.projectNumber)" color="success" icon="add"
                             type="filled">
                    Assign
                  </vs-button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="projectSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="projectSelected" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off"
                       @click="projectSelected = false"></vs-button>
            <h2>Details vom {{ currentProject.projectName }}</h2>
          </div>
          <div>
            <p><strong>ID of the Client: </strong>{{ currentProject.clientID }}</p>
            <hr>
            <p><strong>Planned Start: </strong>{{ currentProject.plannedStart.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned End: </strong>{{ currentProject.plannedEnd.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned Effort <i>(In Hours)</i>: </strong>{{ currentProject.plannedEffort }}</p>
            <hr>
            <p><strong>Performed Effort <i>(In Hours)</i>: </strong>{{ currentProject.performedEffort }}</p>
            <hr>
            <p><strong>Competences: </strong>{{ currentProject.competences }}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <!--deleteProject(project.projectNumber)-->

      <vs-prompt
          title="Delete Project"
          color="danger"
          @cancel="closeDeletePrompt"
          @accept="deleteProject"
          @close="closeDeletePrompt"
          :is-valid="true"
          :active.sync="activeDeletePrompt"
      >
        <div class="con-exemple-prompt">
          <h4>Are you sure to delete the project :</h4>  <h5>{{ currentProject.projectName }}</h5>
        </div>
      </vs-prompt>


      <vs-prompt
          title="Add New Project"
          color="success"
          @cancel="closeAddForm"
          @accept="addProject"
          @close="closeAddForm"
          :is-valid="validProject(start,end)"
          :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" class="mb-3" v-model="inputValues.projectName"/>
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3">
            <div> Client:</div>
            <vs-dropdown class="ml-1">
              <a class="a-icon" href="#">
                {{ this.selectedClientName }}
                <vs-icon class="" icon="expand_more"></vs-icon>
              </a>
              <vs-dropdown-menu>
                <vs-dropdown-item v-for="client in clients" :key="client.clientID"
                                  @click="updateSelectedClient(client.clientID,client.name)">
                  {{ client.name }}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>


          </div>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="start" name="plannedStart"
                                                v-model="start"
                                                :min="dateToday" max="2030-12-31" required></div>
          <div class="mb-3">
            <small>Planned End</small> <input class="ml-2" type="date" id="end" name="plannedEnd"
                                              v-model="end"
                                              :min="start" max="2030-12-31" required></div>
          <vs-input type="number" label-placeholder="Planned Effort In Hours" class="mb-3"
                    v-model="inputValues.plannedEffortField"/>
          <vs-input disabled="true" type="number" label-placeholder="Performed Effort In Hours : 0" class="mb-3"
          />
          <vs-input label-placeholder="competences" class="mb-3" v-model="inputValues.competencesField"/>
          <vs-alert
              :active="!validProject(start,end)"
              color="danger"
              icon="new_releases"
          >
            All fields must be filled in
          </vs-alert>


        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Project"
          color="danger"
          @cancel="closeEditForm"
          @accept="updateProject"
          @close="closeEditForm"
          :is-valid="validProjectEdit(this.startDatum,this.endDatum)"
          :active.sync="activeEditPromt"
      >
        <h5>Project Name : {{ currentProject.projectName }}</h5>
        <h6 class="mb-2 mt-2">ID of the Client : {{ currentProject.clientID }}</h6>
        <hr>
        <div class="con-exemple-prompt">
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="startedit" name="plannedStartEdit"
                                                v-model="startDatum"
                                                :min="start" max="2030-12-31"></div>
          <div class="mb-3">
            <small>Planned End</small> <input class="ml-2" type="date" id="endedit" name="plannedEndEdit"
                                              v-model="endDatum"
                                              :min="startDatum" max="2030-12-31">
          </div>
          <vs-input type="number" label="Planned Effort" :placeholder="currentProject.plannedEffort" class="mb-3"
                    v-model="editValues.plannedEffortField"/>
          <vs-input disabled="true" type="number" label="Performed Effort" :placeholder="currentProject.performedEffort"
                    class="mb-3"
          />
          <vs-input label="Competences" :placeholder="currentProject.competences" class="mb-3"
                    v-model="editValues.competencesField"/>
          <vs-alert
              :active="!validProjectEdit"
              color="warning"
              icon="new_releases"
          >
            All fields must be filled in
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Assign Employee"
          color="success"
          @cancel="closeAssignPrompt"
          @accept="assignProject"
          @close="closeAssignPrompt"
          :is-valid="validEmployeeAssign"
          :active.sync="activeAssignPropmt"
      >
        <h5>Project Name: <strong style="color: red"> {{ this.currentProject.projectName }}</strong></h5>
        <hr>
        <div class="con-exemple-prompt">
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3">
            <div> Employee:</div>
            <vs-dropdown class="ml-1">
              <a class="a-icon" href="#">
                {{ this.selectedEmployeeName }}
                <vs-icon class="" icon="expand_more"></vs-icon>
              </a>
              <vs-dropdown-menu>
                <vs-dropdown-item v-for="employee in employees" :key="employee.employeeID"
                                  @click="updateSelectedEmployee(employee.employeeID,employee.name)">
                  {{ employee.name }}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>
            <vs-input type="number" label-placeholder="Number of Hours This Week" class="mb-3" v-model="assignHours"/>


          </div>
          <div class="ml-5">
          <ul>
            <li><h6>Remaining Working Hours This Week: <strong style="color: red" >{{ this.currentEmployee.remainingWorkingHoursPerWeek }}</strong></h6></li>
            <li>
              <h6 class="mt-2 mb-2" style="font-weight: bold">Effort to perform: <strong style="color: red" >{{ this.currentProject.plannedEffort - this.currentProject.performedEffort }}</strong></h6></li>
          </ul> </div>
          <table class="table">
            <thead>
            <tr class="">
              <!-- <th class="border-top-0">Assignment ID</th> -->
              <!--<th class="border-top-0">ID</th>-->
              <th class="border-top-0">Employee</th>
              <th class="border-top-0">Planned Hours</th>
              <th class="border-top-0">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="assignment in currentProjectAssignments" :key="assignment.id">
              <!--  <td>{{ assignment.id }}</td>-->
              <!--<td>{{employee.employeeID}}</td>-->
              <td> {{ currentEmployeeName(assignment.employeeID) }}</td>
              <td>{{ assignment.plannedWorkingHours }}</td>
              <td>
                <vs-button icon="delete" @click="updateCurrentAssignment(assignment.id)" class="m-1" color="danger"
                           type="filled">
                </vs-button>
              </td>
            </tr>
            </tbody>
          </table>

          <vs-alert
              :active="!validEmployeeAssign"
              color="danger"
              icon="new_releases"
          >
            You cannot assign an employee more hours than his remaining ones !
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close="closeDeletio"
          @cancel="closeDeletio"
          @accept="deleteAssignment"
          :is-valid="true"
          :active.sync="deleteAssignmentPrompt"
      >
        <div class="con-exemple-prompt">
          <h6>Are you sure you want to delete the next Assignment <br>
            Employee ID: {{ this.currentAssignment.employeeID }}, Project ID :{{ this.currentAssignment.projectID }},
            Planned Working Hours : {{ this.currentAssignment.plannedWorkingHours }}</h6>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "projectList",
  data: () => {
    return {
      projects: [],
      clients: [],
      employees: [],
      currentProjectAssignments: [],
      dateToday: "",
      startDatum: "",
      start: "",
      end: "",
      endDatum: "",
      assignHours: '',
      currentClient: {},
      currentAssignmentID: 0,
      currentAssignment: {},
      currentProject: {},
      currentEmployee: {},
      editProjectID: 0,
      createdClientID: 0,
      selectedClientID: 0,
      selectedEmployeeName: "Employee",
      selectedEmployeeID: 0,
      selectedClientName: "Owner of the project",
      projectSelected: false,
      deleteAssignmentPrompt: false,
      activeAssignPropmt: false,
      activeEditPromt: false,
      activePrompt: false,
      activeDeletePrompt: false,
      inputValues: {
        projectName: "",
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        performedEffortField: "",
        competencesField: ""
      },
      editValues: {
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        performedEffortField: "",
        competencesField: ""
      }
    };
  },

  created() {


    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;
    this.start = yyyy + "-" + mm + '-' + dd;
    this.end = yyyy + "-" + mm + '-' + dd;
    this.fetchAllEmployees();
    this.fetchClients();
    this.fetchAllProjects();


  },


  computed: {

    validEmployeeAssign() {
      return this.currentEmployee.remainingWorkingHoursPerWeek >= this.assignHours &&
          this.currentProject.plannedEffort - this.currentProject.performedEffort >= this.assignHours && this.selectedEmployeeName !== "Employee" && this.assignHours !== "" && this.assignHours > 0
    },



  },

  methods: {

    validProjectEdit(starttime,endttime) {
      return ( this.editValues.plannedEffortField >= this.currentProject.performedEffort && (new Date(starttime).getTime() <= new Date(endttime).getTime())
          && this.editValues.competencesField.length >0 )
    },

    validProject(start,end) {
      return (parseFloat(this.inputValues.plannedEffortField) > 0 && (new Date(start).getTime() <= new Date(end).getTime())
          && this.inputValues.competencesField.length > 0)

    },

    currentEmployeeName(id) {
      for (let i = 0; i < this.employees.length; i++) {
        if (this.employees[i].employeeID === id)
          return this.employees[i].name
      }
    },
    /**
     * Deletes the current assignment
     */
    async deleteAssignment() {

      await axios.delete(`http://localhost:8080/assignments/` + this.currentAssignmentID)


      await this.fetchEmployee(this.currentAssignment.employeeID)
      await this.fetchCurrentProjectAssignments(this.currentAssignment.projectID)
      await axios.put('http://localhost:8080/employees', {
        "employeeID": this.currentEmployee.employeeID,
        "name": this.currentEmployee.name,
        "domicile": this.currentEmployee.domicile,
        "competences": this.currentEmployee.competences,//.
        "workingHoursPerWeek": this.currentEmployee.workingHoursPerWeek,
        "remainingWorkingHoursPerWeek": (this.currentEmployee.remainingWorkingHoursPerWeek +
            this.currentAssignment.plannedWorkingHours)
      })
      await this.fetchAllEmployees()
      await this.fetchAllProjects()      //await this.fetchEmployees()
      // await this.fetchAllAssignments()
      this.alertAssignAlert()
    },

    /**
     * Notifies assignment succeeded
     */
    alertAssignAlert() {
      this.$vs.notify({
        title: 'Deletion',
        text: 'Deletion of the Assignment was successful.',
        color: 'red',
      })
    },

    /**
     * Activates deletion prompt for assignment
     */
    async updateCurrentAssignment(id) {
      this.currentAssignmentID = id
      await axios.get('http://localhost:8080/assignments/' + this.currentAssignmentID).then(response => {
        this.currentAssignment = response.data
      })
      this.activeAssignPropmt = false
      this.deleteAssignmentPrompt = true;
    },

    /**
     * Notifies that deletion succeeded
     */
    closeDeletio() {
      this.$vs.notify({
        title: 'Closed',
        text: 'Deletion was cancelled.',
        color: 'red'
      })
    },

    /**
     * Assigns employee to projects and updates the remaining work time of employee
     */
    assignProject: async function () {
      await axios.put(`http://localhost:8080/employees/`, {
        "employeeID": this.currentEmployee.employeeID,
        "name": this.currentEmployee.name,
        "domicile": this.currentEmployee.domicile,
        "competences": this.currentEmployee.competences,
        "workingHoursPerWeek": this.currentEmployee.workingHoursPerWeek,
        "remainingWorkingHoursPerWeek": parseInt(this.currentEmployee.remainingWorkingHoursPerWeek) - parseInt(this.assignHours),
      })
      await this.fetchEmployee(this.currentEmployee.employeeID)

      await axios.post(`http://localhost:8080/assignments/`, {
        "employeeID": this.currentEmployee.employeeID,
        "projectID": this.currentProject.projectNumber,
        "plannedWorkingHours": this.assignHours
      })
      this.assignHours = 0
      this.selectedEmployeeName = "Employee"
      this.currentEmployee = {}
      await this.fetchAllProjects()
      await this.fetchAllEmployees()
      this.assignProjectAlert()
    },

    /**
     * Gets clients from DB
     */
    fetchClients: async function () {
      await axios.get(`http://localhost:8080/clients`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.clients = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Sets selectedClientID and selectedClientName
     *
     * @param id of client
     * @param name of client
     */
    updateSelectedClient(id, name) {
      this.selectedClientID = id;
      this.selectedClientName = name;
    },

    /**
     * Sets the selectedEmployeeId and selectedEmployeeName
     */
    updateSelectedEmployee(id, name) {
      this.selectedEmployeeID = id;
      this.selectedEmployeeName = name;
      this.fetchEmployee(id)
    },

    /**
     * Gets specific employee from DB
     *
     * @param id of employee
     */
    async fetchEmployee(id) {
      await axios.get(`http://localhost:8080/employees/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentEmployee = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      // this.projectSelected = true

    },

    /**
     * Closes assignment prompt
     */
    closeAssignPrompt() {
      this.activeAssignPropmt = false;
      // this.activeAssignPropmt = true;
      this.cancelAssignAlert()
      this.selectedEmployeeName = "Employee"
      this.currentEmployee = {}
    },

    /**
     *
     */
    updateSelectedClientEdit(id, name) {
      this.selectedClientNameEdit = name;
      if (id == 0) {
        this.fetchAllProjects();
      } else {
        this.fetchProjectsSortedByCustomer(id)
      }


    },

    /**
     * Activates deletion prompt
     */
    showDeletePrompt: function (id) {
      this.fetchProject(id)
      this.activeDeletePrompt = true
    },

    /**
     * Gets planned start without time as String
     */
    plannedStart: function () {
      var startdate = this.currentProject.plannedStart;
      this.startDatum = startdate.substring(0, 10)
    },

    /**
     * Gets the end date of project without time and as String
     */
    plannedEnd: function () {
      var enddate = this.currentProject.plannedEnd;
      this.endDatum = enddate.substring(0, 10)
    },

    /**
     * Closes deletion prompt
     */
    closeDeletePrompt: function () {
      this.activeDeletePrompt = false;
      this.closeDeleteAlert()

    },

    updateProjectDetails(id) {
      this.fetchProject(id)
      this.projectSelected = true
    },

    /**
     * Adds project to the DB and updates the frontend project list.
     */
    addProject: async function () {
      this.inputValues.plannedStartField = this.start + " " + "00:00"
      this.inputValues.plannedEndField = this.end + " " + "00:00"
      await axios.post('http://localhost:8080/projects', {
        "projectName": this.inputValues.projectName,
        "clientID": parseInt(this.selectedClientID),
        "plannedStart":  this.start + " " + "00:00",
        "plannedEnd":  this.end + " " + "00:00" ,
        "plannedEffort": parseInt(this.inputValues.plannedEffortField),
        "performedEffort": 0,
        "competences": this.inputValues.competencesField,
      }).then((result) => {
        this.createdClientID = result.data;
      })
      await this.fetchCustomer(this.selectedClientID)
      await axios.put(`http://localhost:8080/clients/`, {
        'id': this.currentClient.clientID,
        'name': this.currentClient.name,
        'email': this.currentClient.email,
        'telephoneNumber': this.currentClient.telephoneNumber,
        'contactPersonID': this.currentClient.contactPersonID,
        'projectIDs': this.currentClient.projectIDs + "\t" + this.createdClientID
      })
      this.acceptAlert();
      await this.fetchAllProjects()
      this.inputValues.competencesField = ""
      this.inputValues.plannedEndField = this.dateToday
      this.inputValues.plannedStartField = this.dateToday
      this.inputValues.plannedEffortField = ""
      this.selectedClientName = "Owner of the project"
      this.inputValues.projectName = ""

    },

    /**
     * Gets specific client from DB
     *
     * @param id of client
     */
    fetchCustomer: async function (id) {
      await axios.get(`http://localhost:8080/clients/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            console.log(response.data)
            this.currentClient = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Updates project in the DB
     */
    updateProject: async function () {
      var dateControlEdit = document.querySelector('input[id="startedit"]');
      var startdateedit = dateControlEdit.value;
      var timeControlEdit = document.querySelector('input[id="endedit"]');
      var enddateedit = timeControlEdit.value;
      this.editValues.plannedStartField = startdateedit + " " + "00:00";
      this.editValues.plannedEndField = enddateedit + " " + "00:00";
      await axios.put(`http://localhost:8080/projects`, {
        "projectNumber": this.editProjectID,
        "projectName": this.currentProject.projectName,
        "clientID": this.currentProject.clientID,
        "plannedStart": startdateedit + " " + "00:00",
        "plannedEnd": enddateedit + " " + "00:00",
        "plannedEffort": parseInt(this.editValues.plannedEffortField),
        "performedEffort": this.currentProject.performedEffort,
        "competences": this.editValues.competencesField,
      })
      this.editAlert()
      await this.fetchAllProjects();
    },

    /**
     * Gets project assignments and project for editing.
     * Activates assignment prompt.
     */
    updateeProjectID: async function (id) {
      await this.fetchCurrentProjectAssignments(id)
      await this.fetchProject(id);
      this.activeAssignPropmt = true

    },

    /**
     * Gets assignment of currentProject
     *
     * @param id of assignmentByProject
     */
    async fetchCurrentProjectAssignments(id) {
      await axios.get(`http://localhost:8080/assignmentsbyproject/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentProjectAssignments = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Sets update prompt with the currentProject data
     */
    updateProjectID: async function (id) {
      await this.fetchProject(id);
      this.activeEditPromt = true
      this.editProjectID = id;
      this.editValues.clientIDField = this.currentProject.clientID
      this.editValues.competencesField = this.currentProject.competences
      this.editValues.performedEffortField = this.currentProject.performedEffort
      this.editValues.plannedEndField = this.currentProject.plannedEnd
      this.editValues.plannedStartField = this.currentProject.plannedStart
      this.editValues.plannedEffortField = this.currentProject.plannedEffort
      this.plannedStart()
      this.plannedEnd()
    },

    /**
     * Deletes the currentProject from DB
     */
    deleteProject: async function () {
      this.activeDeletePrompt = false;
      await axios.delete(`http://localhost:8080/projects/` + this.currentProject.projectNumber);
      await this.fetchCustomer(this.currentProject.clientID)
      await axios.put(`http://localhost:8080/clients/`, {
        'id': this.currentClient.clientID,
        'name': this.currentClient.name,
        'email': this.currentClient.email,
        'telephoneNumber': this.currentClient.telephoneNumber,
        'contactPersonID': this.currentClient.contactPersonID,
        'projectIDs': this.currentClient.projectIDs.replace(this.currentProject.projectNumber.toString(), "") //TODO : HOW TO GET THE ID OF THE PROJECT TO BE ADDED
      })
      this.deleteAlert()
      await this.fetchAllProjects();
    },

    /**
     * Gets all projects from DB
     */
    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/projects/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Gets all employees from DB
     */
    fetchAllEmployees: async function () {
      await axios.get(`http://localhost:8080/employees/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.employees = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Notifies that addition succeeded
     */
    acceptAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Project has been successfully added.'
      })
    },

    /**
     * Notifies that deletion succeeded
     */
    deleteAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Project has been successfully deleted.'
      })
    },

    /**
     * Notifies that assignment was closed
     */
    cancelAssignAlert() {
      this.$vs.notify({
        title: 'Cancel:',
        color: "warning", type: "flat",
        text: 'Assignment has been cancelled.'
      })
    },

    /**
     * Notifies that assignment succeeded
     */
    assignProjectAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        color: "success", type: "flat",
        text: 'Assignment has been successfully performed.'
      })
    },

    /**
     * Notifies that updating project succeeded
     */
    editAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        color: "success", type: "flat",
        text: 'Project has been successfully edited.'
      })
    },

    /**
     * Notifies that deletion was closed
     */
    closeDeleteAlert() {
      this.$vs.notify({
        title: 'Cancelled:',
        color: "rgb(187, 138, 200)", type: "gradient",
        text: 'Project has not been deleted.'
      })
    },

    /**
     * Notifies that addition prompt was closed and sets the inputValues to ''
     */
    closeAddForm() {
      this.inputValues.clientIDField = "";
      this.inputValues.projectName = "";
      this.inputValues.plannedStartField = "";
      this.inputValues.plannedEndField = "";
      this.inputValues.plannedEffortField = "";
      this.inputValues.performedEffortField = "";
      this.inputValues.competencesField = "";
      this.$vs.notify({
        title: 'Closed',
        color: "warning",
        text: 'Add was cancelled successfully.'
      })
    },

    /**
     * Notifies that updating prompt for project war closed ans sets the editValues to ''
     */
    closeEditForm() {
      this.editValues.clientIDField = "";
      this.editValues.plannedStartField = "";
      this.editValues.plannedEndField = "";
      this.editValues.plannedEffortField = "";
      this.editValues.performedEffortField = "";
      this.editValues.competencesField = "";
      this.$vs.notify({
        title: 'Closed',
        text: 'Edit was cancelled successfully.'
      })
    },

    /**
     * Gets a specific project from DB
     *
     * @param id of project
     */
    fetchProject: async function (id) {
      await axios.get(`http://localhost:8080/projects/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentProject = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Gets all projects of a specific client
     *
     * @param id of client
     */
    async fetchProjectsSortedByCustomer(id) {
      await axios.get(`http://localhost:8080/projects`)
          .then(response => {
            var array = []
            for (var i = 0; i < response.data.length; i++) {
              if (response.data[i].clientID == id) {
                array.push(response.data[i])
              }
            }
            this.projects = array
          })
          .catch(e => {
            this.errors.push(e)
          })

    },

    /**
     * Checks if deadline of given project exceeded
     *
     * @param id project deadline to be checked
     */
    calculateStatus: function (id) {
      let today = new Date()

      for (var i = 0; i < this.projects.length; i++) {
        if (this.projects[i].projectNumber === id) {
          let deadline = new Date(this.projects[i].plannedEnd)
          if (today.getTime() > deadline.getTime()) {
            // eslint-disable-next-line no-console
            console.log("Returning false")
            return false;
          } else {
            // eslint-disable-next-line no-console
            console.log("Returning true")
            return true;
          }
        }
      }
    },
  }
}
</script>

<style>
</style>