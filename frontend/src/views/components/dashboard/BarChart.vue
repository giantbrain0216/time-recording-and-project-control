<script>
import { Bar } from "vue-chartjs";
import axios from "axios";

export default {
  extends: Bar,
  props: {employeeID:Number},

  data: () => {
    return {
      registrations:[],
      dateLabels:["","","","",""],
      workedHours:[0,0,0,0,0],
      errors:[],
    };
  },

  watch: {
     employeeID: async function() {
       let arr = [0,0,0,0,0];
      this.workedHours = arr.splice(0);
      this.registrations = [];
      await this.fetchRegistrations();
      await this.getWorkedHours();
      this.renderChart(
          {
            labels: this.dateLabels,
            datasets: [
              {
                label: "Worked Time in Hours",
                backgroundColor: "#2962ff",
                data: this.workedHours,
              }
            ]
          },
          { responsive: true, maintainAspectRatio: false, scales: {
              xAxes: [{
                barThickness: 20,
              }]
            } }
      );
    }
  },

  methods: {
    async fetchRegistrations(){
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
            // JSON responses are automatically parsed.
            for (let i = 0; i < response.data.length; i++) {
              if(response.data[i].employeeID === this.employeeID){
                if(this.dateLabels.includes(response.data[i].start.slice(0,10))){
                  this.registrations.push(response.data[i])
                }

              }
            }
          })
          .catch(e => {
            this.errors.push(e)
          })

    },
    async getDateLabels(){
      let today = new Date();
      for(let i = 0;i<5;i++){
        let dd = String(today.getDate()-4+i).padStart(2, '0');
        let mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        this.dateLabels[i] =  yyyy + "-" +  mm + "-" + dd
      }
    },
    async getWorkedHours(){
      for(let i  = 0; i < this.registrations.length;i++){
        for(let j= 0; j<this.dateLabels.length;j++){
          // eslint-disable-next-line no-console
          console.log(this.registrations[i].start.slice(0,10))
          if(this.registrations[i].start.slice(0,10).localeCompare(this.dateLabels[j]) === 0){
            let startDate = Date.parse(this.registrations[i].start);
            let endDate = Date.parse(this.registrations[i].end);
            let hours = Math.abs(endDate - startDate) / 36e5;
            this.workedHours[j] = this.workedHours[j] + hours
          }

        }
      }

    },
  },


 async mounted() {
    await this.getDateLabels();
    await this.fetchRegistrations();
    await this.getWorkedHours();
    this.renderChart(
      {
        labels: this.dateLabels,
        datasets: [
          {
            label: "Worked Time in Hours",
            backgroundColor: "#2962ff",
            data: this.workedHours,
          }
        ]
      },
      { responsive: true, maintainAspectRatio: false, scales: {
              xAxes: [{
                  barThickness: 20,
              }]
          } }
    );
    this.workedHours = [0,0,0,0,0];
    this.registrations = []

  }
};
</script>
