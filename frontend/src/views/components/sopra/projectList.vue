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
            <p><strong>Planned Start: </strong>{{ currentProject.plannedStart }}</p>
            <hr>
            <p><strong>Planned End: </strong>{{ currentProject.plannedEnd }}</p>
            <hr>
            <p><strong>Planned Effort: </strong>{{ currentProject.plannedEffort }}</p>
            <hr>
            <p><strong>Performed Effort: </strong>{{ currentProject.performedEffort }}</p>
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
          <h4>Are you sure to delete the project :</h4>  <h5>{{currentProject.projectName}}</h5>
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
          <vs-input label-placeholder="ID of the client" class="mb-3" v-model="inputValues.clientIDField"/>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="start" name="plannedStart"
                                                :value="dateToday"
                                                min="2018-01-01" max="2030-12-31"></div>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="end" name="plannedEnd"
                                                :value="dateToday"
                                                min="2018-01-01" max="2030-12-31"></div>
          <vs-input label-placeholder="planned effort" class="mb-3" v-model="inputValues.plannedEffortField"/>
          <vs-input label-placeholder="performed effort" class="mb-3" v-model="inputValues.performedEffortField"/>
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

        <div class="con-exemple-prompt">
          <vs-input :text="currentProject.clientID" class="mb-3 mt-2" v-model="editValues.clientIDField"/>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="startedit" name="plannedStartEdit"
                                                :value="dateToday"
                                                min="2018-01-01" max="2030-12-31"></div>
          <div class="mb-3">
            <small>Planned Start</small> <input class="ml-2" type="date" id="endedit" name="plannedEndEdit"
                                                :value="dateToday"
                                                min="2018-01-01" max="2030-12-31">
          </div>
          <vs-input :text="editValues.plannedEffort" class="mb-3"
                    v-model="editValues.plannedEffortField"/>
          <vs-input :text="currentProject.performedEffort" class="mb-3"
                    v-model="editValues.performedEffortField"/>
          <vs-input :text="currentProject.competences" class="mb-3" v-model="editValues.competencesField"/>
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
      currentProject: {},
      editProjectID: 0,
      projectSelected: false,
      activeEditPromt: false,
      activePrompt: false,
      activeDeletePrompt:false,
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
      return (this.inputValues.clientIDField.length > 0
          && this.inputValues.plannedEffortField.length > 0
          && this.inputValues.performedEffortField.length > 0
          && this.inputValues.competencesField.length > 0)

    },

    validProjectEdit() {
      return ( this.editValues.plannedEffortField.length > 0 && this.editValues.clientIDField.length > 0
          && this.editValues.performedEffortField.length > 0
          && this.editValues.competencesField.length > 0     )
    }
  },

  methods: {

    showDeletePrompt: function (id){
      this.fetchProject(id)
      this.activeDeletePrompt = true
    },

    closeDeletePrompt: function (){
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
        "performedEffort": parseInt(this.inputValues.performedEffortField),
        "competences": this.inputValues.competencesField,
      })
      this.acceptAlert();
      await this.fetchAllProjects()
    },

    updateProject: async function () {
      var dateControl = document.querySelector('input[id="startedit"]');
      var startdateedit = dateControl.value;
      var timeControl = document.querySelector('input[id="endedit"]');
      var enddateedit = timeControl.value;
      this.editValues.plannedStartField = startdateedit + " " + "00:00"
      this.editValues.plannedEndField = enddateedit + " " + "00:00"
      await axios.put(`http://localhost:8080/projects`), {
        "id":this.editProjectID,
        "projectName": this.currentProject.projectName,
        "clientID": parseInt(this.editValues.clientIDField),
        "plannedStart": this.editValues.plannedStartField,
        "plannedEnd": this.editValues.plannedEndField,
        "plannedEffort": parseInt(this.editValues.plannedEffortField),
        "performedEffort": parseInt(this.editValues.performedEffortField),
        "competences": this.editValues.competencesField,
      }
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

    closeDeleteAlert(){
      this.$vs.notify({
        title: 'Cancelled:',
        color:"rgb(187, 138, 200)", type:"gradient",
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
        color:"warning",
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

    fetchProject: function (id) {
      axios.get(`http://localhost:8080/projects/${id}`)
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