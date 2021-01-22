<script>

import {Line} from "vue-chartjs";
import axios from "axios";

export default {
  extends: Line,
  data: () => {
    return {
      gradient: null,
      gradient2: null,
      projects: [],
      projectName: [],
      progress: [],

    };
  },

  methods: {

    getProgress: function () {
      for (let i = 0; i < this.projects.length; i++) {
        let progress = (this.projects[i].performedEffort * 100 / this.projects[i].plannedEffort).toFixed(2)
        this.progress.push(progress)
      }
    },
    /**
     * Gets all projects from DB
     */
    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/projects/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
            this.projectName = response.data.map(project => project.projectName)
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },
  }
  ,


  async mounted() {
    await this.fetchAllProjects()
    await this.getProgress()
    this.gradient = this.$refs.canvas
        .getContext("2d")
        .createLinearGradient(0, 0, 0, 450);
    this.gradient2 = this.$refs.canvas
        .getContext("2d")
        .createLinearGradient(0, 0, 0, 450);

    this.gradient.addColorStop(0, "rgba(41, 98, 255, 0.5)");
    this.gradient.addColorStop(0.5, "rgba(41, 98, 255, 0.25)");
    this.gradient.addColorStop(1, "rgba(255, 255, 255, 0)");

    this.gradient2.addColorStop(0, "rgba(0, 231, 255, 0.9)");
    this.gradient2.addColorStop(0.5, "rgba(0, 231, 255, 0.25)");
    this.gradient2.addColorStop(1, "rgba(0, 231, 255, 0)");


    this.renderChart(
        {
          labels: this.projectName,
          datasets: [
            {
              label: "Progress",
              borderColor: "#2962ff",
              pointBackgroundColor: "white",
              borderWidth: 2,
              backgroundColor: this.gradient,
              data: this.progress
            },

          ]
        },
        {responsive: true, maintainAspectRatio: false}
    );
  }
};
</script>