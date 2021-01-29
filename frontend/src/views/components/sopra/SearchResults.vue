<template>
  <div>
<div>
    <h2> Search Results of : <i>{{ searchKey }}</i></h2>
     <ul class="list-inline mb-0 mt-2">
       <li class="list-inline-item">
         <vs-checkbox v-model="showProject">Projects</vs-checkbox>
       </li>
      <li class="list-inline-item">
        <vs-checkbox v-model="showEmployee">Employees</vs-checkbox>
      </li>
      <li class="list-inline-item">
        <vs-checkbox v-model="showClient">Clients</vs-checkbox>
      </li>

      <li class="list-inline-item">
        <vs-checkbox v-model="showTimeRegistration">Time Registrations</vs-checkbox>
      </li>
    </ul>
</div>
    <h6  class="mt-2" style="color: #28a745">
      {{
        this.timeregistrationSearched.length + this.projectsSearched.length +
        this.clientsSearched.length + this.employeesSearched.length
      }} Results</h6>

    <div class="mt-3">
      <vs-row v-show="showProject" type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h3 class="float-left" style="color: gray"><strong style="color: red">
              {{ this.projectsSearched.length }} </strong> Projects found</h3>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Deadline</th>
                <th class="border-top-0" style="color: cornflowerblue">Progress</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="project in projectsSearched" :key="project.projectNumber">
                <td>{{ project.projectNumber }}</td>
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">{{ project.projectName }}
                </td>
                <td> {{ project.plannedEnd.substring(0, 10) }}</td>
                <td style="text-align: center">
                  <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                  </vs-progress>
                  <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
                <td><i>You need to go to the project list view. You can there edit, assign or delete
                  projects !</i></td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-row>

      <vs-row v-show="showEmployee" type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h3 class="float-left" style="color: gray"><strong style="color: red">
              {{ this.employeesSearched.length }} </strong> Employees found</h3>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                 <th class="border-top-0" style="color: cornflowerblue">Remaining Working Hours Per Week</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="employee in employeesSearched" :key="employee.employeeID">
                <td>{{ employee.employeeID }}</td>
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                  <div class="mr-2">
                    <!--   <vs-avatar color="primary" :text="employee.name"/> -->
                  </div>
                  {{ employee.name }}
                </td>
                 <td>{{ employee.remainingWorkingHoursPerWeek }}</td>
                <td><i>You need to go to the employee list view. You can there edit or delete
                  employees !</i></td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-row>

      <vs-row  v-show="showClient" type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h3 class="float-left" style="color: gray"><strong style="color: red">
              {{ this.clientsSearched.length }} </strong> Clients found</h3>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">EMAIL</th>
                <th class="border-top-0" style="color: cornflowerblue">ADDRESS</th>
                 <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="client in clientsSearched" :key="client.clientID">
                <td>{{ client.clientID }}</td>
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                  <div class="mr-2">
                    <!--      <vs-avatar color="primary" :text="client.name"/> -->
                  </div>
                  {{ client.name }}
                </td>
                <td>{{ client.email }}</td>
                <td>{{ client.address }}</td>
                <td><i >You need to go to the client list view. You can there edit or delete clients
                  !</i></td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-row>

      <vs-row v-show="showTimeRegistration" type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h3 class="float-left" style="color: gray"><strong style="color: red">
              {{ this.timeregistrationSearched.length }} </strong> Time Registrations found</h3>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">Project</th>
                <th class="border-top-0" style="color: cornflowerblue">Employee</th>
                <th class="border-top-0" style="color: cornflowerblue">From</th>
                <th class="border-top-0" style="color: cornflowerblue">To</th>
                <th class="border-top-0" style="color: cornflowerblue">Duration</th>
                <th class="border-top-0" style="color: cornflowerblue">Brief Description</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="registration in timeregistrationSearched" :key="registration.id">
                <td >{{ getProjectName(registration.projectID) }}</td>
                <td >{{ getEmployeeName(registration.employeeID) }}</td>
                <td >{{ registration.start }}</td>
                <td >{{ registration.end }}</td>
                <td >  {{ ((new Date(registration.end) - new Date(registration.start)) / 36e5).toFixed(2) }} Hours</td>
                <td >{{ registration.description }}</td>
                <td><i>You need to go to the time registration list view. You can there delete time
                  registrations
                  !</i></td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-row>
    </div>

  </div>
</template>


<script>
import axios from "axios";
/*import { debounce } from "lodash";*/


export default {
  name: "SearchResults",
  //props: {searchKey: String},

  data: () => {
    return {
      allProjects: [],
      employeeNames:[],
      allClients: [],
      allEmployees: [],
      allTimeRegistrations: [],
      timeregistrationSearched: [],
      projectsSearched: [],
      clientsSearched: [],
      employeesSearched: [],
      searchKey: "",
      buttonClicked: false,
      showClient:true,
      showEmployee:true,
      showProject:true,
      showTimeRegistration:true

    };
  },
  /* watch: {
     searchKey: debounce(function () {
       // buttonClicked is used to show the results of the search. Even though the button is not clicked
       // it means that the search key is not empty. May be it should be refactored !!
       this.buttonClicked = true;
       this.search();
     }, 1500),
   },*/
  methods: {
    /**
     * Gets the employees from the DB
     */
    fetchAllExistingProjects: async function () {
      await axios.get(`http://localhost:8080/projects/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allProjects = response.data
          })
    },

    /**
     * Gets the employees from the DB
     */
    fetchAllExistingEmployees: async function () {
      await axios.get(`http://localhost:8080/employees/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allEmployees = response.data
            this.employeeNames = response.data.map(employee => employee.name)
          })
    },

    /**
     * Gets the clients from the DB
     */
    fetchAllExistingClients: async function () {
      await axios.get(`http://localhost:8080/clients/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allClients = response.data
          })
    },

    fetchAllExistingRegistrations: async function () {
      await axios.get(`http://localhost:8080/timeregistrations/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allTimeRegistrations = response.data
          })
    },

    /**
     * adds the projects whose name include the search key to one array
     */
    addSearchedProjects: function () {
      this.projectsSearched = []
      if (this.searchKey.length !== 0) {
        for (let i = 0; i < this.allProjects.length; i++) {
          if (this.allProjects[i].projectName.toLowerCase().includes(this.searchKey.toLowerCase())) {
            if (!this.projectsSearched.includes(this.allProjects[i]))
              this.projectsSearched.push(this.allProjects[i])
          }
        }
      }
    },

    getProjectName: function (id){
      for (let i=0;i<this.allProjects.length;i++){
        if (this.allProjects[i].projectNumber === id)
          return this.allProjects[i].projectName
      }
    },

    getEmployeeName: function (id){
      for (let i=0;i<this.allEmployees.length;i++){
        if (this.allEmployees[i].employeeID === id)
          return this.allEmployees[i].name
      }
    },

    /**
     * adds the clients whose name include the search key to one array
     */
    addSearchedClients: function () {
      this.clientsSearched = []
      if (this.searchKey.length !== 0) {
        for (let i = 0; i < this.allClients.length; i++) {
          if (this.allClients[i].name.toLowerCase().includes(this.searchKey.toLowerCase())) {
            if (!this.clientsSearched.includes(this.allClients[i]))
              this.clientsSearched.push(this.allClients[i])
          }
        }
      }
    },

    /**
     * adds the employees whose name include the search key to one array
     */
    addSearchedEmployees: function () {
      this.employeesSearched = []
      if (this.searchKey.length !== 0) {
        for (let i = 0; i < this.allEmployees.length; i++) {
          if (this.allEmployees[i].name.toLowerCase().includes(this.searchKey.toLowerCase())) {
            if (!this.employeesSearched.includes(this.allEmployees[i]))
              this.employeesSearched.push(this.allEmployees[i])
          }
        }
      }
    },

    /**
     * adds the time registrations whose brief description  include the search key to one array
     */
    addSearchedTimeRegistrations: function () {
      this.timeregistrationSearched = []
      if (this.searchKey.length !== 0) {
        for (let i = 0; i < this.allTimeRegistrations.length; i++) {
          if ( this.getProjectName(this.allTimeRegistrations[i].projectID).toLowerCase().includes(this.searchKey.toLowerCase())||
              this.getEmployeeName(this.allTimeRegistrations[i].employeeID).toLowerCase().includes(this.searchKey.toLowerCase())) {
            if (!this.timeregistrationSearched.includes(this.allTimeRegistrations[i]))
              this.timeregistrationSearched.push(this.allTimeRegistrations[i])
          }
        }
      }
    },

    search: function () {
      this.addSearchedProjects()
      this.addSearchedEmployees()
      this.addSearchedClients()
      this.addSearchedTimeRegistrations()
    },

    reset: function () {
      this.searchKey = ""
      this.projectsSearched = []
      this.clientsSearched = []
      this.employeesSearched = []
      this.buttonClicked = false
    },
  },


  async created() {
    this.searchKey = this.$route.params.searchKey;
    await this.fetchAllExistingClients()
    await this.fetchAllExistingEmployees()
    await this.fetchAllExistingProjects()
    await this.fetchAllExistingRegistrations()
    this.search()
  },

  watch: {
    '$route.params.searchKey': async function () {
      this.searchKey = this.$route.params.searchKey;
      await this.fetchAllExistingClients()
      await this.fetchAllExistingEmployees()
      await this.fetchAllExistingProjects()
      await this.fetchAllExistingRegistrations()
      this.search()
    }
  },

};
</script>