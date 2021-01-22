<template>
  <div>
    <vs-card class="cardx">
      <div slot="header">
        <h4 style="color:#007bff;">
          Import Time Registrations from Ticket System
        </h4>
      </div>


      <div style="display:flex;">
        <input type="file" style="width: 500px;" id="fsile" accept=".csv" ref="file" v-on:change="handleFileUpload()">
        <vs-button v-show="this.selectedFile" @click="submit()" v-bind:disabled="numberOfTimeRegistrations() === 0"
                   class="float-right ml-2" color="success" type="filled">
          Submit
        </vs-button>
      </div>

    </vs-card>
    <vs-card v-show="this.selectedFile" class="cardx">
      <div slot="header">
        <h4 style="color:#007bff;">
          Preview
        </h4>
        <h6 class="mt-2" style="color: #28a745">{{ numberOfTimeRegistrations() }} Time Registrations will be uploaded
          !</h6>
        <h6 class="mt-2" style="color: red">{{ this.allTimeRegistrationFromCSV.length - numberOfTimeRegistrations() }}
          Time Registrations will not be uploaded. See Remarks !</h6>
      </div>
      <div class="table-responsive">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Employee ID</th>
            <th class="border-top-0">Project ID</th>
            <th class="border-top-0">From</th>
            <th class="border-top-0">To</th>
            <th class="border-top-0">Link</th>
            <th class="border-top-0">Brief Description</th>
            <th class="border-top-0">Remarks</th>
            <th class="border-top-0">Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(registration,index)  in timeRegistrationsToUpload"
              :key="registration.index"
          >
            <td>
              <div class="d-flex align-items-center">
                <div class="mr-2">{{ registration[0] }}</div>
              </div>
            </td>
            <td>
              <div class="mr-2">{{ registration[1] }}</div>
            </td>
            <td>
              <div class="d-flex align-items-center">{{ registration[2] }}</div>
            </td>
            <td>{{ registration[3] }}</td>
            <td>{{ registration[4] }}</td>
            <td>{{ registration[5] }}</td>
            <td><p style="color:red;" v-show="!validTimeRegistration(index)"> This Time Registration will not be
              uploaded
              <br>Either the Employee ID or the Project ID does not exist
              or you may have deleted it. </p></td>
            <td>
              <div>
                <vs-button @click="deleteTimeRegistration(index)" v-show="deletedRegistrations(index)" icon="delete"
                           class="m-1" color="danger"
                           type="filled">
                  Delete
                </vs-button>

                <vs-button @click="undo(index)" v-show="!deletedRegistrations(index)" icon="cancel" class="m-1"
                           color="warning"
                           type="filled">
                  Undo
                </vs-button>


              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </vs-card>
  </div>
</template>

<script>

import axios from "axios";

export default {
  name: "csv-import",
  data: () => {
    return {
      allProjectIDs: [],
      allEmployeeIDs: [],
      allTimeRegistrationFromCSV: [],
      timeRegistrationsToUpload: [],
      selectedFile: false,
    }
  },

  async created() {
    await this.fetchAllEmployees()
    await this.fetchAllProjectIDs()
  },
  methods: {

    download_csv: async function () {
      var csv = 'employee id,project id,work_ids/date,work_ids/time,work_ids/hours,work_ids/name\n';
      var hiddenElement = document.createElement('a');
      hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
      hiddenElement.target = '_blank';
      hiddenElement.download = 'template.csv';
      hiddenElement.click();
    },

    fetchAllProjectIDs: async function () {
      await axios.get(`http://localhost:8080/projects`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allProjectIDs = response.data.map(project => project.projectNumber)
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },
    fetchAllEmployees: async function () {
      await axios.get(`http://localhost:8080/employees`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.allEmployeeIDs = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },
// in the first index we save the employee id and in the seconde one the project id
    validTimeRegistration(index) {
      return (!this.timeRegistrationsToUpload[index].includes("") && !this.timeRegistrationsToUpload[index].includes(null)
          && parseInt(this.timeRegistrationsToUpload[index][0]) !== 0 && this.allProjectIDs
              .includes(parseInt(this.timeRegistrationsToUpload[index][1])))
    },
    numberOfTimeRegistrations() {
      let sum = 0;
      for (let i = 0; i < this.timeRegistrationsToUpload.length; i++) {
        // if one cell has the value null or is empty it will now be uploaded
        let valid = this.validTimeRegistration(i)
        if (valid)
          sum += 1;
      }
      return sum;
    },
    submit: async function () {
      for (let i = 0; i < this.timeRegistrationsToUpload.length; i++) {
        // eslint-disable-next-line no-console
        if (this.validTimeRegistration(i)) {
          await axios.post(`http://localhost:8080/timeregistrations`,
              {
                "employeeID": this.timeRegistrationsToUpload[i][0],
                "projectID": this.timeRegistrationsToUpload[i][1],
                "start": this.timeRegistrationsToUpload[i][2],
                "end": this.timeRegistrationsToUpload[i][3],
                "description": this.timeRegistrationsToUpload[i][4]
              }).then(async () => {

            // eslint-disable-next-line no-console
            //      console.log("Hallo")

            await axios.get(`http://localhost:8080/projects/` + parseInt(this.timeRegistrationsToUpload[i][1])).then(response => {
              var startDate = new Date("1970-01-01 " + this.timeRegistrationsToUpload[i][2].slice(this.timeRegistrationsToUpload[i][2].length - 5));
              var endDate = new Date("1970-01-01 " + this.timeRegistrationsToUpload[i][3].slice(this.timeRegistrationsToUpload[i][3].length - 5));
              let hours = ((endDate - startDate) / 36e5);
              // eslint-disable-next-line no-console
              var project = response.data
              project.performedEffort = project.performedEffort + hours
              axios.put(`http://localhost:8080/projects/`, project).then(async () => {
                this.notify("Success", "Registration has been successfully added.", "success")


              }).catch((error) => {
                // handle this error here
                if (error.response) {
                  this.notify("Error", error.message, "danger")
                }
              })

            }).catch((error) => {
              // handle this error here
              if (error.response) {
                this.notify("Error", error.message, "danger")
              }
            })
          })
              .catch((error) => {
                // handle this error here
                if (error.response) {
                  this.notify("Error", error.message, "danger")
                }
              })
        }
      }
      this.$router.push({name: "Time Registration", params: {}});
    },
    handleFileUpload: function () {
      this.selectedFile = true;
      /* return first object in FileList */
      var file = event.target.files[0];
      this.$papa.parse(file, {
        header: true,
        skipEmptyLines: true,
        //  preview: 2,,


        complete: (results) => {
          let allTimeRegistrationFromCSV = [];
          for (let i = 0; i < results.data.length; i++) {

            let startTime = new Date(results.data[i]["Created At (UTC)"]).getTime();
            let endtime = new Date(startTime + parseFloat(results.data[i]["Time Spent"]) * 60 * 60 * 1000)
            /*   let sumOfStartAndDuration = parseFloat(results.data[i]["work_ids/time"]) + parseFloat(results.data[i]["work_ids/hours"])
               let hours = Math.floor(sumOfStartAndDuration)
               let minutes = (sumOfStartAndDuration - hours) * 60*/
            let starttime = new Date(startTime)
            starttime = starttime.getFullYear() + '-' + ('0' + (starttime.getMonth() + 1)).slice(-2) + '-' + ('0' + starttime.getDate()).slice(-2) + ' ' + ('0' + starttime.getHours()).slice(-2) + ':' + ('0' + (starttime.getMinutes())).slice(-2)
            endtime = endtime.getFullYear() + '-' + ('0' + (endtime.getMonth() + 1)).slice(-2) + '-' + ('0' + endtime.getDate()).slice(-2) + ' ' + ('0' + endtime.getHours()).slice(-2) + ':' + ('0' + (endtime.getMinutes())).slice(-2)
            let currentTimeRegistration = [];

            // only the data needed in the database will be parsed
            let employeeID = 0;
            //this.allProjectIDs = this.allProjectIDs.filter(x => x.name === results.data[i]["Author"])
            for (let j=0;j<this.allEmployeeIDs;j++){
              if (this.allProjectIDs[j].name === results.data[i]["Author"])
                employeeID = this.allProjectIDs[j].employeeID
            }

            // eslint-disable-next-line no-console
            console.log(employeeID.toString() )
            currentTimeRegistration.push(this.allProjectIDs[0])
            currentTimeRegistration.push(results.data[i]["project id"])
            currentTimeRegistration.push(starttime)
            currentTimeRegistration.push(endtime)
            currentTimeRegistration.push(results.data[i]["URL"])
            currentTimeRegistration.push(results.data[i]["Description"]);
            allTimeRegistrationFromCSV.push(currentTimeRegistration)
          }
          // save the data in the local variable so that http requests and the preview will be possible
          this.allTimeRegistrationFromCSV = allTimeRegistrationFromCSV;
          //make deep copy so that not the two arrays will be changed when one of them has been changed
          this.timeRegistrationsToUpload = JSON.parse(JSON.stringify(this.allTimeRegistrationFromCSV));

        }
      })
    },

    deleteTimeRegistration: function (id) {

      // this.$data._timeRegistrationsToUpload.push(this.allTimeRegistrationFromCSV[id])
      for (let i = 0; i < this.timeRegistrationsToUpload[id].length; i++) {
        this.$set(this.timeRegistrationsToUpload[id], i, "");
      }
    },

    deletedRegistrations: function (id) {
      return this.timeRegistrationsToUpload[id][1].localeCompare("") !== 0;
    },

    undo: function (id) {
      // the loops goes until 5 because we have 4 elements in the arrays
      for (let i = 0; i < 6; i++) {
        this.$set(this.timeRegistrationsToUpload[id], i, this.allTimeRegistrationFromCSV[id][i])
      }
    },
    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },

  },
}
</script>