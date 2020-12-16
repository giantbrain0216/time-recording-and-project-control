<script>
import { Bar } from "vue-chartjs";
import axios from "axios";

export default {
  extends: Bar,
  props: ['clientID'],

  data: () => {
    return {
      registrations:[],
      dateLabels:["","","","",""],
      workedHours:[0,0,0,0,0]
    };
  },

  methods: {
    async fetchRegistrations(){
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
            // JSON responses are automatically parsed.
            for (var i = 0; i < response.data.length; i++) {
              if(response.data[i].id === this.props.clientID){
                if(this.dateLabels.includes(response.data[i].start.slice(5,10))){
                  this.registrations.concat(response.data[i])
                }

              }
            }
          })
          .catch(e => {
            this.errors.push(e)
          })

    },
    getDateLabels(){
      var today = new Date();
      for(var i = 0;i<5;i++){
        var dd = String(today.getDate()-4+i).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        this.dateLabels[i] =  mm + "-" + dd
      }
    },
    getWorkedHours(){
      for(var i = 0; i < this.registrations.length;i++){
        for(var j= 0; j<this.dateLabels.length;j++){
          if(this.registrations[i].start.slice(5,10).localeCompare(this.dateLabels[j])){
            var startDate = Date.parse(this.registrations[j].start);
            var endDate = Date.parse(this.registrations[j].end);
            var hours = Math.abs(endDate - startDate) / 36e5;
            this.workedHours[j] = this.workedHours[j] + hours
          }

        }
      }

    },
  },


  mounted() {
    this.getDateLabels();
    this.fetchRegistrations();
    this.getWorkedHours();

    this.renderChart(
      {
        labels: this.dateLabels,
        datasets: [
          {
            label: "Worked Time $",
            backgroundColor: "#2962ff",
            data: this.workedHours,
          }
        ]
      },
      { responsive: true, maintainAspectRatio: false, scales: {
              xAxes: [{
                  barThickness: 10,
              }]
          } }
    );
  }
};
</script>
