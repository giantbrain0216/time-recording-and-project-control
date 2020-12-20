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
            </vs-card>
        </vs-col>
        <vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">50%</h4>
                <span>Yearly Sales</span>
                <vs-progress :percent="50" color="success">primary</vs-progress>
            </vs-card>
        </vs-col>
        <vs-col-- vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">{{ Math.round(calculateCancelled())}}%</h4>
                <span>Projects are cancelled</span>
                <vs-progress :percent="calculateCancelled()" color="warning">primary</vs-progress>
            </vs-card>
        </vs-col-->
    </vs-row>    
</template>
<script>
import axios from "axios";

export default {
  name: "States",
  data: () => {
    return {
      projects: [],
      percentageProjectsNeedEmployees:null,
    }
  },

  async created() {
    await this.fetchAllProjects()
    this.calculatePercentageOfProjectsNeedMoreEmployees()
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
        console.log(await this.projectNeedsMoreEmployees(i))
        if (await this.projectNeedsMoreEmployees(i) !== -1) {

          if ( await this.projectNeedsMoreEmployees(i)) {
            nrProjects += 1
            nrProjectsNeedEmployees += 1
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
  }
}
</script>