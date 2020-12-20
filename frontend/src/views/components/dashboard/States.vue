<template>
    <vs-row>
        <vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">{{Math.round(getProgressOfAll())}}% Done</h4>
                <span>Total Poject progress</span>
                <vs-progress :percent="(getPerformedEffort()/getPlannedEffort())*100" color="primary">primary</vs-progress>
            </vs-card>
        </vs-col>
        <vs-col vs-lg="3" vs-xs="12" >
            <vs-card>
                <h4 class="mb-1">{{Math.round(percentageProjectsNeedEmployees) + "%"}}</h4>
             <span>Projects need more Employees</span>
                <vs-progress :percent="percentageProjectsNeedEmployees" color="danger">primary</vs-progress>
              <ul class="list-group list-group-flush">
                <li class="list-group-item" v-for="project in projectsNeedMoreEmployees" :key="project.id"><b-card class="text-danger">{{project.projectName}}</b-card></li>
              </ul>
            </vs-card>
        </vs-col>
        <vs-col vs-lg="2" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">{{Math.round(percentageOverloadedEmployees)+"%"}}</h4>
                <span>Overloaded Employees</span>
                <vs-progress :percent="percentageOverloadedEmployees" color="danger">primary</vs-progress>
              <ul class="list-group list-group-flush">
                <li class="list-group-item" v-for="employee in employeesOverloaded" :key="employee.employeeID"><b-card class="text-danger">{{employee.name}}</b-card></li>
              </ul>
            </vs-card>
        </vs-col>
      <vs-col vs-lg="2" vs-xs="12">
        <vs-card>
          <h4 class="mb-1">{{Math.round(percentageUnderloadedEmployees)+"%"}}</h4>
          <span>Underloaded Employees</span>
          <vs-progress :percent="percentageUnderloadedEmployees" color="warning">primary</vs-progress>
          <ul class="list-group list-group-flush">
            <li class="list-group-item" v-for="employee in employeesUnderloaded" :key="employee.employeeID"><b-card class="text-warning">{{employee.name}}</b-card></li>
          </ul>
        </vs-card>
      </vs-col>
      <vs-col vs-lg="2" vs-xs="12">
        <vs-card>
          <h4 class="mb-1">{{Math.round(calculateCancelled())}}%</h4>
          <span>Projects are finished</span>
          <vs-progress :percent="calculateCancelled()" color="success">primary</vs-progress>
        </vs-card>
      </vs-col>
    </vs-row>    
</template>
<script>
import axios from "axios";

export default {
  name: "States",
  data: () => {
    return {
      projects: [],
      employees:[],
      percentageProjectsNeedEmployees:null,
      projectsNeedMoreEmployees:[],
      percentageOverloadedEmployees:null,
      employeesOverloaded:[],
      percentageUnderloadedEmployees:null,
      employeesUnderloaded:[]
    }
  },

  async created() {
    await this.fetchAllProjects()
    await this.fetchAllEmployees()
    this.calculatePercentageOfProjectsNeedMoreEmployees()
    this.calculatePercentageOfOverloadedEmployees()
  },

  methods: {
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
    getPlannedEffort: function () {
      let plannedEffort = 0;
      this.projects.forEach((project) => plannedEffort += parseInt(project.plannedEffort))
      return plannedEffort;
    },

    getPerformedEffort: function () {
      let performedEffort = 0
      this.projects.forEach((project) => performedEffort += parseInt(project.performedEffort))
      return performedEffort
    },

    getProgressOfAll: function () {
      return this.getPerformedEffort()/this.getPlannedEffort()*100
    },

    getTimeUntilDeadline: function(projectIndexInList){
      var project = this.projects[projectIndexInList]
      var deadline = new Date(project.plannedEnd)
      var today = new Date()
      var daysUntilDeadline = (deadline - today)/ (1000 * 3600 * 24)
      if(daysUntilDeadline < 0){
        return -1;
      }else{
        return daysUntilDeadline
      }

    },

    getAvailableWorkingPower: async function(projectIndexInList){
      var summ = 0

      await axios.get(`http://localhost:8080/assignmentsbyproject/` + this.projects[projectIndexInList].projectNumber)
          .then(response => {
            for(var i=0;i<response.data.length;i++){
              summ += response.data[i].plannedWorkingHours
            }
          })
          .catch(e => {

            this.errors.push(e)
          })

     // console.log("SUMM " + summ)
      return summ

    },

    projectNeedsMoreEmployees: async function(projectIndexInList){
      var project = this.projects[projectIndexInList]
      var remainingEffort = project.plannedEffort - project.performedEffort
      var remainingTime = this.getTimeUntilDeadline(projectIndexInList)
      if(remainingTime === -1){
        return -1
      }else{
        var effortPerWeek = remainingEffort/(remainingTime / 7)
      }
      var availableWorkingPower = 0
      availableWorkingPower = await this.getAvailableWorkingPower(projectIndexInList)
      return (availableWorkingPower < effortPerWeek)

    },

    async calculatePercentageOfProjectsNeedMoreEmployees(){
      var nrProjects = 0;
      var nrProjectsNeedEmployees = 0;
      for(var i =0;i<this.projects.length;i++) {
        // eslint-disable-next-line no-console
        if (await this.projectNeedsMoreEmployees(i) !== -1) {

          if ( await this.projectNeedsMoreEmployees(i)) {
            nrProjects += 1
            nrProjectsNeedEmployees += 1
            this.projectsNeedMoreEmployees.push(this.projects[i])
          } else {
            nrProjects += 1
          }
        }
      }
      this.percentageProjectsNeedEmployees = (nrProjectsNeedEmployees/nrProjects)*100

    },

    calculateCancelled: function () {
      let today = new Date()
      let countRunning = 0
      let countCancelled = 0
      for (let i = 0; i < this.projects.length; i++) {
        let deadline = new Date(this.projects[i].plannedEnd)
        if (today.getTime() > deadline.getTime()) {
          countCancelled += 1
        } else {
          countRunning += 1
        }
      }
      return countCancelled/(countRunning+countCancelled)*100
    },

    calculateWorkedHours(timeRegistration){
      let startDate = Date.parse(timeRegistration.start);
      let endDate = Date.parse(timeRegistration.end);
      return Math.abs(endDate - startDate) / 36e5;
    },

    async getWorkedHoursInLastMonth(employeeID){
      var relevantTimeRegistrations = []
      var today = new Date();
      today = today.getTime()
      var sum = 0;
      await axios.get(`http://localhost:8080/timeregistrations/`)
          .then(response => {
            for(var i=0;i<response.data.length;i++){
              if(response.data[i].employeeID === employeeID){
                var date = new Date(response.data[i].start)
                date = date.getTime()
                if((today - 2592000000) < date){
                  relevantTimeRegistrations.push(response.data[i])
                }
              }

            }


          })
          .catch(e => {

            this.errors.push(e)
          })

      for(var i=0;i<relevantTimeRegistrations.length;i++){
        var hours = this.calculateWorkedHours(relevantTimeRegistrations[i])
        sum += hours
      }
      return sum
    },

    async calculatePercentageOfOverloadedEmployees(){
      var nrOfEmployees = this.employees.length
      var nrOfOverloadedEmployees = 0
      var nrOfUnderloadedEmployees = 0
      for(var i = 0; i<this.employees.length;i++){
        var workedHours = await this.getWorkedHoursInLastMonth(this.employees[i].employeeID)
        var plannedWorkingHours = this.employees[i].workingHoursPerWeek
        if(workedHours > (plannedWorkingHours * 1.1)){
          nrOfOverloadedEmployees += 1;
          this.employeesOverloaded.push(this.employees[i])
        }else if(workedHours < (plannedWorkingHours * 0.8)){
          nrOfUnderloadedEmployees += 1;
          this.employeesUnderloaded.push(this.employees[i])
        }
      }
      this.percentageOverloadedEmployees = (nrOfOverloadedEmployees/nrOfEmployees) * 100
      this.percentageUnderloadedEmployees = (nrOfUnderloadedEmployees/nrOfEmployees) * 100



    }

  }
}
</script>