<template>
  <div>
    <vs-card class="cardx">
      <div slot="header">
        <h4 style="color:#007bff;">
          Import Time Registrations from CSV File
        </h4>
      </div>
      <div style="display:flex;">
        <vs-input type="file" style="width: 500px;" id="file" ref="file" v-on:change="handleFileUpload()"></vs-input>
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
          <tr v-for="(registration,index)  in deletedTimeRegistrations"
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
                <vs-button @click="deleteTimeRegistration(index)" v-show="deletedRegistrations(index)" icon="delete" class="m-1" color="danger"
                           type="filled">
                  Delete
                </vs-button>

                <vs-button @click="undo(index)" v-show="!deletedRegistrations(index)" icon="cancel" class="m-1" color="warning"
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
      timeRegistrationCSV: [],
      deletedTimeRegistrations: [],
      selectedFile: false,
    }
  },

  methods: {

    submit: async function () {
      for (let i = 0; i < this.deletedTimeRegistrations.length; i++) {
        // eslint-disable-next-line no-console
        console.log(this.deletedTimeRegistrations[i][0].localeCompare("")!==0)
        if (this.deletedTimeRegistrations[i][0].localeCompare("")!==0){
        await axios.post(`http://localhost:8080/timeregistrations`,
            {
              "employeeID": this.deletedTimeRegistrations[i][0],
              "projectID": this.deletedTimeRegistrations[i][1],
              "start": this.deletedTimeRegistrations[i][2],
              "end": this.deletedTimeRegistrations[i][3],
              "description": this.deletedTimeRegistrations[i][4]
            }).then(async () => {

          // eslint-disable-next-line no-console
        //      console.log("Hallo")

          await axios.get(`http://localhost:8080/projects/` + parseInt(this.deletedTimeRegistrations[i][1])).
          then(response => {
            var startDate = new Date("1970-01-01 " + this.deletedTimeRegistrations[i][2].slice(this.deletedTimeRegistrations[i][2].length - 5));
            var endDate = new Date("1970-01-01 " + this.deletedTimeRegistrations[i][3].slice(this.deletedTimeRegistrations[i][3].length - 5));
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
      }}
      this.selectedFile = false
    },
    handleFileUpload: function () {
      this.selectedFile = true;
      /* return first object in FileList */
      var file = event.target.files[0];
      this.$papa.parse(file, {
        header: true,

        complete: (results) => {
          let timeRegistrationCSV = [];
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
            timeRegistrationCSV.push(currentTimeRegistration)
          }
          // save the data in the local variable so that http requests and the preview will be possible
          this.timeRegistrationCSV = timeRegistrationCSV;
          //make deep copy so that not the two arrays will be changed when one of them has been changed
          this.deletedTimeRegistrations = JSON.parse(JSON.stringify(this.timeRegistrationCSV));

        }
      })
    },

    deleteTimeRegistration: function (id) {

      // this.$data._deletedTimeRegistrations.push(this.timeRegistrationCSV[id])
      for (let i = 0; i < this.deletedTimeRegistrations[id].length; i++) {
        this.$set(this.deletedTimeRegistrations[id],i,"" );
      }
    },

    deletedRegistrations: function(id) {
      return this.deletedTimeRegistrations[id][2].localeCompare("")!==0;
    },

    undo: function (id){
      // the loops goes until 5 because we have 4 elements in the arrays
      for (let i=0;i<5;i++){
      this.$set(this.deletedTimeRegistrations[id],i,this.timeRegistrationCSV[id][i]) }
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