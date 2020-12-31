<template>
  <div>

    <h2>{{"Search Results for: " + searchKey}}</h2>

    <div class="mt-3">
      <vs-row type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h3 class="float-left" style="color: cornflowerblue"><strong style="color: red">
              {{ this.projectsSearched.length }} </strong> Projects found</h3>
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
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">{{ project.projectName }}
                </td>
                <td>{{ project.clientID }}</td>
                <!-- <td>{{ project.projectName }}</td> -->
                <td> {{ project.plannedEnd.substring(0, 10) }}</td>
                <td style="text-align: center">
                  <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                  </vs-progress>
                  <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
                <td><i icon="information">You need to go to the project list view. You can there edit, assign or delete
                  projects !</i></td>
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
            <h3 class="float-left" style="color: cornflowerblue"><strong style="color: red">
              {{ this.employeesSearched.length }} </strong> Employees found</h3>
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
                <td>{{ employee.employeeID }}</td>
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                  <div class="mr-2">
                    <!--   <vs-avatar color="primary" :text="employee.name"/> -->
                  </div>
                  {{ employee.name }}
                </td>
                <td>{{ employee.competences }}</td>
                <td>{{ employee.remainingWorkingHoursPerWeek }}</td>
                <td><i icon="information">You need to go to the employee list view. You can there edit or delete
                  employees !</i></td>
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
            <h3 class="float-left" style="color: cornflowerblue"><strong style="color: red">
              {{ this.clientsSearched.length }} </strong> Clients found</h3>
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
                <td>{{ client.clientID }}</td>
                <td class="m-b-0" style="font-weight: bold; font-size: 15px; cursor:pointer">
                  <div class="mr-2">
                    <!--      <vs-avatar color="primary" :text="client.name"/> -->
                  </div>
                  {{ client.name }}
                </td>
                <td>{{ client.email }}</td>
                <td>{{ client.projectIDs }}</td>
                <td><i icon="information">You need to go to the client list view. You can there edit or delete clients
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
      allClients: [],
      allEmployees: [],
      projectsSearched: [],
      clientsSearched: [],
      employeesSearched: [],
      searchKey: "",
      buttonClicked: false
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
      this.projectsSearched = []
      if (this.searchKey.length!==0){
      for (let i = 0; i < this.allProjects.length; i++) {
        if (this.allProjects[i].projectName.toLowerCase().includes(this.searchKey.toLowerCase())) {
          if (!this.projectsSearched.includes(this.allProjects[i]))
            this.projectsSearched.push(this.allProjects[i])
        }
      }}
    },

    /**
     * adds the clients whose name include the search key to one array
     */
    addSearchedClients: function () {
      this.clientsSearched = []
      if (this.searchKey.length!==0){
      for (let i = 0; i < this.allClients.length; i++) {
        if (this.allClients[i].name.toLowerCase().includes(this.searchKey.toLowerCase())) {
          if (!this.clientsSearched.includes(this.allClients[i]))
            this.clientsSearched.push(this.allClients[i])
        }
      }}
    },

    /**
     * adds the employees whose name include the search key to one array
     */
    addSearchedEmployees: function () {
      this.employeesSearched = []
      if (this.searchKey.length!==0){
      for (let i = 0; i < this.allEmployees.length; i++) {
        if (this.allEmployees[i].name.toLowerCase().includes(this.searchKey.toLowerCase())) {
          if (!this.employeesSearched.includes(this.allEmployees[i]))
            this.employeesSearched.push(this.allEmployees[i])
        }
      }}
    },

    search:function() {
      this.addSearchedProjects()
      this.addSearchedEmployees()
      this.addSearchedClients()
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
    this.search()
  },

  watch: {
    '$route.params.searchKey': async function() {
      this.searchKey = this.$route.params.searchKey;
      await this.fetchAllExistingClients()
      await this.fetchAllExistingEmployees()
      await this.fetchAllExistingProjects()
      this.search()
    }
  },

};
</script>