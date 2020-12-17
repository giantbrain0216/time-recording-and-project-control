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
     employeeID: async function() { // watch it
       var arr = [0,0,0,0,0];
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
            for (var i = 0; i < response.data.length; i++) {
              if(response.data[i].employeeID === this.employeeID){
                if(this.dateLabels.includes(response.data[i].start.slice(5,10))){
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
      var today = new Date();
      for(var i = 0;i<5;i++){
        var dd = String(today.getDate()-4+i).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        this.dateLabels[i] =  mm + "-" + dd
      }
    },
    async getWorkedHours(){
      for(var i = 0; i < this.registrations.length;i++){
        for(var j= 0; j<this.dateLabels.length;j++){
          if(this.registrations[i].start.slice(5,10).localeCompare(this.dateLabels[j]) === 0){
            var startDate = Date.parse(this.registrations[i].start);
            var endDate = Date.parse(this.registrations[i].end);
            var hours = Math.abs(endDate - startDate) / 36e5;
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
