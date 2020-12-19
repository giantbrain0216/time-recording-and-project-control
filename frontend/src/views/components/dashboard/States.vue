<template>
    <vs-row>
        <vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">Worked {{getPerformedEffort()}} hours of {{getPlannedEffort()}} hours</h4>
                <span>Total Sales</span>
                <vs-progress :percent="(getPerformedEffort()/getPlannedEffort())*100" color="primary">primary</vs-progress>
            </vs-card>
        </vs-col>
        <!--vs-col vs-lg="3" vs-xs="12">
            <vs-card>
                <h4 class="mb-1">-10%</h4>
                <span>Monthly Sales</span>
                <vs-progress :percent="10" color="danger">primary</vs-progress>
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
                <h4 class="mb-1">30%</h4>
                <span>Company Growth</span>
                <vs-progress :percent="30" color="warning">primary</vs-progress>
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
      this.fetchAllProjects()
      this.projects.forEach((project) => plannedEffort += parseInt(project.plannedEffort))
      return plannedEffort;
    },

    getPerformedEffort: function () {
      let performedEffort = 0
      this.fetchAllProjects()
      this.projects.forEach((project) => performedEffort += parseInt(project.performedEffort))
      return performedEffort
    },

    getProjectsProgress: function () {
      this.progressBar = (this.performedEffort * 100) / this.plannedEffort
      return parseInt(this.progressBar)
    }
  }


}
</script>