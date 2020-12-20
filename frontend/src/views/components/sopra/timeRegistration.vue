<template>
  <div>
  <vs-row >
    <vs-col vs-lg="4" vs-sm="12" vs-xs="12">
      <vs-card class="cardx">
        <div slot="header">
          <h4>
            Time Registration
          </h4>
        </div>
        <div>
          <div  class="d-flex align-items-center dropdownbtn-alignment m-3">
            <vs-button color="success" class="btnx" type="filled">{{this.currentEmployee}}</vs-button>
            <vs-dropdown>
              <vs-button color="success" class="btn-drop" type="filled" icon="expand_more"></vs-button>
              <!-- <a href="#">Hola mundo</a> -->
              <vs-dropdown-menu>
                <vs-dropdown-item  @click="updateEmployee(employee.employeeID)" v-for="employee in employees" :key="employee.employeeID">
                  {{employee.name}}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>
          </div>
          <div class="d-flex align-items-center dropdownbtn-alignment m-3">
            <vs-button class="btnx" type="filled" :disabled="currentEmployeeID == 0">{{this.currentProject}}</vs-button>
            <vs-dropdown>
              <vs-button class="btn-drop" type="filled" icon="expand_more" :disabled="currentEmployeeID == 0"></vs-button>
              <!-- <a href="#">Hola mundo</a> -->
              <vs-dropdown-menu>
                <vs-dropdown-item  @click="updateProject(project.projectNumber)" v-for="project in projects" :key="project.projectNumber">
                  {{project.projectName}}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>
          </div>
          <div class="m-3"><input type="date" id="date" name="date"
                                  v-model="dateInput"
                                  min="2018-01-01" :max="dateToday"></div>

          <div class="m-3"><input type="time" id="starttime" name="starttime"
                      min="06:00" max="22:00" v-model="starttime" required>
            <small>   Start Time</small>
          </div>

          <div class="m-3"><input type="time" id="endtime" name="endtime"
                      min="06:00" max="22:00" v-model="endtime" required>
            <small>   End Time</small>
          </div>
          <div class="m-3">
            <vs-textarea counter="100" label="Brief Description" :counter-danger.sync="counterDanger" v-model="textarea" />
          </div>

          <div @click="submitTimeRegistration" class="m-3"><vs-button color="success" type="relief" v-bind:disabled="!validInput(starttime,endtime)">Save Time Registration</vs-button></div>
        </div>

      </vs-card>
    </vs-col>
    <vs-col  v-if="timeregistrations.length!==0" type="flex" vs-justify="center" vs-align="center" :vs-lg="8" vs-sm="12" vs-xs="12" code-toggler>
      <vs-card class="cardx">
        <div slot="header">
          <h4>
            Time Registrations for Employee : <strong style="color: red">  {{this.currentEmployee }}</strong>
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
                <div class="mr-2">{{projectNames[registration.projectID]}}</div>
              </div>
            </td>
            <td><div class="d-flex align-items-center">{{registration.start}}</div></td>
            <td><div class="d-flex align-items-center">{{registration.end}}</div></td>
            <td>{{registration.description}}</td>
            <td>
              <div>
                <vs-button @click="activeDeletePrompt=true; currentRegistration=registration.id" class="m-1" color="danger" type="filled">
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
        @cancel='closeDeletePrompt("Registration has not been deleted.")'
        @accept="deleteRegistration"
        @close='closeDeletePrompt("Registration has not been deleted.")'
        :is-valid="true"
        :active.sync="activeDeletePrompt"
    >
      <div class="con-exemple-prompt">
        <h4>{{"Are you sure you want to delete the registration with ID: " + currentRegistration}}</h4>
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
      projects:[],
      projectNames:{},
      currentProject: "Project",
      currentProjectID: 0,
      employees:[],
      currentEmployee: "Employee",
      currentEmployeeID:0,
      dateToday: "",
      dateInput: "",
      starttime: "",
      endtime: "",
      timeregistrations:[],
      activeDeletePrompt:false,
      currentRegistration:0,
      textarea: '',
      counterDanger: false

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




    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;
    this.dateInput = yyyy + "-" + mm + '-' + dd;

  },

  methods: {

      fetchTimeRegistrations: async function(id){
      this.timeregistrations = []
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              if(response.data[i].employeeID === id){
                  this.timeregistrations.push(response.data[i])
              }
            }
          })
          .catch(e => {
            this.errors.push(e)
          })

      for(var i=0;i<this.projects.length;i++){
        //this.projectNames.push({key:this.projects[i].projectID ,value: this.projects[i].projectName})
        this.projectNames[this.projects[i].projectNumber] = this.projects[i].projectName
      }
    },


    fetchProjectsByEmployee: async function(id){
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
      await axios.get(`http://localhost:8080/assignmentsbyemployee/` + id )
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              listOfProjectsIndexes.push(response.data[i].projectID)
            }
          })
          .catch(e => {
            this.errors.push(e)
          })

      for(var i=0;i<this.projects.length;i++){
        if(listOfProjectsIndexes.indexOf(this.projects[i].projectNumber) != -1){
          listOfProjects.push(this.projects[i])
        }
      }
        this.projects = listOfProjects



    },

    updateProject: function(id){
        this.currentProjectID = id;
      for(let i = 0; i<this.projects.length;i++){
        if(this.projects[i].projectNumber == id){
          this.currentProject = this.projects[i].projectName
        }
      }
    },

    updateEmployee: async function(id){
      this.fetchProjectsByEmployee(id)
      await this.fetchTimeRegistrations(id)
      this.currentEmployeeID = id;
      for(let i = 0; i<this.employees.length;i++){
        if(this.employees[i].employeeID == id){
          this.currentEmployee = this.employees[i].name
        }
      }
    },

    submitTimeRegistration: async function(){
      var dateControl = document.querySelector('input[type="date"]');
      var startdate = dateControl.value;
      var timeControl = document.querySelector('input[id="starttime"]');
      var startime = timeControl.value;
      var timeControl1 = document.querySelector('input[id="endtime"]');
      var endtime = timeControl1.value;
      var startString = startdate + " " + startime
      var endString = startdate + " " + endtime

      await axios.post(`http://localhost:8080/timeregistrations`,
          {"employeeID":this.currentEmployeeID,"projectID":this.currentProjectID,"start":startString,"end":endString,"description":this.textarea}).then(() => {
         axios.get(`http://localhost:8080/projects/` + this.currentProjectID).then(response => {
          var startDate = new Date("1970-01-01 " + startime);
          var endDate = new Date("1970-01-01 " + endtime);
          let hours = Math.round(Math.abs(endDate - startDate) / 36e5);
          var project = response.data
          project.performedEffort = project.performedEffort + hours
          axios.put(`http://localhost:8080/projects/`,project).then(()=>{
            this.submitRegistrationAlert()
            dateControl.value = this.dateInput
            this.starttime = ""
            this.endtime = ""
            this.currentProject= "Project"
            this.currentProjectID=0
            this.currentEmployee= "Employee"
            this.currentEmployeeID=0
            this.timeregistrations=[]
            this.textarea=""
          }).catch((error) => {
            // handle this error here
            if (error.response){
              this.submitRegistrationFailed( error.message);
            }
          })

        }).catch((error) => {
          // handle this error here
          if (error.response){
            this.submitRegistrationFailed( error.message);
          }
        })
          })
          .catch((error) => {
          // handle this error here
          if (error.response){
            this.submitRegistrationFailed( error.message);
          }
        })



    },

    validInput(starttime, endtime){
      var startdate = new Date("1970-01-01 " + starttime);
      var enddate = new Date("1970-01-01 " + endtime);
      return (startdate.getTime() < enddate.getTime() && !this.counterDanger && this.textarea != '' && this.currentProjectID != 0 && this.currentEmployeeID != 0 )
    },
    closeDeleteAlert(message){
      // eslint-disable-next-line no-console
      console.log(message)
      this.$vs.notify({
        title: 'Cancelled:',
        color:"rgb(187, 138, 200)", type:"gradient",
        text: message//'Registration has not been deleted.'
      })
    },

    closeDeletePrompt: function (message){
      this.activeDeletePrompt = false;
      this.closeDeleteAlert(message)

    },
    deleteAlert() {
      this.activeDeletePrompt = false;
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Registration has been successfully deleted.'
      })
    },
    submitRegistrationAlert() {

      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Registration has been successfully added.'
      })
    },

    submitRegistrationFailed(message){
      this.$vs.notify({
        title: 'Confirmation:',
        text: message
      })
    },

    async deleteRegistration(){
      var timeOfRegistration = 0;
      await axios.get(`http://localhost:8080/timeregistrations/` + this.currentRegistration).then(response => {
        var reg = response.data
        var startDate = new Date(reg.start);
        var endDate = new Date(reg.end);
        timeOfRegistration = Math.round(Math.abs(endDate - startDate) / 36e5);

      }).catch((error) => {
        if (error.response){
          this.closeDeletePrompt( error.message);
        }
      })

      await axios.delete(`http://localhost:8080/timeregistrations/` + this.currentRegistration).then(() => {
        var projectID = 0;
        for(var i=0;i<this.timeregistrations.length;i++){
          if(this.timeregistrations[i].id === this.currentRegistration) {
            projectID = this.timeregistrations[i].projectID
          }
        }

        axios.get(`http://localhost:8080/projects/` + projectID).then(response => {
          var project = response.data
          project.performedEffort = project.performedEffort - timeOfRegistration
          axios.put(`http://localhost:8080/projects/`, project).then(()=>{
            this.deleteAlert()
          }).catch((error) => {
            // handle this error here
            if (error.response){
              this.closeDeletePrompt( error.message);
            }
          })

        }).catch((error) => {
          // handle this error here
          if (error.response){
            this.closeDeletePrompt( error.message);
          }
        })
      }).catch((error) => {
        // handle this error here
        if (error.response){
          this.closeDeletePrompt( error.message);
        }
      })
      await this.fetchTimeRegistrations(this.currentEmployeeID)
    }


  }



}
</script>

<style scoped>

</style>