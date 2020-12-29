<template>
  <div>
    <vs-row type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
            code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Projects </h2> <h6 style="color:forestgreen;">
            {{ this.projectsSearched.length }} Projects found</h6>
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
              <tr v-for="project in projectsSearched" :key="project.projectNumber">
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">{{project.projectName}}
                </td>
                <td>{{ project.clientID }}</td>
                <!-- <td>{{ project.projectName }}</td> -->
                <td> {{ project.plannedEnd.substring(0, 10) }}</td>
                <td style="text-align: center">
                  <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                  </vs-progress>
                  <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
                <td> <i icon="information">You need to go to the project list view. You can there edit, assign or delete projects !</i> </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
    </vs-row>

    <vs-row type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
            code-toggler>
      <vs-card class="cardx">
        <div slot="header">
          <h2 class="float-left" style="color: cornflowerblue">Employees </h2> <h6 style="color:forestgreen;">
          {{ this.employeesSearched.length }} Employees found</h6>
        </div>
        <div class="table-responsive">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0" style="color: cornflowerblue">ID</th>
              <th class="border-top-0" style="color: cornflowerblue">Name</th>
              <th class="border-top-0" style="color: cornflowerblue">Competences</th>
              <th class="border-top-0" style="color: cornflowerblue">Remaining Working Hours Per Week</th>
              <th class="border-top-0" style="color: cornflowerblue">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="employee in employeesSearched" :key="employee.employeeID">
              <td>{{employee.employeeID}}</td>
              <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                <div class="mr-2">
                  <vs-avatar color="primary" :text="employee.name"/>
                </div>
                {{employee.name}}
              </td>
              <td>{{ employee.competences }}</td>
              <td>{{ employee.remainingWorkingHoursPerWeek }}</td>
              <td> <i icon="information">You need to go to the project list view. You can there edit or delete employees !</i> </td>
            </tr>
            </tbody>
          </table>
        </div>
      </vs-card>
    </vs-row>

    <vs-row type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
            code-toggler>
      <vs-card class="cardx">
        <div slot="header">
          <h2 class="float-left" style="color: cornflowerblue">Clients </h2> <h6 style="color:forestgreen;">
          {{ this.employeesSearched.length }} Clients found</h6>
        </div>
        <div class="table-responsive">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0" style="color: cornflowerblue">ID</th>
              <th class="border-top-0" style="color: cornflowerblue">Name</th>
              <th class="border-top-0" style="color: cornflowerblue">EMAIL</th>
              <th class="border-top-0" style="color: cornflowerblue">Project IDs</th>
              <th class="border-top-0" style="color: cornflowerblue">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="client in clientsSearched" :key="client.clientID">
              <td>{{client.clientID}}</td>
              <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                <div class="mr-2">
                  <vs-avatar color="primary" :text="client.name"/>
                </div>
                {{client.name}}
              </td>
              <td>{{client.email}}</td>
              <td>{{client.projectIDs}}</td>
              <td> <i icon="information">You need to go to the project list view. You can there edit or delete clients !</i> </td>
            </tr>
            </tbody>
          </table>
        </div>
      </vs-card>
    </vs-row>


  </div>
</template>


<script>
import axios from "axios";

export default {
  name: "SearchResults",
  props: {searchKey: String},

  data: () => {
    return {
      allProjects: [],
      allClients: [],
      allEmployees: [],
      projectsSearched: [],
      clientsSearched: [],
      employeesSearched: [],
    };
  },

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

    /**
     * adds the projects whose name include the search key to one array
     */
    addSearchedProjects: function () {
      for (let i = 0; i < this.allProjects.length; i++) {
        if (this.allProjects[i].projectName.includes(this.searchKey)) {
          this.projectsSearched.push(this.allProjects[i])
        }
      }
    },

    /**
     * adds the clients whose name include the search key to one array
     */
    addSearchedClients: function () {
      for (let i = 0; i < this.allClients.length; i++) {
        if (this.allClients[i].name.includes(this.searchKey)) {
          this.clientsSearched.push(this.allClients[i])
        }
      }
    },

    /**
     * adds the employees whose name include the search key to one array
     */
    addSearchedEmployees: function () {
      for (let i = 0; i < this.allEmployees.length; i++) {
        if (this.allEmployees[i].name.includes(this.searchKey)) {
          this.employeesSearched.push(this.allEmployees[i])
        }
      }
    },
  },

  created() {
    this.fetchAllExistingClients()
    this.fetchAllExistingEmployees()
    this.fetchAllExistingProjects()
  }
};
</script>