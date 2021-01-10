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
        <vs-button @click="submit()" class="float-right ml-2" color="success" type="filled"> Submit</vs-button>
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
          </tr>
          </thead>
          <tbody>
          <tr v-for="registration in timeRegistrationCSV" :key="registration.id">
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
      timeRegistrationCSV: [[1,8,"2021-01-06 14:00", "2020-12-06 14:30","TEST NEW API 55555 "],
        [3,8,"2021-12-06 14:00", "2021-12-06 14:50","TEST NEW API"]],
      selectedFile :false,
    }
  },
  methods: {
    submit: async function (){
      for (let i=0; i<this.timeRegistrationCSV.length;i++){
    await axios.post(`http://localhost:8080/timeregistrations`,
        {
          "employeeID": this.timeRegistrationCSV[i][0],
          "projectID": this.timeRegistrationCSV[i][1],
          "start": this.timeRegistrationCSV[i][2],
          "end": this.timeRegistrationCSV[i][3],
          "description": this.timeRegistrationCSV[i][4]
        }).then(() => {
      axios.get(`http://localhost:8080/projects/` + parseInt(this.timeRegistrationCSV[i][1])).then(response => {
        var startDate = new Date("1970-01-01 "+this.timeRegistrationCSV[i][2].slice(this.timeRegistrationCSV[i][2].length -5 ));
        var endDate = new Date("1970-01-01 "+this.timeRegistrationCSV[i][3].slice(this.timeRegistrationCSV[i][3].length -5 ));
        let hours = ((endDate - startDate) / 36e5);
        // eslint-disable-next-line no-console
        console.log(hours)
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
        })}
      this.selectedFile = false},
    handleFileUpload: function () {
      this.selectedFile = true;
      /* return first object in FileList */
      var file = event.target.files[0];
      this.$papa.parse(file, {
        header: true,

        complete: function (results) {
          // eslint-disable-next-line no-console
          console.log(results);
          // eslint-disable-next-line no-console
          console.log(results.data);
          let y = [];
          for (let i = 0; i < results.data.length; i++) {
            let sample = parseFloat(results.data[i]["work_ids/time"]) + parseFloat(results.data[i]["work_ids/hours"])
            let hours = Math.floor(sample)
            let minutes = (sample - hours) * 60
            let starttime = results.data[i]["work_ids/date"] + " " + results.data[i]["work_ids/time"];
            let endtime = results.data[i]["work_ids/date"] + " " + hours + ":" + minutes;
            let currentTimeRegistratin = [];
            currentTimeRegistratin.push(results.data[i]["id"])
            currentTimeRegistratin.push(results.data[i]["project id"])
            currentTimeRegistratin.push(starttime)
            currentTimeRegistratin.push(endtime)
            currentTimeRegistratin.push(results.data[i]["work_ids/name"])
y.push(currentTimeRegistratin)
// eslint-disable-next-line no-console
         //   console.log(currentTimeRegistratin)
          }
          this.timeRegistrationCSV = y;

          // eslint-disable-next-line no-console
          console.log(y)
          // eslint-disable-next-line no-console
          console.log(y[1][4])


        }
      })
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