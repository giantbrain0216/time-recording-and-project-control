<template>
    <vs-row>
        <vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">{{Math.round(getProgressOfAll())}}% Done</h4>
                <span>Total Poject progress</span>
                <vs-progress :percent="(getPerformedEffort()/getPlannedEffort())*100" color="primary">primary</vs-progress>
            </vs-card>
        </vs-col>
        <vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">{{Math.round(calculateCancelledProjects())}}%</h4>
                <span>Projects are cancelled</span>
                <vs-progress :percent="calculateCancelledProjects()" color="danger">primary</vs-progress>
            </vs-card>
        </vs-col>
        <!--vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">50%</h4>
                <span>Yearly Sales</span>
                <vs-progress :percent="50" color="success">primary</vs-progress>
            </vs-card>
        </vs-col>
        <vs-col-- vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">30%</h4>
                <span>Company Growth</span>
                <vs-progress :percent="30" color="warning">primary</vs-progress>
            </vs-card>
        </vs-col-->
    </vs-row>    
</template>
<script>
import axios from "axios";

let countRunning = 0
let countCancelled = 0
export default {
  name: "States",
  data: () => {
    return {
      projects: [],
    }
  },

  created() {
    this.fetchAllProjects()
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

    calculateCancelledProjects: function () {
      let today = new Date()
      for (let i = 0; i < this.projects.length; i++) {
        let deadline = new Date(this.projects[i].plannedEnd)
        if (today.getTime() > deadline.getTime()) {
          // eslint-disable-next-line no-console
          console.log("Returning false")
          countRunning += 1
        } else {
          // eslint-disable-next-line no-console
          console.log("Returning true")
          countCancelled += 1
        }
      }
      return countRunning / countCancelled * 100
    },
  }
}
</script>