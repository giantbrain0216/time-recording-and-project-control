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
                  {{"Project " + project.projectNumber}}
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
                      min="2018-01-01" max="2030-12-31"></div>
          <div class="m-3"><input type="time" id="starttime" name="starttime"
                      min="06:00" max="22:00" required>
            <small>   Start Time</small>
          </div>

          <div class="m-3"><input type="time" id="endtime" name="endtime"
                      min="06:00" max="22:00" required>
            <small>   End Time</small>
          </div>
          <div class="m-3"><vs-button color="success" type="relief">Success</vs-button></div>
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
      currentProject: "Project",
      currentProjectID: 0,
      employees:[],
      currentEmployee: "Employee",
      currentEmployeeID:0,
      dateToday: ""
    };
  },

  created() {
    axios.get(`http://localhost:8080/projects`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.projects = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })
    axios.get(`http://localhost:8080/employees`)
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
    updateProject: function(id){
        this.currentProjectID = id;
      for(let i = 0; i<this.projects.length;i++){
        if(this.projects[i].projectNumber == id){
          this.currentProject = this.projects[i].projectName
        }
      }
    },

    updateEmployee: function(id){
      this.currentEmployeeID = id;
      for(let i = 0; i<this.employees.length;i++){
        if(this.employees[i].employeeID == id){
          this.currentEmployee = this.employees[i].name
        }
      }
    },

  }



}
</script>

<style scoped>

</style>