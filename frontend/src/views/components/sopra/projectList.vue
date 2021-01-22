<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="prompts.activeProjectDetailWindow ? 6 : 12"
              vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Project List </h2>
            <div class="float-right mb-1">
              <vs-button @click="prompts.activeAddPrompt = true" color="primary" icon="add" type="filled">Add New
                Project
              </vs-button>
            </div>
            <div class="d-flex align-items-center dropdownbtn-alignment m-2 ml-3 float-left">
              <div>Only see projects from:</div>
              <vs-dropdown class="ml-1">
                <a class="a-icon" href="#">
                  {{ this.selectedClientNameEdit }}
                  <vs-icon class="" icon="expand_more"></vs-icon>
                </a>
                <vs-dropdown-menu>
                  <vs-dropdown-item @click='updateClientForFiltering(0,"All Clients")'>
                    All Clients
                  </vs-dropdown-item>
                  <vs-dropdown-item @click="updateClientForFiltering(client.clientID,client.name)"
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
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Name of the Client</th>
                <th class="border-top-0" style="color: cornflowerblue">Deadline</th>
                <th class="border-top-0" style="color: cornflowerblue">Progress</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="project in projects" :key="project.projectNumber">
                <td>{{ project.projectNumber }}</td>
                <td>
                  <div class="d-flex align-items-center">
                    <div class="">
                      <a @click="fetchProject(project.projectNumber);prompts.activeProjectDetailWindow = true"
                         class="m-b-0"
                         style="font-weight: bold; font-size: 15px; cursor:pointer">
                        {{ project.projectName }}
                        <b-card style="font-size:10px;" class="text-success"
                                v-if="calculateStatus(project.projectNumber)">running
                        </b-card>
                        <b-card style="font-size:10px;" class="text-secondary"
                                v-else-if="!calculateStatus(project.projectNumber)">finished
                        </b-card>

                      </a>
                    </div>
                  </div>
                </td>
                <td> <i><strong>{{ "" + clientsNamesForProjects[project.projectNumber] }}</strong></i></td>
                <!-- <td>{{ project.projectName }}</td> -->
                <td> {{ project.plannedEnd.substring(0, 10) }}</td>
                <td style="text-align: center">
                  <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                  </vs-progress>
                  <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
                <td>
                  <div>
                    <vs-button style="width:80px" @click="fetchProject(project.projectNumber);prompts.activeDeletePrompt=true "
                               class="m-1 btn-bloc" color="danger"
                               icon="delete" type="filled">
                      Delete

                    </vs-button>
                    <vs-button style="width:80px" @click="updateProjectIDBeforeEdit(project.projectNumber)" class=" m-1 btn-bloc
                  " color="warning" icon="edit" type="filled">
                      Edit
                    </vs-button>
                    <vs-button style="width:80px" class=" m-1 btn-bloc"
                               @click="fetchCurrentProjectAssignments(project.projectNumber);fetchProject(project.projectNumber);prompts.activeAssignPrompt = true"
                               color="success" icon="add"
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
      <vs-col v-if="prompts.activeProjectDetailWindow" type="flex" vs-justify="center" vs-align="center" vs-sm="6"
              vs-lg="6" vs-xs="12">
        <vs-card v-show="prompts.activeProjectDetailWindow" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off"
                       @click="prompts.activeProjectDetailWindow = false"></vs-button>
            <h2>Details of <strong style="color: red"> {{ currentProject.projectName }} </strong></h2>
          </div>
          <div>
            <p><strong>Name of the Client: </strong>{{
                "" + clientsNamesForProjects[currentProject.projectNumber] + " (ID:" + currentProject.clientID + ")"
              }}</p>
            <hr>
            <p><strong>Price Per Hour: </strong> <strong style="color:#28a745;"> {{ currentProject.pricePerHour }} €</strong></p>
            <hr>
            <p><strong>Planned Start: </strong>{{ currentProject.plannedStart.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned End: </strong>{{ currentProject.plannedEnd.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned Effort <i>(In Hours)</i>: </strong>{{ currentProject.plannedEffort }}</p>
            <hr>
            <p><strong>Performed Effort <i>(In Hours)</i>: </strong>{{ currentProject.performedEffort }}</p>
            <hr>
            <vs-list>
              <vs-list-header icon="build" title="Competences"></vs-list-header>
              <vs-list-item :style='competencesAbgedeckt[competence.id] ? "color:#46c93a" : "color:#ff4757"'
                            v-for="competence in currentProject.competences"
                            :icon='competencesAbgedeckt[competence.id] ? "check" : "error"'
                            :title="competence.name" :key="competence.id"></vs-list-item>
            </vs-list>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <!--deleteProject(project.projectNumber)-->

      <vs-prompt
          title="Delete Project"
          color="danger"
          @cancel='prompts.activeDeletePrompt = false;notify("Cancelled","Project has not been deleted.","danger")'
          @accept="deleteProject"
          @close='prompts.activeDeletePrompt = false;notify("Cancelled","Project has not been deleted.","danger")'
          :is-valid="true"
          :active.sync="prompts.activeDeletePrompt"
      >
        <div class="con-exemple-prompt">
          <h4>Are you sure to delete the project :</h4>  <h5>{{ currentProject.projectName }}</h5>
        </div>
      </vs-prompt>


      <vs-prompt
          title="Add New Project"
          color="success"
          @cancel='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          @accept="addProject"
          @close='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          :is-valid="validProjectAdd"
          :active.sync="prompts.activeAddPrompt"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" class="mb-3" v-model="inputValues.projectName"/>
          <hr>
          <autocomplete
              ref="textSearchOfClientAdd"
              :search="filterClientItemsAdd"
              :get-result-value="getClientResultValue"
              @submit="handleClientSubmitAdd"
              placeholder="Search for a client"
              aria-label="Search for a client"
              auto-select
          ></autocomplete>
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
            <div>Selected Client:</div>
            <div class="ml-1" style="color:royalblue;">{{ currentClient.name }}</div>
          </div>
          <hr>
          <div class="mb-3" style="width: 55%">
            Planned Start  <input style="width: 60%" class="float-right" type="date" id="start" name="plannedStart"
                                                v-model="inputValues.plannedStartField"
                                                :min="dateToday" max="2030-12-31" required></div>
          <div class="mb-3" style="width: 55%">
            Planned End  <input style="width: 60%" class="float-right" type="date" id="end" name="plannedEnd"
                                              v-model="inputValues.plannedEndField"
                                              :min="inputValues.plannedStartField" max="2030-12-31" required></div>
          <hr>
          <vs-input type="number" label-placeholder="Planned Effort In Hours" class="mb-4"
                    v-model="inputValues.plannedEffortField"/>
          <vs-input type="number" label-placeholder="Price Per Hour : " class="mt-3" v-model="inputValues.pricePerHour"
          />
          <vs-input disabled="true" type="number" label-placeholder="Performed Effort In Hours : 0" class="mb-3"
          />
          <hr>
          <autocomplete
              ref="textSearchOfCompetencesAdd"
              :search="filterCompetenceItemsAdd"
              :get-result-value="getCompetenceResultValue"
              @submit="handleCompetenceSubmitAdd"
              placeholder="Search for a competence"
              aria-label="Search for a competence"
              auto-select
          ></autocomplete>
          <!--          <ul class="list-group mt-2 mb-2" >-->
          <!--            <li v-for="competence in inputValues.selectedCompetences" :key="competence.id" class="list-group-item" style="border-radius:3px !important; width:80%"><div class="float-left">{{ competence.name }}</div><button style="border-radius:3px" type="button" class="btn btn-danger float-right">Remove</button></li>-->
          <!--          </ul>-->
          <div class="mt-3 mb-3">
            <vs-checkbox v-for="competence in inputValues.selectedCompetences" :key="competence.id"
                         class="justify-content-start mt-2"
                         v-model="inputValues.tickBoxesForCompetences[competence.id]">{{ competence.name }}
            </vs-checkbox>
          </div>

          <vs-alert
              :active="!validProjectAdd"
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
          @cancel='resetAllValues();notify("Closed","Edit was cancelled successfully.","warning")'
          @accept="updateProject"
          @close='resetAllValues();notify("Closed","Edit was cancelled successfully.","warning")'
          :is-valid="validProjectEdit"
          :active.sync="prompts.activeEditPrompt"
      >
        <h5>Project Name : {{ currentProject.projectName }}</h5>
        <h6 class="mb-2 mt-2">ID of the Client : {{ currentProject.clientID }}</h6>
        <h6 class="mb-2 mt-2" >Price Per Hour : <strong style="color: red"> {{ currentProject.pricePerHour }} € </strong></h6>

        <hr>
        <div class="con-exemple-prompt">
          <div class="mb-3">
            Planned Start <input class="ml-2" type="date" id="startedit" name="plannedStartEdit"
                                                v-model="editValues.plannedStartField"
                                                :min="editValues.plannedStartField" max="2030-12-31"></div>
          <div class="mb-3">
            Planned End <input class="ml-2" type="date" id="endedit" name="plannedEndEdit"
                                              v-model="editValues.plannedEndField"
                                              :min="editValues.plannedStartField" max="2030-12-31">
          </div>
          <div class="centerx">
            <vs-input-number :placeholder="currentProject.plannedEffort" class="mb-3" :min="currentProject.performedEffort"
                             v-model="editValues.plannedEffortField" label="Planned Effort In Hours:"/>
          </div>
          <h6 class="mb-2 mt-2" >Performed Effort : <strong style="color: red"> {{ currentProject.performedEffort }} Hours </strong></h6>

          <autocomplete
              ref="textSearchOfCompetencesEdit"
              :search="filterCompetenceItemsEdit"
              :get-result-value="getCompetenceResultValue"
              @submit="handleCompetenceSubmitEdit"
              placeholder="Search for a competence"
              aria-label="Search for a competence"
              auto-select
          ></autocomplete>

          <div class="mt-3 mb-3">
            <vs-checkbox v-for="competence in editValues.selectedCompetences" :key="competence.id"
                         class="justify-content-start mt-2" v-model="editValues.tickBoxesForCompetences[competence.id]">
              {{ competence.name }}
            </vs-checkbox>
          </div>

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
          @cancel='prompts.activeAssignPrompt = false;notify("Cancel","Assignment has been cancelled.","warning");resetAllValues()'
          @accept="assignProject"
          @close='prompts.activeAssignPrompt = false;notify("Cancel","Assignment has been cancelled.","warning");resetAllValues()'
          :is-valid="validEmployeeAssign"
          :active.sync="prompts.activeAssignPrompt"
      >
        <h5>Project Name: <strong style="color: red"> {{ this.currentProject.projectName }}</strong></h5>
        <hr>
        <div class="con-exemple-prompt">
          <autocomplete
              ref="textSearchOfEmployeeAdd"
              :search="filterEmployeeItemsAdd"
              :get-result-value="getEmployeeResultValue"
              @submit="handleEmployeeSubmitAdd"
              placeholder="Search for a employee"
              aria-label="Search for a employee"
              auto-select
          ></autocomplete>
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
            <div>Selected Employee:</div>
            <a class="a-icon ml-1 " href="#">
              {{ this.currentEmployee.name }}
            </a>
          </div>
          <hr>
          <vs-input type="number" label-placeholder="Number of Hours Per Week" class="mt-4" v-model="assignHours"/>
          <hr>
          <div class="ml-5">
            <ul>
              <li><h6>Remaining Working Hours per Week: <strong
                  style="color: red">{{ this.currentEmployee.remainingWorkingHoursPerWeek }}</strong></h6></li>
              <li>
                <h6 class="mt-2 mb-2" style="font-weight: bold">Effort to perform: <strong style="color: red">{{
                    Math.round(this.currentProject.plannedEffort - this.currentProject.performedEffort)
                  }}</strong> Hours</h6></li>
            </ul>
          </div>
          <hr>
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
              <td> {{ getEmployeeName(assignment.employeeID) }}</td>
              <td>{{ assignment.plannedWorkingHours }}</td>
              <td>
                <vs-button icon="delete" @click="showDeletePromptAssignmentAndSetCurrentAssignment(assignment.id)"
                           class="m-1" color="danger"
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
          color="danger"
          @close='notify("Closed","Deletion was cancelled.","danger")'
          @cancel='notify("Closed","Deletion was cancelled.","danger")'
          @accept="deleteAssignment"
          :is-valid="true"
          :active.sync="prompts.deleteAssignmentPrompt"
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
      competences: [],
      competencesAbgedeckt: {},
      clientsNamesForProjects: {},
      clients: [],
      employees: [],
      currentProjectAssignments: [],
      dateToday: "",
      assignHours: '',
      currentClient: {name: "None"},
      currentAssignment: {},
      currentProject: {},
      currentEmployee: {name: "Employee"},
      prompts: {
        activeProjectDetailWindow: false,
        deleteAssignmentPrompt: false,
        activeAssignPrompt: false,
        activeEditPrompt: false,
        activeAddPrompt: false,
        activeDeletePrompt: false,
      },
      inputValues: {
        projectName: "",
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        pricePerHour: "",
        selectedCompetences: [],
        tickBoxesForCompetences: {}
      },
      editValues: {
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        performedEffortField: "",
        selectedCompetences: [],
        tickBoxesForCompetences: {}
      },
      selectedClientNameEdit: "",
    };
  },

  async created() {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;
    this.inputValues.plannedStartField = this.dateToday
    this.inputValues.plannedEndField = this.dateToday
    this.editValues.plannedStartField = this.dateToday
    this.editValues.plannedEndField = this.dateToday
    await this.fetchAllProjects();
    await this.fetchNameOfClientForProjects()
    this.fetchAllEmployees();
    this.fetchClients();
    this.fetchAllCompetences();


  },


  computed: {
    /**Checks if Assign of employee is valid*/
    validEmployeeAssign() {
      return this.currentEmployee.remainingWorkingHoursPerWeek >= this.assignHours &&
          this.currentProject.plannedEffort - this.currentProject.performedEffort >= this.assignHours && this.currentEmployee.name !== "Employee" && this.assignHours !== "" && this.assignHours > 0
    },

    validProjectAdd() {
      return (parseFloat(this.inputValues.plannedEffortField) > 0 && parseFloat(this.inputValues.pricePerHour) > 0 && (new Date(this.inputValues.plannedStartField).getTime() <= new Date(this.inputValues.plannedEndField).getTime()) && this.inputValues.projectName !== '' && this.currentClient.name !== 'None')
    },

    validProjectEdit() {
      return (this.editValues.plannedEffortField >= this.currentProject.performedEffort && (new Date(this.editValues.plannedStartField).getTime() <= new Date(this.editValues.plannedEndField).getTime()))
    }

  },

  methods: {


    /**Filters items for searchbar of competences on add form*/
    async filterCompetenceItemsAdd(input) {

      if (input.length < 1) {
        return []
      }

      return this.competences.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()) && !Object.keys(this.inputValues.tickBoxesForCompetences).includes(competence.id.toString()))
      })
    },
    /**Returns name of the competence objects*/
    getCompetenceResultValue(result) {
      return result.name
    },
    /**Handle function when competence is selected by searchbar add form*/
    handleCompetenceSubmitAdd(result) {
      this.inputValues.selectedCompetences.push(result)
      this.inputValues.tickBoxesForCompetences[result.id] = true
      this.$refs.textSearchOfCompetencesAdd.value = ""

    },
    /**Filters items for searchbar of competences on edit form*/
    async filterCompetenceItemsEdit(input) {

      if (input.length < 1) {
        return []
      }

      return this.competences.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()) && !Object.keys(this.editValues.tickBoxesForCompetences).includes(competence.id.toString()))
      })
    },
    /**Handle function when competence is selected by searchbar edit form*/
    handleCompetenceSubmitEdit(result) {
      this.editValues.selectedCompetences.push(result)
      this.editValues.tickBoxesForCompetences[result.id] = true
      this.$refs.textSearchOfCompetencesEdit.value = ""

    },


    /**Filters items for searchbar of clients on add form*/
    async filterClientItemsAdd(input) {

      if (input.length < 1) {
        return []
      }

      return this.clients.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()))
      })
    },
    /**Returns name of the client objects*/
    getClientResultValue(result) {
      return result.name
    },
    /**Handle function when client is selected by searchbar edit form*/
    handleClientSubmitAdd(result) {
      this.currentClient = result
      this.$refs.textSearchOfClientAdd.value = ""

    },

    /**Filters items for searchbar of employees on add form*/
    async filterEmployeeItemsAdd(input) {

      if (input.length < 1) {
        return []
      }

      return this.employees.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()) && !this.currentProjectAssignments.map(x => x.employeeID).includes(competence.employeeID))
      })
    },
    /**Returns name of the employee objects*/
    getEmployeeResultValue(result) {
      return result.name
    },
    /**Handle function when employee is selected by searchbar add form*/
    handleEmployeeSubmitAdd(result) {
      this.currentEmployee = result
      this.$refs.textSearchOfEmployeeAdd.value = ""

    },


    /**Checks for add and edit prompt if time input is correct*/
    validProjectTimes(start, finish, editPrompt) {
      if (editPrompt == true) {
        return (this.editValues.plannedEffortField >= this.currentProject.performedEffort && (new Date(start).getTime() <= new Date(finish).getTime())
            && this.editValues.competencesField.length > 0)
      } else {
        return (parseFloat(this.inputValues.plannedEffortField) > 0 && (new Date(start).getTime() <= new Date(finish).getTime())
            && this.inputValues.competencesField.length > 0)
      }

    },

    /**Gets the name of project with given id*/
    getEmployeeName(id) {
      for (let i = 0; i < this.employees.length; i++) {
        if (this.employees[i].employeeID === id)
          return this.employees[i].name
      }
    },

    /**
     * Deletes the current assignment
     */
    async deleteAssignment() {

      await axios.delete(`http://localhost:8080/assignments/` + this.currentAssignment.id).then(async () => {
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
        }).then(async () => {
          //await this.fetchEmployees()
          // await this.fetchAllAssignments()

          this.notify("Deletion", "Deletion of the Assignment was successful.", "danger")
        }).catch((error) => {
          if (error.response) {
            this.notify("Delete Assignment Error", error.message, "danger")
          }
        })
      }).catch((error) => {
        if (error.response) {
          this.notify("Delete Assignment Error", error.message, "danger")
        }
      })
      this.resetAllValues()
      await this.fetchAllEmployees()
      await this.fetchAllProjects()

    },

    /**
     * Activates deletion prompt for assignment
     */
    async showDeletePromptAssignmentAndSetCurrentAssignment(id) {
      await axios.get('http://localhost:8080/assignments/' + id).then(response => {
        this.currentAssignment = response.data
      }).catch((error) => {
        if (error.response) {
          this.notify("Assignment Database Error", error.message, "danger")
        }
      })
      this.prompts.activeAssignPrompt = false
      this.prompts.deleteAssignmentPrompt = true;
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
      }).then(async () => {
        await axios.post(`http://localhost:8080/assignments/`, {
          "employeeID": this.currentEmployee.employeeID,
          "projectID": this.currentProject.projectNumber,
          "plannedWorkingHours": this.assignHours
        }).then(async () => {
          this.notify("Confirmation", "Assignment has been successfully performed.", "success")
        }).catch((error) => {
          if (error.response) {
            this.notify("Add Assignment Error", error.message, "danger")
          }
        })
      }).catch((error) => {
        if (error.response) {
          this.notify("Add Assignment Error", error.message, "danger")
        }
      })


      this.resetAllValues();
      await this.fetchAllProjects()
      await this.fetchAllEmployees()

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
          .catch((error) => {
            if (error.response) {
              this.notify("Clients Database Error", error.message, "danger")
            } else {
              this.notify("Clients Database Error", "Connection to Database Error", "danger")
            }
          })
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
          .catch((error) => {
            if (error.response) {
              this.notify("Employee Database Error", error.message, "danger")
            }
          })
      // this.projectSelected = true

    },

    /**
     *
     */
    updateClientForFiltering(id, name) {
      this.selectedClientNameEdit = name;
      if (id == 0) {
        this.fetchAllProjects();
      } else {
        this.fetchProjectsSortedByCustomer(id)
      }


    },

    /**
     * Adds project to the DB and updates the frontend project list.
     */
    addProject: async function () {
      await axios.post('http://localhost:8080/projects', {
        "projectName": this.inputValues.projectName,
        "pricePerHour": this.inputValues.pricePerHour,
        "clientID": parseInt(this.currentClient.clientID),
        "plannedStart": this.inputValues.plannedStartField + " " + "00:00",
        "plannedEnd": this.inputValues.plannedEndField + " " + "00:00",
        "plannedEffort": parseInt(this.inputValues.plannedEffortField),
        "performedEffort": 0,
        "competences": this.inputValues.competencesField,
      }).then(async (result) => {

        await axios.post(`http://localhost:8080/assignedProjectsClient`, {
          'clientID': this.currentClient.clientID,
          'projectID': result.data,
        }).then(() => {
          this.notify("Confirmation", "Project has been successfully added.", "success")

        }).catch((error) => {
          if (error.response) {
            this.notify("Error", error.message, "danger");
          }
        })

        var arr = Object.keys(this.inputValues.tickBoxesForCompetences)

        for (var i = 0; i < arr.length; i++) {
          if (this.inputValues.tickBoxesForCompetences[arr[i]]) {
            // eslint-disable-next-line no-console
            await axios.post(`http://localhost:8080/assignedCompetencesProject`, {
              'projectID': result.data,
              'competenceID': arr[i],
            }).catch((error) => {
              if (error.response) {
                this.notify("Error", error.message, "danger");
              }
            })
          }
        }


      }).catch((error) => {
        if (error.response) {
          this.notify("Error", error.message, "danger");
        }
      });

      await this.fetchAllProjects()
      await this.fetchNameOfClientForProjects()
      this.resetAllValues()

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
          .catch((error) => {
            if (error.response) {
              this.notify("Client Database Error", error.message, "danger")
            }
          })
    },

    /**
     * Updates project in the DB
     */
    updateProject: async function () {
      await axios.put(`http://localhost:8080/projects`, {
        "projectNumber": this.currentProject.projectNumber,
        "projectName": this.currentProject.projectName,
        "clientID": this.currentProject.clientID,
        "plannedStart": this.editValues.plannedStartField + " " + "00:00",
        "plannedEnd": this.editValues.plannedEndField + " " + "00:00",
        "plannedEffort": parseInt(this.editValues.plannedEffortField),
        "performedEffort": this.currentProject.performedEffort,
        "pricePerHour": this.currentProject.pricePerHour,
      }).then(() => {
        this.notify("Confirmation", "Project has been successfully edited.", "success")

      }).catch((error) => {
        if (error.response) {
          this.notify("Edit Error", error.message, "danger")
        }
      })

      var currentCompetences = []
      await axios.get(`http://localhost:8080/competencesByProject/` + this.currentProject.projectNumber)
          .then(response => {
            // JSON responses are automatically parsed.
            currentCompetences = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Competences for Projects Database Error", error.message, "danger")
            }
          })

      var arr = Object.keys(this.editValues.tickBoxesForCompetences)


      for (var i = 0; i < arr.length; i++) {
        if (this.editValues.tickBoxesForCompetences[parseInt(arr[i])]) {
          if (!currentCompetences.includes(parseInt(arr[i]))) {

            await axios.post(`http://localhost:8080/assignedCompetencesProject`, {
              'projectID': this.currentProject.projectNumber,
              'competenceID': parseInt(arr[i]),
            }).catch((error) => {
              if (error.response) {
                this.notify("Error", error.message, "danger");
              }
            })

          }
        } else {
          if (currentCompetences.includes(parseInt(arr[i]))) {
            await axios.get(`http://localhost:8080/assignedCompetencesProject`).then(async response => {
              for (var j = 0; j < response.data.length; j++) {
                if (response.data[j].projectID == this.currentProject.projectNumber && response.data[j].competenceID == parseInt(arr[i])) {
                  await axios.delete(`http://localhost:8080/assignedCompetencesProject/` + response.data[j].id).catch((error) => {
                    if (error.response) {
                      this.notify("Error", error.message, "danger");
                    }
                  })
                }
              }
            })


          }
        }
      }

      await this.fetchAllProjects();
      await this.resetAllValues();
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
          .catch((error) => {
            if (error.response) {
              this.notify("Assignments Database Error", error.message, "danger")
            }
          })
    },

    /**
     * Sets update prompt with the currentProject data
     */
    updateProjectIDBeforeEdit: async function (id) {
      await this.fetchProject(id);
      this.prompts.activeEditPrompt = true
      this.editValues.clientIDField = this.currentProject.clientID
      this.editValues.competencesField = this.currentProject.competences
      this.editValues.performedEffortField = this.currentProject.performedEffort
      this.editValues.plannedEndField = this.currentProject.plannedEnd.substring(0, 10)
      this.editValues.plannedStartField = this.currentProject.plannedStart.substring(0, 10)
      this.editValues.plannedEffortField = this.currentProject.plannedEffort

      await axios.get(`http://localhost:8080/competencesByProject/${id}`)
          .then(async response => {
            for (var i = 0; i < response.data.length; i++) {

              await axios.get(`http://localhost:8080/competences/` + response.data[i]).then((competence) => {
                this.editValues.selectedCompetences.push(competence.data)
                this.editValues.tickBoxesForCompetences[competence.data.id] = true
              })

            }
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Assignments Project Competence Database Error", error.message, "danger")
            }
          })


    },

    /**
     * Deletes the currentProject from DB
     */
    deleteProject: async function () {
      this.prompts.activeDeletePrompt = false;
      //deletes the project
      await axios.delete(`http://localhost:8080/projects/` + this.currentProject.projectNumber).then(async () => {

        await axios.delete(`http://localhost:8080/assignmentsbyproject/${this.currentProject.projectNumber}`);
        await axios.delete(`http://localhost:8080/allAssignedCompetencesProject/${this.currentProject.projectNumber}`);


        var idOfAssignment = 0
        await axios.get(`http://localhost:8080/assignedProjectsClient/`).then(async (result) => {
          for (var i = 0; i < result.data.length; i++) {
            if (result.data[i].clientID === this.currentProject.clientID && result.data[i].projectID === this.currentProject.projectNumber) {
              idOfAssignment = result.data[i].id
            }
          }

        })
        await axios.delete(`http://localhost:8080/assignedProjectsClient/` + idOfAssignment).then(async () => {

          this.notify("Confirmation", "Project has been successfully deleted.", "success")
        }).catch((error) => {
          if (error.response) {
            this.notify("Delete Error", error.message, "danger")
          }
        })

      }).catch((error) => {
        if (error.response) {
          this.notify("Delete Error", error.message, "danger")
        }
      })
      await this.resetAllValues()
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
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
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
          .catch((error) => {
            if (error.response) {
              this.notify("Employees Database Error", error.message, "danger")
            } else {
              this.notify("Employees Database Error", "Connection to Database Error", "danger")
            }
          })
    },

    /**
     * Gets all competences from DB
     */
    fetchAllCompetences: async function () {
      await axios.get(`http://localhost:8080/competences/`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            console.log(response.data)
            this.competences = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Competences Database Error", error.message, "danger")
            } else {
              this.notify("Employees Database Error", "Connection to Database Error", "danger")
            }
          })
    },

    /**Fetches the names of all the clients of all projects*/
    fetchNameOfClientForProjects: async function () {
      var assignments = []
      await axios.get(`http://localhost:8080/assignedProjectsClient/`)
          .then(response => {
            assignments = response.data
          })
      // eslint-disable-next-line no-console
      console.log(assignments)

      for (var i = 0; i < this.projects.length; i++) {
        for (var j = 0; j < assignments.length; j++) {
          if (assignments[j].projectID == this.projects[i].projectNumber) {
            // eslint-disable-next-line no-console
            console.log("Went In here")
            await axios.get(`http://localhost:8080/clients/` + assignments[j].clientID).then(response => {
              this.clientsNamesForProjects[this.projects[i].projectNumber] = response.data.name
            })
          }
        }


      }

    },

    /**
     * Gets a specific project from DB
     *
     * @param id of project
     */
    fetchProject: async function (id) {
      var competences = []
      var clientID = 0
      await axios.get(`http://localhost:8080/competencesByProject/` + id).then(async response => {
        // JSON responses are automatically parsed.
        for (var i = 0; i < response.data.length; i++) {
          await axios.get(`http://localhost:8080/competences/` + response.data[i]).then(response1 => {

            competences.push(response1.data)
          })
        }

      })
          .catch((error) => {
            if (error.response) {
              this.notify("Competences For Projects Database Error", error.message, "danger")
            }
          })

      await axios.get(`http://localhost:8080/assignedProjectsClient/`).then(response => {
        // JSON responses are automatically parsed.
        for (var i = 0; i < response.data.length; i++) {
          if (response.data[i].projectID == id) {
            clientID = response.data[i].clientID
          }
        }


      })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            }
          })

      var arrayOfAllCompetences = []
      await axios.get(`http://localhost:8080/assignmentsbyproject/` + id).then(async response => {
        // JSON responses are automatically parsed.
        for (var i = 0; i < response.data.length; i++) {
          await axios.get(`http://localhost:8080/competencesByEmployee/` + response.data[i].employeeID).then((response1) => {
            for (var j = 0; j < response1.data.length; j++) {
              arrayOfAllCompetences.push(response1.data[j])
            }
          })
        }


      })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            }
          })

      this.competencesAbgedeckt = {}

      for (var i = 0; i < competences.length; i++) {
        if (arrayOfAllCompetences.includes(competences[i].id)) {
          this.$set(this.competencesAbgedeckt, competences[i].id, true)
        } else {
          this.$set(this.competencesAbgedeckt, competences[i].id, false)
        }
      }


      await axios.get(`http://localhost:8080/projects/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentProject = response.data
            this.currentProject.competences = competences
            this.currentProject.clientID = clientID
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            }
          })


    },


    /**
     * Gets all projects of a specific client
     *
     * @param id of client
     */
    async fetchProjectsSortedByCustomer(id) {
      var indexesOfProjects = []
      await axios.get(`http://localhost:8080/projectsByClient/` + id).then((response) => {
        indexesOfProjects = response.data
      })

      await axios.get(`http://localhost:8080/projects`)
          .then(response => {
            var array = []
            for (var i = 0; i < response.data.length; i++) {
              if (indexesOfProjects.includes(response.data[i].projectNumber)) {
                array.push(response.data[i])
              }
            }
            this.projects = array
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            }
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
            //console.log("Returning false")
            return false;
          } else {
            // eslint-disable-next-line no-console
            //console.log("Returning true")
            return true;
          }
        }
      }
    },

    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },
    /** Resets all values of input and edit fields. Also resets the values for the employee dropdown*/
    resetAllValues: function () {
      this.inputValues.projectName = ''
      this.inputValues.pricePerHour = ''
      this.inputValues.clientIDField = ''
      this.inputValues.plannedStartField = this.dateToday;
      this.inputValues.plannedEndField = this.dateToday;
      this.inputValues.plannedEffortField = ''
      this.inputValues.selectedCompetences = []
      this.inputValues.tickBoxesForCompetences = {}
      this.currentClient = {name: "None"}
      this.currentEmployee = {name: "Employee"}
      //this.selectedEmployeeForAssignment= {name:"Employee"}
      this.assignHours = ''
      this.editValues.clientIDField = ''
      this.editValues.plannedStartField = ''
      this.editValues.plannedEndField = ''
      this.editValues.plannedEffortField = ''
      this.editValues.selectedCompetences = []
      this.editValues.tickBoxesForCompetences = {}
      this.editValues.performedEffortField = ''
      this.assignHours = 0
      this.competencesAbgedeckt = {}
    },


  }


}
</script>
label {
vertical-align: top;
}
<style>
</style>