<template>
  <div>
    <vs-card class="cardx">
      <div slot="header">
        <h4 style="color:#007bff;">
          Import Time Registrations from CSV File
        </h4>
      </div>
      <div style="display:flex;">
        <span v-show="!this.selectedFile" class="filewrap">
      <h5 class="mb-2"> Drag and Drop a File here or Click !</h5>
          <input type="file" style="width: 500px;" id="fsile" accept=".csv" ref="file" v-on:change="handleFileUpload()">
      </span>
        <p  v-show="this.selectedFile"> TODO : READ NAME FROM INPUT </p>

        <vs-button v-show="this.selectedFile" @click="submit()" class="float-right ml-2" color="success" type="filled">
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
      </div>
      <div class="table-responsive">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Employee ID</th>
            <th class="border-top-0">Project ID</th>
            <th class="border-top-0">From</th>
            <th class="border-top-0">To</th>
            <th class="border-top-0">Brief Description</th>
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
      name:"",
      allTimeRegistrationFromCSV: [],
      timeRegistrationsToUpload: [],
      selectedFile: false,
    }
  },


  methods: {
    numberOfTimeRegistrations() {
      let sum = 0;
      for (let i = 0; i < this.timeRegistrationsToUpload.length; i++) {
        // if one cell has the value null or is empty it will now be uploaded
        if (!this.timeRegistrationsToUpload[i].includes("") && !this.timeRegistrationsToUpload[i].includes(null))
          sum += 1;
      }
      return sum;
    },
    submit: async function () {
      for (let i = 0; i < this.timeRegistrationsToUpload.length; i++) {
        // eslint-disable-next-line no-console
        console.log(this.timeRegistrationsToUpload[i][0].localeCompare("") !== 0)
        if (this.timeRegistrationsToUpload[i][0].localeCompare("") !== 0) {
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
      this.selectedFile = false
    },
    handleFileUpload: function () {
      this.selectedFile = true;
      /* return first object in FileList */
      var file = event.target.files[0];
      this.$papa.parse(file, {
        header: true,

        complete: (results) => {
          let allTimeRegistrationFromCSV = [];
          for (let i = 0; i < results.data.length; i++) {
            let sumOfStartAndDuration = parseFloat(results.data[i]["work_ids/time"]) + parseFloat(results.data[i]["work_ids/hours"])
            let hours = Math.floor(sumOfStartAndDuration)
            let minutes = (sumOfStartAndDuration - hours) * 60
            let starttime = results.data[i]["work_ids/date"] + " " + results.data[i]["work_ids/time"];
            let endtime = (results.data[i]["work_ids/date"] + " " + hours + ":" + minutes).padEnd(16, '0');
            let currentTimeRegistration = [];

            // only the data needed in the database will be parsed
            currentTimeRegistration.push(results.data[i]["id"])
            currentTimeRegistration.push(results.data[i]["project id"])
            currentTimeRegistration.push(starttime)
            currentTimeRegistration.push(endtime)
            currentTimeRegistration.push(results.data[i]["work_ids/name"]);
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
      return this.timeRegistrationsToUpload[id][2].localeCompare("") !== 0;
    },

    undo: function (id) {
      // the loops goes until 5 because we have 4 elements in the arrays
      for (let i = 0; i < 5; i++) {
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
<style>.filewrap {
  position: relative;
  background: #17a2b8;
  border: 1px solid #007bff;
  padding: 15px 100px;
  color: #fff;
  font-family: sans-serif;
  font-size: 14px;
  font-weight: bold;
}

input[type="file"] {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  opacity: 0;
  cursor: pointer;
}</style>