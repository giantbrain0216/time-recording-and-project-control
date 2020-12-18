<template>
  <div>
  <vs-row >
    <vs-col vs-lg="4" vs-sm="6" vs-xs="12">
      <vs-card class="cardx">
        <div slot="header">
          <h4>
            Time Registration
          </h4>
        </div>
        <div>
          <div class="d-flex align-items-center dropdownbtn-alignment m-3">
            <vs-button class="btnx" type="filled">{{this.currentProject}}</vs-button>
            <vs-dropdown>
              <vs-button class="btn-drop" type="filled" icon="expand_more"></vs-button>
              <!-- <a href="#">Hola mundo</a> -->
              <vs-dropdown-menu>
                <vs-dropdown-item  @click="updateProject(project.projectNumber)" v-for="project in projects" :key="project.projectNumber">
                  {{project.projectName}}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>
          </div>
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
          <div class="m-3"><input type="date" id="date" name="date"
                      :value="dateToday"
                      min="2018-01-01" :max="dateToday"></div>
          <div class="m-3"><input type="time" id="starttime" name="starttime"
                      min="06:00" max="22:00" v-model="starttime" required>
            <small>   Start Time</small>
          </div>

          <div class="m-3"><input type="time" id="endtime" name="endtime"
                      min="06:00" max="22:00" v-model="endtime" required>
            <small>   End Time</small>
          </div>
          <div @click="submitTimeRegistration" class="m-3"><vs-button color="success" type="relief" v-bind:disabled="!validTime(starttime,endtime)">Save Time Registration</vs-button></div>
        </div>

      </vs-card>
    </vs-col>
    <vs-col  v-if="timeregistrations.length!==0" type="flex" vs-justify="center" vs-align="center" :vs-lg="8" vs-sm="6" vs-xs="12" code-toggler>
      <vs-card class="cardx">
        <div slot="header">
          <h4>
            {{"Time Registrations for Employee with ID: " + currentEmployeeID}}
          </h4>
        </div>
        <div>
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Project</th>
            <th class="border-top-0">Registration ID</th>
            <th class="border-top-0">From</th>
            <th class="border-top-0">To</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="registration in timeregistrations" :key="registration.id">
            <td>
              <div class="d-flex align-items-center">
                <div class="mr-2">{{projectNames[registration.projectID]}}</div>
              </div>
            </td>
            <td>{{registration.id}}</td>
            <td><div class="d-flex align-items-center">{{registration.start}}</div></td>
            <td><div class="d-flex align-items-center">{{registration.end}}</div></td>
            <td>
              <div>
                <vs-button class="m-1" color="danger" type="filled">
                  Delete
                </vs-button>
                <vs-button class="m-1" color="primary" type="filled">
                  Edit
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
      starttime: "",
      endtime: "",
      timeregistrations:[]
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

  },

  methods: {
    fetchTimeRegistrations: async function(id){
      this.timeregistrations = []
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              console.log("Comparing " + response.data[i].employeeID+ "with " + id)
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


    updateProject: function(id){
        this.currentProjectID = id;
      for(let i = 0; i<this.projects.length;i++){
        if(this.projects[i].projectNumber == id){
          this.currentProject = this.projects[i].projectName
        }
      }
    },

    updateEmployee: async function(id){
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
          {"employeeID":this.currentEmployeeID,"projectID":this.currentProjectID,"start":startString,"end":endString})


    },

    validTime(starttime,endtime){
      var startdate = new Date("1970-01-01 " + starttime);
      var enddate = new Date("1970-01-01 " + endtime);
      return (startdate.getTime() < enddate.getTime())
    },


  }



}
</script>

<style scoped>

</style>