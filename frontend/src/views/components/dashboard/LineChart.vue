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
      months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
        "November", "December"],
      orderOfMonthsToDisplay: [],
      allTimeRegistrations: [],
      allTimeRegistrationsPerMonth: [[], [], [], [], [], [], [], [], [], [], [], []],
      sum: [],
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
    async fetchAllTimeRegistrations() {
      await axios.get(`http://localhost:8080/timeregistrations/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allTimeRegistrations = response.data

          })
    },

    setTimeRegistrationsMonth() {
      let month = new Date().getMonth()
      let previousYear = new Date().getFullYear()
      // get only time registration from this year or previous one
      this.allTimeRegistrations = this.allTimeRegistrations.filter(time => new Date(time.start).getFullYear() === previousYear ||
          new Date(time.start).getFullYear() === previousYear-1)
      for (let i = 0; i < this.allTimeRegistrations.length; i++) {
        this.allTimeRegistrationsPerMonth[parseInt(this.allTimeRegistrations[i].start.slice(5, 7)) - 1].push(this.allTimeRegistrations[i])
      }

      for (let i = 0; i < this.allTimeRegistrationsPerMonth.length; i++) {
        let summe = 0;
        for (let j = 0; j < this.allTimeRegistrationsPerMonth[i].length; j++) {
          // eslint-disable-next-line no-console
          console.log("TEST1")
          let startDate = Date.parse(this.allTimeRegistrationsPerMonth[i][j].start);
          let endDate = Date.parse(this.allTimeRegistrationsPerMonth[i][j].end);
          let hours = Math.abs(endDate - startDate) / 36e5;
          let price = hours * this.getPricePerHour(this.allTimeRegistrationsPerMonth[i][j].projectID)
          summe += price;
        }
        if (month>11) month = 0;
        this.sum.splice(month-1, 0, summe)
        month ++;
      }
    },

    getPricePerHour(projectID) {
      for (let i = 0; i < this.projects.length; i++) {
        if (this.projects[i].projectNumber === projectID)
          return parseFloat(this.projects[i].pricePerHour)
      }
    },


  },


  async mounted() {
    await this.fetchAllProjects()
    await this.fetchAllTimeRegistrations()
    let month = new Date().getMonth() + 1
    for (let i = 0; i < this.months.length; i++) {
      if (month > 11) month = 0
      this.orderOfMonthsToDisplay.splice(i, 0, this.months[month])
      month = month + 1;
    }

    this.setTimeRegistrationsMonth()


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
          labels: this.orderOfMonthsToDisplay,
          datasets: [
            {
              label: "Price",
              borderColor: "#2962ff",
              pointBackgroundColor: "white",
              borderWidth: 2,
              backgroundColor: this.gradient,
              data: this.sum
            },

          ]
        },
        {responsive: true, maintainAspectRatio: false}
    );
  }
};
</script>