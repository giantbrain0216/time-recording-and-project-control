<template>
  <div>
    <vs-row>
      <vs-col vs-lg="4" vs-sm="12" vs-xs="12">
        <vs-card class="cardx">
          <div slot="header">
            <h4>
              Time Registration
            </h4>
          </div>
          <div>
            <div class="ml-3">
            <autocomplete
                ref="textSearchOfEmployeeAdd"
                :search="filterEmployeeItemsAdd"
                :get-result-value="getEmployeeResultValue"
                @submit="handleEmployeeSubmitAdd"
                placeholder="Search for an employee"
                aria-label="Search for an employee"
                auto-select
            ></autocomplete>
            <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
              <div>Selected Employee:</div>
              <div class="ml-1" style="color:royalblue;">{{currentEmployee.name }}</div>
            </div>
            </div>
            <div class="ml-3" v-if='"employeeID" in currentEmployee'>
            <autocomplete

                ref="textSearchOfProjectAdd"
                :search="filterProjectItemsAdd"
                :get-result-value="getProjectResultValue"
                @submit="handleProjectSubmitAdd"
                :placeholder= "'Select project of ' + currentEmployee.name"
                aria-label="Search for an employee"
                auto-select
            ></autocomplete>
            <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
              <div>Selected Project:</div>
              <div class="ml-1" style="color:royalblue;">{{currentProject.projectName }}</div>
            </div>
            </div>

            <div class="m-3">
              <h6 style="color: red" v-if="projectNotStarted(currentProject)">You cannot submit time registration for
                this
                project because it has not started yet !</h6>
            </div>
            <div v-show="!projectNotStarted(currentProject)">
              <div class="m-3"><input type="date" id="date" name="date"
                                      v-model="dateInput"
                                      min="2018-01-01" :max="dateToday"></div>

              <div class="m-3"><input style="width:128px" type="time" id="starttime" name="starttime"
                                      min="06:00" max="22:00" v-model="starttime" required>
                <small> Start Time</small>
              </div>

            <div class="m-3"><input style="width:128px" type="time" id="endtime" name="endtime"
                                    min="06:00" max="22:00" v-model="endtime" required>
              <small> End Time</small>
            </div>
            <div class="m-3">
              <vs-textarea counter="100" label="Brief Description" :counter-danger.sync="counterDanger"
                           v-model="textarea"/>
            </div>
            <div  class="m-3">
              <vs-button color="success" type="relief" @click="submitTimeRegistration"
                         v-bind:disabled="!validInput(starttime,endtime)||projectNotStarted(currentProject)">Save Time
                Registration
              </vs-button>
            </div>
          </div>
          </div>

        </vs-card>
        <vs-divider>
          <h4 style="text-align: center">Or</h4> </vs-divider>        <vs-card class="cardx mt-3">
          <div  slot="header">
            <h4 style="cursor: pointer" @click='$router.push({name:"CSV Import", params: {}});'>
              Import Time Registrations From CSV File
            </h4>
           </div>
        </vs-card>

        <vs-divider>
        <h4 style="text-align: center">Or</h4> </vs-divider>
        <vs-card class="cardx mt-3">
          <div  slot="header">
            <h4 style="cursor: pointer" @click='$router.push({name:"CSV Import Ticket System", params: {}});'>
              Import Time Registrations From Ticket System
            </h4>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="timeregistrations.length!==0" type="flex" vs-justify="center" vs-align="center" :vs-lg="8"
              vs-sm="12" vs-xs="12" code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h4>
              Time Registrations for Employee : <strong style="color: red"> {{ this.currentEmployee.name }}</strong>
            </h4>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0">Project</th>
                <th class="border-top-0">From</th>
                <th class="border-top-0">To</th>
                <th class="border-top-0">Brief Description</th>
                <th class="border-top-0">Delete</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="registration in timeregistrations" :key="registration.id">
                <td>
                  <div class="d-flex align-items-center">
                    <div class="mr-2">{{projectNames[registration.projectID] }}</div>
                  </div>
                </td>
                <td>
                  <div class="d-flex align-items-center">{{ registration.start }}</div>
                </td>
                <td>
                  <div class="d-flex align-items-center">{{ registration.end }}</div>
                </td>
                <td>{{ registration.description }}</td>
                <td>
                  <div>
                    <vs-button @click="activeDeletePrompt=true; currentRegistration=registration.id" class="m-1"
                               color="danger" icon="delete" type="filled">
                      Delete
                    </vs-button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>
    </vs-row>

    <vs-prompt
        title="Delete Registration"
        color="danger"
        @cancel='notify("Closed","Registration has not been deleted.","warning")'
        @accept="deleteRegistration()"
        @close='notify("Closed","Registration has not been deleted.","warning")'
        :is-valid="true"
        :active.sync="activeDeletePrompt"
    >
      <div class="con-exemple-prompt">
        <h4>{{ "Are you sure you want to delete the registration with ID: " + currentRegistration }}</h4>
      </div>
    </vs-prompt>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "timeRegistration",
  data: () => {
    return {
      projects: [],
      employees: [],
      projectNames: {},
      currentProject: {projectName: "Project"},
      currentEmployee: {name: "None"},
      dateToday: "",
      dateInput: "",
      starttime: "",
      endtime: "",
      timeregistrations: [],
      activeDeletePrompt: false,
      currentRegistration: 0,
      textarea: '',
      counterDanger: false,
      csv: ''

    };
  },

  async created() {
    await axios.get(`http://localhost:8080/projects`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.projects = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })
    await axios.get(`http://localhost:8080/employees`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.employees = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })
    this.fetchProjectNames()


    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;
    this.dateInput = this.dateToday

  },

  methods: {

    /**Filters items for searchbar of employees on add form*/
    async filterEmployeeItemsAdd(input) {

      if (input.length < 1) { return [] }

      return this.employees.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()))
      })
    },
    /**Returns name of the employee objects*/
    getEmployeeResultValue(result){
      return result.name
    },
    /**Handle function when employee is selected by searchbar add form*/
    async handleEmployeeSubmitAdd(result){
      await this.fetchProjectsByEmployee(result.employeeID);
      await this.fetchTimeRegistrationsByEmployee(result.employeeID);
      this.currentEmployee=result
      this.$refs.textSearchOfEmployeeAdd.value = ""

    },

    /**Filters items for searchbar of projects on add form*/
    async filterProjectItemsAdd(input) {

      if (input.length < 1) { return [] }

      return this.projects.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.projectName.toLowerCase()
            .startsWith(input.toLowerCase()))
      })
    },
    /**Returns name of the project objects*/
    getProjectResultValue(result){
      return result.projectName
    },
    /**Handle function when project is selected by searchbar add form*/
    handleProjectSubmitAdd(result){
      this.currentProject = result;
      this.$refs.textSearchOfEmployeeAdd.value = ""

    },

    /**Fetches the time registrations filtered by the employeeID*/
    fetchTimeRegistrationsByEmployee: async function (id) {
      this.timeregistrations = []
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              if (response.data[i].employeeID === id) {
                this.timeregistrations.push(response.data[i])
              }
            }
          })
          .catch(e => {
            this.errors.push(e)
          })


    },

    fetchProjectNames(){
      for (var i = 0; i < this.projects.length; i++) {
        //this.projectNames.push({key:this.projects[i].projectID ,value: this.projects[i].projectName})
        this.projectNames[this.projects[i].projectNumber] = this.projects[i].projectName
      }
    },

    /**Fetches Porjects filtered by the employeeID*/
    fetchProjectsByEmployee: async function (id) {
      await axios.get(`http://localhost:8080/projects`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      var listOfProjectsIndexes = []
      var listOfProjects = []
      await axios.get(`http://localhost:8080/assignmentsbyemployee/` + id)
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              listOfProjectsIndexes.push(response.data[i].projectID)
            }
          })
          .catch(e => {
            this.errors.push(e)
          })

      for (var i = 0; i < this.projects.length; i++) {
        if (listOfProjectsIndexes.indexOf(this.projects[i].projectNumber) !== -1) {
          listOfProjects.push(this.projects[i])
        }
      }
      this.projects = listOfProjects


    },

    /**Submits time registration*/
    submitTimeRegistration: async function () {

      await axios.post(`http://localhost:8080/timeregistrations`,
          {
            "employeeID": this.currentEmployee.employeeID,
            "projectID": this.currentProject.projectNumber,
            "start": this.dateInput + " " + this.starttime,
            "end": this.dateInput + " " + this.endtime,
            "description": this.textarea
          }).then(() => {
        axios.get(`http://localhost:8080/projects/` + this.currentProject.projectNumber).then(response => {
          var startDate = new Date("1970-01-01 " + this.starttime);
          var endDate = new Date("1970-01-01 " + this.endtime);
          let hours = ((endDate - startDate) / 36e5);
          var project = response.data
          project.performedEffort = project.performedEffort + hours
          axios.put(`http://localhost:8080/projects/`, project).then(async () => {
            this.notify("Success", "Registration has been successfully added.", "success")
            await this.resetAllValues()


          }).catch((error) => {
            // handle this error here
            if (error.response) {
              this.notify("Error", error.message, "danger")
            }
          })

        }).catch((error) => {
          // handle this error here
          if (error.response) {
            this.notify("Error", error.message, "danger")
          }
        })
      })
          .catch((error) => {
            // handle this error here
            if (error.response) {
              this.notify("Error", error.message, "danger")
            }
          })


    },

    /**Checks if the input for the time registration is valid*/
    validInput(starttime, endtime) {
      var startdate = new Date("1970-01-01 " + starttime);
      var enddate = new Date("1970-01-01 " + endtime);
      return (startdate.getTime() < enddate.getTime() && !this.counterDanger && this.textarea !== '' && 'projectNumber'
          in this.currentProject && this.currentEmployee.name !== "None"  &&
          new Date(this.currentProject.plannedStart).getTime()<=new Date(this.dateInput + " 00:00").getTime())
    },

    /**Checks if a project has not started yet*/
    projectNotStarted(project) {
      return (new Date(project.plannedStart).getTime() > new Date(this.dateToday + " 00:00").getTime())

    },

    /**Deletes Registration*/
    async deleteRegistration() {
      var timeOfRegistration = 0;
      await axios.get(`http://localhost:8080/timeregistrations/` + this.currentRegistration).then(response => {
        var reg = response.data
        var startDate = new Date(reg.start);
        var endDate = new Date(reg.end);
        timeOfRegistration = Math.round(Math.abs(endDate - startDate) / 36e5);

      }).catch((error) => {
        if (error.response) {

          this.notify("Error", error.message, "danger")
        }
      })

      await axios.delete(`http://localhost:8080/timeregistrations/` + this.currentRegistration).then(() => {
        var projectID = 0;
        for (var i = 0; i < this.timeregistrations.length; i++) {
          if (this.timeregistrations[i].id === this.currentRegistration) {
            projectID = this.timeregistrations[i].projectID
          }
        }

        axios.get(`http://localhost:8080/projects/` + projectID).then(response => {
          var project = response.data
          project.performedEffort = project.performedEffort - timeOfRegistration
          axios.put(`http://localhost:8080/projects/`, project).then(() => {
            this.notify("Success", 'Registration has been successfully deleted.', "success")
            this.resetAllValues()
          }).catch((error) => {
            // handle this error here
            if (error.response) {
              this.notify("Error", error.message, "danger")
            }
          })

        }).catch((error) => {
          // handle this error here
          if (error.response) {
            this.notify("Error", error.message, "danger")
          }
        })
      }).catch((error) => {
        // handle this error here
        if (error.response) {
          this.activeDeletePrompt = false;
          this.notify("Error", error.message, "danger")

        }
      })
      await this.fetchTimeRegistrationsByEmployee(this.currentEmployeeID)
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
      this.currentProject = {projectName: "Project"}
      this.currentEmployee = {name: "None"}
      this.dateInput = this.dateToday
      this.starttime = ""
      this.endtime = ""
      this.timeregistrations = []
      this.textarea = ""
    },

    handleFileUpload: function () {
      /* return first object in FileList */
      var file = event.target.files[0];
      this.$papa.parse(file, {
        header: true,
        complete: function (results) {
          // eslint-disable-next-line no-console
          console.log(results.data);
        }
      })
    },

  }
}
</script>

<style scoped>

</style>
