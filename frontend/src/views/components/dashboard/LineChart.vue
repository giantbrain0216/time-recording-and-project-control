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
      allTimeRegistrationsPerMonthThisYear: [[], [], [], [], [], [], [], [], [], [], [], []],
      allTimeRegistrationsPerMonthLastYear: [[], [], [], [], [], [], [], [], [], [], [], []],
      sum: [],
      sumLastYear: [],
      projectName: [],

    };
  },

  methods: {



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

    setTimeRegistrationsMonthYear() {
      let month = new Date().getMonth()
      let year = new Date().getFullYear()
      // get only time registration from this year
      let timeRegistrationsThisYear = this.allTimeRegistrations.filter(time => new Date(time.start).getFullYear() === year )
      /* get only time registrations from last year, that have been done in the actual month to not show them
        for example : the time registrations in march 2020 should not be showed when the time registrations
        from 2021 are being displayed
       */
      let timeRegistrationsPreviousYearToDisplay = this.allTimeRegistrations.filter(time =>((new Date(time.start).getFullYear() === year-1)
          &&(new Date(time.start).getMonth()>month)))
      let allTimeRegistrationsToDisplay = timeRegistrationsPreviousYearToDisplay.concat(timeRegistrationsThisYear)
      for (let i = 0; i < allTimeRegistrationsToDisplay.length; i++) {
        this.allTimeRegistrationsPerMonthThisYear[parseInt(allTimeRegistrationsToDisplay[i].start.slice(5, 7)) - 1]
            .push(allTimeRegistrationsToDisplay[i])
      }

      for (let i = 0; i < this.allTimeRegistrationsPerMonthThisYear.length; i++) {
        let summe = 0;
        for (let j = 0; j < this.allTimeRegistrationsPerMonthThisYear[i].length; j++) {
          let startDate = Date.parse(this.allTimeRegistrationsPerMonthThisYear[i][j].start);
          let endDate = Date.parse(this.allTimeRegistrationsPerMonthThisYear[i][j].end);
          let hours = Math.abs(endDate - startDate) / 36e5;
          let price = hours * this.getPricePerHour(this.allTimeRegistrationsPerMonthThisYear[i][j].projectID)
          summe += price;
        }
        if (month>11) month = 0;
        this.sum.splice(this.orderOfMonthsToDisplay.indexOf(this.months[i]), 0, summe)
        month ++;
      }
    },

    setTimeRegistrationsMonthLastYear() {
      let month = new Date().getMonth()
      let year = new Date().getFullYear()
      // get only time registration from this year
      let timeRegistrationsThisYear = this.allTimeRegistrations.filter(time => new Date(time.start).getFullYear() === year -1)
      /* get only time registrations from last year, that have been done in the actual month to not show them
        for example : the time registrations in march 2020 should not be showed when the time registrations
        from 2021 are being displayed
       */
      let timeRegistrationsPreviousYearToDisplay = this.allTimeRegistrations.filter(time =>((new Date(time.start).getFullYear()
          === year-2)
          &&(new Date(time.start).getMonth()>month)))
      let allTimeRegistrationsToDisplay = timeRegistrationsPreviousYearToDisplay.concat(timeRegistrationsThisYear)
      for (let i = 0; i < allTimeRegistrationsToDisplay.length; i++) {
        this.allTimeRegistrationsPerMonthLastYear[parseInt(allTimeRegistrationsToDisplay[i].start.slice(5, 7)) - 1]
            .push(allTimeRegistrationsToDisplay[i])
      }

      for (let i = 0; i < this.allTimeRegistrationsPerMonthLastYear.length; i++) {
        let summe = 0;
        for (let j = 0; j < this.allTimeRegistrationsPerMonthLastYear[i].length; j++) {
          let startDate = Date.parse(this.allTimeRegistrationsPerMonthLastYear[i][j].start);
          let endDate = Date.parse(this.allTimeRegistrationsPerMonthLastYear[i][j].end);
          let hours = Math.abs(endDate - startDate) / 36e5;
          let price = hours * this.getPricePerHour(this.allTimeRegistrationsPerMonthLastYear[i][j].projectID)
          summe += price;
        }
        if (month>11) month = 0;
        this.sumLastYear.splice(this.orderOfMonthsToDisplay.indexOf(this.months[i]), 0, summe)
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
    // it has been incremented by 1 so that the actual month will be the last displayed in the chart
    let month = new Date().getMonth() + 1
    for (let i = 0; i < this.months.length; i++) {
      if (month > 11) month = 0
      this.orderOfMonthsToDisplay.splice(i, 0, this.months[month])
      month = month + 1;
    }

    this.setTimeRegistrationsMonthYear()
    this.setTimeRegistrationsMonthLastYear()

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
              label: "Revenue This Year",
              borderColor: "#2962ff",
              pointBackgroundColor: "white",
              borderWidth: 2,
              backgroundColor: this.gradient,
              data: this.sum
            },
            {
              label: "Revenue Last Year",
              borderColor: "#05CBE1",
              pointBackgroundColor: "white",
              borderWidth: 2,
              backgroundColor: this.gradient2,
              data: this.sumLastYear
            },

          ]
        },
        {responsive: true, maintainAspectRatio: false}
    );
  }
};
</script>
