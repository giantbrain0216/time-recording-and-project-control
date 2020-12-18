<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="projectSelected ? 6 : 12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0">Name</th>
              <th class="border-top-0">ID of the Client</th>
              <th class="border-top-0">Deadline</th>
              <th class="border-top-0">Progress</th>
              <th class="border-top-0">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="project in projects" :key="project.projectNumber">
              <td>
                <div class="d-flex align-items-center">
                  <div class="">
                    <a @click="updateProjectDetails(project.projectNumber)" class="m-b-0" style="cursor:pointer">
                      {{ project.projectName }}</a>
                  </div>
                </div>
              </td>
              <td>{{ project.clientID }}</td>
              <!-- <td>{{ project.projectName }}</td> -->
              <td> {{ project.plannedEnd.substring(0, 10) }}</td>
              <td style="text-align: center">
                <vs-progress :percent="project.performedEffort * 100 / project.plannedEffort" color="success">primary
                </vs-progress>
                <i>{{ (project.performedEffort * 100 / project.plannedEffort).toFixed(2) }} %</i></td>
              <td>
                <div>
                  <vs-button @click="showDeletePrompt(project.projectNumber) " class="m-1" color="danger" type="filled">
                    Delete
                  </vs-button>
                  <vs-button @click="updateProjectID(project.projectNumber)" class=" m-1
                  " color="warning" type="filled">
                    Edit
                  </vs-button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </vs-card>
      </vs-col>
      <vs-col v-if="projectSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="projectSelected" class="cardx">
          <div slot="header">
            <h4>Details vom {{ currentProject.projectName }}</h4>
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off"
                       @click="projectSelected = false"></vs-button>
          </div>
          <div>
            <p><strong>ID of the Project: </strong>{{ currentProject.projectNumber }}</p>
            <hr>
            <p><strong>Planned Start: </strong>{{ currentProject.plannedStart.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned End: </strong>{{ currentProject.plannedEnd.substring(0, 10) }}</p>
            <hr>
            <p><strong>Planned Effort <i>(In Hours)</i>: </strong>{{ currentProject.plannedEffort }}</p>
            <hr>
            <p><strong>Performed Effort <i>(In Hours)</i>: </strong>{{ currentProject.performedEffort }}</p>
            <hr>
            <p><strong>Competences: </strong>{{ currentProject.competences }}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <!--deleteProject(project.projectNumber)-->

      <vs-prompt
          title="Delete Project"
          color="danger"
          @cancel="closeDeletePrompt"
          @accept="deleteProject"
          @close="closeDeletePrompt"
          :is-valid="true"
          :active.sync="activeDeletePrompt"
      >
        <div class="con-exemple-prompt">
          <h4>Are you sure to delete the project :</h4>  <h5>{{ currentProject.projectName }}</h5>
        </div>
      </vs-prompt>


      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Project</vs-button>
      <vs-prompt
          title="Add New Project"
          color="success"
          @cancel="closeAddForm"
          @accept="addProject"
          @close="closeAddForm"
          :is-valid="validProject"
          :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" class="mb-3" v-model="inputValues.projectName"/>
          <vs-input type="number" label-placeholder="ID of the client" class="mb-3"
                    v-model="inputValues.clientIDField"/>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="start" name="plannedStart"
                                                :value="dateToday"
                                                min="2018-01-01" max="2030-12-31"></div>
          <div class="mb-3">
            <small>Planned End</small> <input class="ml-2" type="date" id="end" name="plannedEnd"
                                              :value="dateToday"
                                              min="2018-01-01" max="2030-12-31"></div>
          <vs-input type="number" label-placeholder="Planned Effort In Hours" class="mb-3"
                    v-model="inputValues.plannedEffortField"/>
          <vs-input disabled="true" type="number" label-placeholder="Performed Effort In Hours : 0" class="mb-3"
          />
          <vs-input label-placeholder="competences" class="mb-3" v-model="inputValues.competencesField"/>
          <vs-alert
              :active="!validProject"
              color="danger"
              icon="new_releases"
          >
            All fields must be filled in
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Project"
          color="danger"
          @cancel="closeEditForm"
          @accept="updateProject"
          @close="closeEditForm"
          :is-valid="validProjectEdit"
          :active.sync="activeEditPromt"
      >
        <h5>Project Name : {{ currentProject.projectName }}</h5>
        <h6 class="mb-2 mt-2">ID of the Client : {{ currentProject.clientID }}</h6>
        <hr>
        <div class="con-exemple-prompt">
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="startedit" name="plannedStartEdit"
                                                :value="startDatum"
                                                min="2018-01-01" max="2030-12-31"></div>
          <div class="mb-3">
            <small>Planned End</small> <input class="ml-2" type="date" id="endedit" name="plannedEndEdit"
                                              :value="endDatum"
                                              min="startDatum" max="2030-12-31">
          </div>
          <vs-input type="number" label="Planned Effort" :placeholder="currentProject.plannedEffort" class="mb-3"
                    v-model="editValues.plannedEffortField"/>
          <vs-input type="number" label="Performed Effort" :placeholder="currentProject.performedEffort" class="mb-3"
                    v-model="editValues.performedEffortField"/>
          <vs-input label="Competences" :placeholder="currentProject.competences" class="mb-3"
                    v-model="editValues.competencesField"/>
          <vs-alert
              :active="!validProjectEdit"
              color="warning"
              icon="new_releases"
          >
            All fields must be filled in
          </vs-alert>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "projectList",
  data: () => {
    return {
      projects: [],
      dateToday: "",
      startDatum: "",
      startdate1: "",
      enddate1: "",
      endDatum: "",
      currentClient: {},
      currentProject: {},
      editProjectID: 0,
      projectSelected: false,
      activeEditPromt: false,
      activePrompt: false,
      activeDeletePrompt: false,
      inputValues: {
        projectName: "",
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        performedEffortField: "",
        competencesField: ""
      },
      editValues: {
        clientIDField: "",
        plannedStartField: "",
        plannedEndField: "",
        plannedEffortField: "",
        performedEffortField: "",
        competencesField: ""
      }
    };
  },

  created() {
    this.fetchAllProjects();
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    this.dateToday = yyyy + "-" + mm + '-' + dd;

  },

  computed: {


    validProject() {
      //this.getStartDate()
      //this.getEndDate()
      //   this.startdate1 = dateControl.value;
      //var timeControl1 = document.querySelector('input[id="end"]');
      //this.enddate1 = timeControl.value;
      return (parseFloat(this.inputValues.plannedEffortField) > 0
          && this.inputValues.competencesField.length > 0)

    },

    validProjectEdit() {
      return true
    }
  },

  methods: {
    /*
        getStartDate() {
          var dateControl1 = document.querySelector('input[id="start"]');
          this.startdate1 = dateControl1.value;
        },

        getEndDate(){
          var dateControl1 = document.querySelector('input[id="end"]');
          this.enddate1 = dateControl1.value;
        }, */
    showDeletePrompt: function (id) {
      this.fetchProject(id)
      this.activeDeletePrompt = true
    },

    plannedStart: function () {
      var startdate = this.currentProject.plannedStart;
      this.startDatum = startdate.substring(0, 10)
      // var dateControl = document.querySelector('input[id="startedit"]')

      // eslint-disable-next-line no-console
      console.log(startdate)
      //return startdate;

    },

    plannedEnd: function () {
      var enddate = this.currentProject.plannedEnd;
      this.endDatum = enddate.substring(0, 10)
    },

    closeDeletePrompt: function () {
      this.activeDeletePrompt = false;
      this.closeDeleteAlert()

    },

    updateProjectDetails(id) {
      this.fetchProject(id)
      this.projectSelected = true
    },
    addProject: async function () {
      var dateControl = document.querySelector('input[id="start"]');
      var startdate = dateControl.value;
      var timeControl = document.querySelector('input[id="end"]');
      var enddate = timeControl.value;
      this.inputValues.plannedStartField = startdate + " " + "00:00"
      this.inputValues.plannedEndField = enddate + " " + "00:00"
      await axios.post('http://localhost:8080/projects', {
        "projectName": this.inputValues.projectName,
        "clientID": parseInt(this.inputValues.clientIDField),
        "plannedStart": startdate + " " + "00:00",
        "plannedEnd": enddate + " " + "00:00",
        "plannedEffort": parseInt(this.inputValues.plannedEffortField),
        "performedEffort": 0,
        "competences": this.inputValues.competencesField,
      })

      await this.fetchCustomer(this.inputValues.clientIDField)

      await axios.put(`http://localhost:8080/clients/`, {
        'id': this.currentClient.clientID,
        'name': this.currentClient.name,
        'email': this.currentClient.email,
        'telephoneNumber': this.currentClient.telephoneNumber,
        'contactPersonID': this.currentClient.contactPersonID,
        'projectIDs': this.currentClient.projectIDs + "-" + 5 //TODO : HOW TO GET THE ID OF THE PROJECT TO BE ADDED
      })


      this.acceptAlert();
      await this.fetchAllProjects()
    },
    fetchCustomer: async function (id) {
      await axios.get(`http://localhost:8080/clients/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            console.log(response.data)
            this.currentClient = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    updateProject: async function () {
      var dateControlEdit = document.querySelector('input[id="startedit"]');
      var startdateedit = dateControlEdit.value;
      var timeControlEdit = document.querySelector('input[id="endedit"]');
      var enddateedit = timeControlEdit.value;
      this.editValues.plannedStartField = startdateedit + " " + "00:00";
      this.editValues.plannedEndField = enddateedit + " " + "00:00";
      await axios.put(`http://localhost:8080/projects`, {
        "projectNumber": this.editProjectID,
        "projectName": this.currentProject.projectName,
        "clientID": this.currentProject.clientID,
        "plannedStart": startdateedit + " " + "00:00",
        "plannedEnd": enddateedit + " " + "00:00",
        "plannedEffort": parseInt(this.editValues.plannedEffortField),
        "performedEffort": parseInt(this.editValues.performedEffortField),
        "competences": this.editValues.competencesField,
      })
      this.editAlert()
      await this.fetchAllProjects();
    },
    updateProjectID: async function (id) {
      await this.fetchProject(id);
      this.activeEditPromt = true
      this.editProjectID = id;
      this.editValues.clientIDField = this.currentProject.clientID
      this.editValues.competencesField = this.currentProject.competences
      this.editValues.performedEffortField = this.currentProject.performedEffort
      this.editValues.plannedEndField = this.currentProject.plannedEnd
      this.editValues.plannedStartField = this.currentProject.plannedStart
      this.editValues.plannedEffortField = this.currentProject.plannedEffort
      this.plannedStart()
      this.plannedEnd()
    },


    deleteProject: async function () {
      this.activeDeletePrompt = false;
      await axios.delete(`http://localhost:8080/projects/` + this.currentProject.projectNumber);
      this.deleteAlert()
      await this.fetchAllProjects();
    },

    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/projects/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },
    acceptAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Project has been successfully added.'
      })
    },

    deleteAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Project has been successfully deleted.'
      })
    },

    editAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        color: "success", type: "flat",
        text: 'Project has been successfully edited.'
      })
    },

    closeDeleteAlert() {
      this.$vs.notify({
        title: 'Cancelled:',
        color: "rgb(187, 138, 200)", type: "gradient",
        text: 'Project has not been deleted.'
      })
    },
    closeAddForm() {
      this.inputValues.clientIDField = "";
      this.inputValues.projectName = "";
      this.inputValues.plannedStartField = "";
      this.inputValues.plannedEndField = "";
      this.inputValues.plannedEffortField = "";
      this.inputValues.performedEffortField = "";
      this.inputValues.competencesField = "";
      this.$vs.notify({
        title: 'Closed',
        color: "warning",
        text: 'Add was cancelled successfully.'
      })
    },

    closeEditForm() {
      this.editValues.clientIDField = "";
      this.editValues.plannedStartField = "";
      this.editValues.plannedEndField = "";
      this.editValues.plannedEffortField = "";
      this.editValues.performedEffortField = "";
      this.editValues.competencesField = "";
      this.$vs.notify({
        title: 'Closed',
        text: 'Edit was cancelled successfully.'
      })
    },

    fetchProject: async function (id) {
      await axios.get(`http://localhost:8080/projects/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentProject = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      // this.projectSelected = true
    },
  }
}
</script>

<style scoped>
</style>