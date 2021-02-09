<template>
  <div>
  <vs-button @click="activePrompt=true"  icon="get_app" class="m-1" color="success" type="filled">Invoice
  </vs-button>

    <vs-prompt
        title="Choose Data before Invoice Export"
        color="success"
        @cancel='activePrompt=false;resetValues()'
        @accept='download'
        @close='activePrompt=false;resetValues()'
        :is-valid="validInput(startDateInput,endDateInput,currentSelectedProject)"
        :active.sync="activePrompt"
    >

      <div class="con-exemple-prompt">
<!--        Please Modify Client Data-->
<!--      </div>-->
      <div class="mt-2">
        <div id="app">
          <autocomplete
              :search="search"
              :get-result-value="getResultValue"
              @submit="handleSubmit"
              placeholder="Search for a project"
              aria-label="Search for a project"
              auto-select
          ></autocomplete>
        </div>
      </div>
      <div v-if='currentSelectedProject.hasOwnProperty("projectNumber")' style="color:rgb(32,193,53)" class="mt-2">
        {{"Selected Project: " + currentSelectedProject.projectName}}
      </div>
      <div v-if='projectNotStarted' class="mt-2" style="color:rgb(198,46,46)">The Project has not started yet, please choose another project</div>
      <hr/>
      <div>
        <div style="display:inline-block" class="mr-2">Start Date of Invoice:  </div>
        <div style="display:inline-block" class="mt-2"><input :disabled='!currentSelectedProject.hasOwnProperty("projectNumber")' type="date" id="startDate" name="date"
                                 v-model="startDateInput"
                                 :min="startDateMinimum" :max="dateToday"></div>
      </div>
      <div>
        <div style="display:inline-block" class="mr-2">End Date of Invoice:  </div>
        <div style="display:inline-block" class="mt-2"><input :disabled='!currentSelectedProject.hasOwnProperty("projectNumber")' type="date" id="EndDate" name="date"
                                                              v-model="endDateInput"
                                                              min="2018-01-01" :max="endDateMaximum"></div>
      </div>
      <hr/>
        <h6 class="mb-2 mt-2" >Price Per Hour : <strong style="color: red"> {{ currentSelectedProject.pricePerHour }} € </strong></h6>


        <vs-alert class="mt-3"
          :active="!validInput(startDateInput,endDateInput,currentSelectedProject,pricePerHour)"
          color="success"
          icon="new_releases"
      >
        Please check your input
      </vs-alert>
      </div>
    </vs-prompt>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ExportInvoiceButton",
  props: {clientID:Number},

  data: () => {
    return {
      projects:[],
      timeregistrations:[],
      prices:[],
      dataForCSV : [],
      activePrompt : false,
      projectSelected : false,
      currentSelectedProject:{},
      projectNotStarted:false,
      dateToday:"",
      startDateInput:"",
      endDateInput:"",
      startDateMinimum:"",
      endDateMaximum:"",
      pricePerHour:"",
    };
  },
  async created() {

    await axios.get(`http://localhost:8080/projectsByClient/` + this.clientID)
        .then(async response => {
          var arr = response.data
          for(var i=0;i<arr.length;i++){
            await axios.get(`http://localhost:8080/projects/` + arr[i]).then((response) => {
              if(response.data){
                this.projects.push(response.data)
              }

            })
          }
        })
        .catch((error) => {
          if(error.response){
            this.notify("Projects Database Error", error.message,"danger")
          }})




 /*   await axios.get(`http://localhost:8080/projects/`)
        .then(response => {
          for(var i=0;i<response.data.length;i++){
            if(response.data[i].clientID == this.clientID){
              this.projects.push(response.data[i])
            }
          }
        })
        .catch((error) => {
          if(error.response){
            this.notify("Projects Database Error", error.message,"danger")
          }})*/

    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;
    this.startDateInput = this.dateToday
    this.endDateInput = this.dateToday

  },

  methods: {
    getEmployeeName: async function(id){
      var name = "Name Unknown"
      await axios.get(`http://localhost:8080/employees/${id}`)
          .then(response => {
            name = response.data.name
          }).catch((error) => {
            if (error.response){
              this.notify("Employees Database Error",error.message,"danger")
            }
          })

      return name
    },

    download: async function(){
      await this.getTimeRegistrationsByProjectAndTime();
      await this.prepareArrayForCSV();
      await this.download_csv()
      this.resetValues()
    },

    async getTimeRegistrationsByProjectAndTime(){
      await axios.get(`http://localhost:8080/timeregistrations`)
          .then(response => {
              for(var i =0;i<response.data.length;i++){
                if(response.data[i].projectID === this.currentSelectedProject.projectNumber){
                  if(this.compareDates(new Date(this.startDateInput),new Date(response.data[i].start)) && this.compareDates(new Date(response.data[i].end),new Date(this.endDateInput))){
                    this.timeregistrations.push(response.data[i])
                  }
                }
              }
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    prepareArrayForCSV: async function(){
      // eslint-disable-next-line no-console
      console.log("Length of TimeRegistrations:" + this.timeregistrations.length)
      for(var i=0;i<this.timeregistrations.length;i++){
        var workedHours =Math.abs(new Date(this.timeregistrations[i].end) - new Date(this.timeregistrations[i].start)) / 36e5
        var element = [this.timeregistrations[i].id, await this.getEmployeeName(this.timeregistrations[i].employeeID),
          this.timeregistrations[i].start.substring(0,10),workedHours,this.timeregistrations[i].description.replace(",", "."),
          parseInt((workedHours * this.currentSelectedProject.pricePerHour).toFixed(1)) + " Euro"]
        this.dataForCSV.push(element)
        this.prices.push(parseInt((workedHours * this.currentSelectedProject.pricePerHour).toFixed(1)))
      }
    },

    download_csv:async function () {
      var csv = 'ID, Employee,Date,Hours,Description,Price\n';
      this.dataForCSV.forEach(function (row) {
        csv += row.join(',');
        csv += "\n";
      });

      csv += ',,,,,' + this.prices.reduce(function(a,b){return a + b}, 0) + "Euro \n"

      // eslint-disable-next-line no-console
      console.log(csv);
      var hiddenElement = document.createElement('a');
      hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
      hiddenElement.target = '_blank';
      hiddenElement.download = 'Rechnung_Client_' + this.clientID + '.csv';
      hiddenElement.click();
      this.notify("Success","Invoice was successfully created","success")
    },

    async search(input) {

      if (input.length < 1) { return [] }

      else if(input.length < 2){return this.projects.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.projectName.toLowerCase()
            .startsWith(input.toLowerCase()))
      })}
      else{
        return this.projects.filter(competence => {
          // eslint-disable-next-line no-console
          return (competence.projectName.toLowerCase()
              .includes(input.toLowerCase()))
        })
      }
    },

    getResultValue(result){
      return result.projectName
    },

    handleSubmit(result){
      this.projectNotStarted = false;
      this.currentSelectedProject = {};
      if(this.compareDates(new Date(),new Date(result.plannedStart))){
        this.projectNotStarted = true
      }else{
        this.currentSelectedProject = result;
        this.startDateInput = result.plannedStart.substr(0,10)
        this.startDateMinimum = result.plannedStart.substr(0,10)
        if(this.compareDates(new Date(result.plannedEnd),new Date())){
          this.endDateMaximum = result.plannedEnd.substr(0,10);
          this.endDateInput = result.plannedEnd.substr(0,10)
        }else{
          this.endDateMaximum = this.dateToday;
          this.endDateInput = this.dateToday;
        }
      }

    },

    compareDates(smallerDate,biggerDate){
      return smallerDate.getTime() < biggerDate.getTime();
    },

    validInput: function(startDateInput,endDateInput,currentSelectedProject){
      return (this.compareDates(new Date(startDateInput),new Date(endDateInput)) &&
          currentSelectedProject.hasOwnProperty("projectNumber"))
    },

    /** Shows prompt with title, message and selected color*/
    notify: function(title, message,color){
      this.$vs.notify({
        title: title,
        text: message,
        color:color, type: "gradient",
      })
    },

     resetValues:async function(){
      this.currentSelectedProject = {}
      this.startDateInput = this.dateToday
      this.endDateInput = this.dateToday
      this.startDateMinimum = ""
      this.endDateMaximum = ""
      this.projectNotStarted = false
       this.pricePerHour = ""

    }


  }


}
</script>

<style scoped>
</style>