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
              <th class="border-top-0">ID</th>
              <th class="border-top-0">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="project in projects" :key="project.projectNumber">
              <td>
                <div class="d-flex align-items-center">
                  <a @click="fetchProject(project.projectNumber)">
                    <div class="mr-2">
                      <vs-avatar color="primary" :text="project.projectNumber"/>
                    </div>
                  </a>
                  <div class="">
                    <a @click="fetchProject(project.projectNumber)" class="m-b-0" style="cursor:pointer">
                      {{ project.projectName }}</a>
                  </div>
                </div>
              </td>
              <td>{{ project.projectNumber }}</td>
             <!-- <td>{{ project.projectName }}</td> -->
              <td>
                <div>
                  <vs-button @click="deleteProject(project.projectNumber) " class="m-1" color="danger" type="filled">
                  Delete
                  </vs-button>
                  <vs-button @click="updateProjectID(project.projectNumber)" class=" m-1
                  " color="primary" type="filled">
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
          </div>
          <div>
            <p><strong>Email: </strong>{{ currentProject.projectNumber }}</p>
            <hr>
            <p><strong>Tel: </strong>{{ currentProject.clientID }}</p>
            <hr>
            <p><strong>Contact Person (ID): </strong>{{ currentProject.plannedStart }}</p>
            <hr>
            <p><strong>Projects (IDs): </strong>{{ currentProject.plannedEnd }}</p>
            <p><strong>Planned Effort</strong>{{ currentProject.plannedEffort }}</p>
            <p><strong>Performed Effort</strong>{{ currentProject.performedEffort }}</p>
            <p><strong>Competences</strong>{{ currentProject.competences }}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
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
          Insert Project Data
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.projectName"/>
          <vs-input placeholder="planned start" class="mb-3" v-model="inputValues.plannedStartField"/>
          <vs-input placeholder="planned end" class="mb-3" v-model="inputValues.plannedEndField"/>
          <vs-input placeholder="planned effort" class="mb-3" v-model="inputValues.plannedEffortField"/>
          <vs-input placeholder="performed effort" class="mb-3" v-model="inputValues.performedEffortField"/>
          <vs-input placeholder="competences" class="mb-3" v-model="inputValues.competencesField"/>
          <vs-alert
              :active="!validProject"
              color="danger"
              icon="new_releases"
          >
            Die Felder müssen gefüllt werden.
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Project"
          color="danger"
          @cancel="closeEditForm"
          @accept="updateProject"
          @close="closeEditForm"
          :is-valid="validProject"
          :active.sync="activeEditPromt"
      >
        <div class="con-exemple-prompt">
          Edit Project Data
          <vs-input :placeholder="inputValues.plannedEffortField" class="mb-3" v-model="editValues.plannedEffortField"/>
          <vs-input :placeholder="inputValues.plannedEndField" class="mb-3" v-model="editValues.plannedEndField"/>
          <vs-input :placeholder="inputValues.plannedEffortField" class="mb-3" v-model="editValues.plannedEffortField"/>
          <vs-input :placeholder="inputValues.performedEffortField" class="mb-3"
                    v-model="editValues.performedEffortField"/>
          <vs-input :placeholder="inputValues.competencesField" class="mb-3" v-model="editValues.competencesField"/>
          <vs-alert
              :active="!validProject"
              color="warning"
              icon="new_releases"
          >
            Die Felder müssen gefüllt werden.
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
      currentProject: {},
      editProjectID: 0,
      projectSelected: false,
      activeEditPromt: false,
      activePrompt: false,
      inputValues: {
        projectName: "",
        clientIDField: '',
        plannedStartField: '',
        plannedEndField: '',
        plannedEffortField: '',
        performedEffortField: '',
        competencesField: ''
      },
      editValues: {
        clientIDField: '',
        plannedStartField: '',
        plannedEndField: '',
        plannedEffortField: '',
        performedEffortField: '',
        competencesField: ''
      }
    };
  },

  created() {
    this.fetchAllProjects();


  },

  computed: {
    validProject() {
      return (this.inputValues.clientIDField.length !== null
          && this.inputValues.plannedStartField.length > 0
          && this.inputValues.plannedEndField.length > 0
          && this.inputValues.plannedEffortField.length > 0
          && this.inputValues.performedEffortField.length > 0
          && this.inputValues.competencesField > 0
      )
    }
  },

  methods: {

    addProject: async function () {
      await axios.post('http://localhost:8080/projects', {
        "projectName": this.inputValues.projectName,
        "clientID": this.inputValues.clientIDField,
        "plannedStart": this.inputValues.plannedStartField,
        "plannedEnd": this.inputValues.plannedEndField,
        "plannedEffort": this.inputValues.plannedEffortField,
        "performedEffort": this.inputValues.performedEffortField,
        "competences": this.inputValues.competencesField,
      })
      this.acceptAlert();
      await this.fetchAllProjects()
    },

    updateProject: async function () {
      await axios.put(`http://localhost:8080/projects/` + this.editProjectID), {
        //"projectName": this.inputValues.projectName,
        //"clientID": this.inputValues.clientIDField,
        "plannedStart": this.editValues.plannedStartField,
        "plannedEnd": this.editValues.plannedEndField,
        "plannedEffort": this.editValues.plannedEffortField,
        "performedEffort": this.editValues.performedEffortField,
        "competences": this.editValues.competencesField,
      }
      await this.fetchAllProjects();
    },
    updateProjectID: async function (id) {
      await this.fetchProject(id);
      this.editProjectID = id,
          this.editValues.clientIDField = this.currentProject.clientIDField
      this.editValues.competencesField = this.currentProject.competencesField
      this.editValues.performedEffortField = this.currentProject.performedEffortField
      this.editValues.plannedEndField = this.currentProject.plannedEndField
      this.editValues.plannedStartField = this.currentProject.plannedStartField
      this.editValues.plannedEffortField = this.currentProject.plannedEffortField,
          this.activeEditPromt = true;

    },


    deleteProject: async function (id) {
      await axios.delete(`http://localhost:8080/projects/` + id)
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
    closeAddForm() {
      this.inputValues.idField = '',
          this.inputValues.plannedStartField = '',
          this.inputValues.plannedEndField = '',
          this.inputValues.plannedEffortField = '',
          this.inputValues.performedEffortField = '',
          this.inputValues.competencesField = '',
          this.$vs.notify({
            title: 'Closed',
            text: 'Add was cancelled successfully.'
          })
    },

    closeEditForm(){

      this.editValues.idField = '',
          this.editValues.plannedStartField = '',
          this.editValues.plannedEndField = '',
          this.editValues.plannedEffortField = '',
          this.editValues.performedEffortField = '',
          this.editValues.competencesField = '',
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
      this.projectSelected = true
    },
  }
}
</script>

<style scoped>
</style>