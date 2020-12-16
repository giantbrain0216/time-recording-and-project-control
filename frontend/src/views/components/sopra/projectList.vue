<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="projectSelected ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
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
                  <a @click="fetchProject(project.projectNumber)"> <div class="mr-2"><vs-avatar color="primary" :text="project.projectNumber"/></div></a>
                  <div class="">
                    <a @click="fetchProject(project.projectNumber)" class="m-b-0" style="cursor:pointer"> {{ project.projectNumber }}</a>
                  </div>
                </div>
              </td>
              <td>{{project.projectNumber}}</td>
              <td>
                <div>
                  <vs-button class="m-1" color="danger" type="filled">
                    Delete
                  </vs-button>
                  <vs-button class="m-1" color="primary" type="filled">
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
            <h4>Details vom {{currentProject.name}}</h4>
          </div>
          <div>
            <p><strong>Email: </strong>{{currentProject.projectNumber}}</p>
            <hr>
            <p><strong>Tel: </strong>{{currentProject.clientID}}</p>
            <hr>
            <p><strong>Contact Person (ID): </strong>{{currentProject.plannedStart}}</p>
            <hr>
            <p><strong>Projects (IDs): </strong>{{currentProject.plannedEnd}}</p>
            <p><strong>-</strong>{{currentProject.plannedEffort}}</p>
            <p><strong>-</strong>{{currentProject.performedEffort}}</p>
            <p><strong>-</strong>{{currentProject.competences}}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Project</vs-button>
      <vs-prompt
          title="Projekt Hinzufügen"
          color="danger"
          @cancel="close"
          @accept="close"
          @close="close"
          :is-valid="validProject"
          :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Projektdaten eingeben
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.clientIDField" />
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
      currentProject:{},
      projectSelected:false,
      activePrompt:false,
      inputValues: {
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
    axios.get(`http://localhost:8080/projects/`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.projects = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })


  },

  computed:{
    validProject(){
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
    acceptAlert(){
      this.$vs.notify({
        title:'Benachrichtigung:',
        text:'Projekt wurde erfolgreich angelegt.'
      })
    },
    close(){
      this.inputValues.idField = '',
          this.inputValues.plannedStartField = '',
          this.inputValues.plannedEndField = '',
          this.inputValues.plannedEffortField = '',
          this.inputValues.performedEffortField = '',
          this.inputValues.competencesField = '',
          this.$vs.notify({
            title:'Beendet',
            text:'Hinzufügen wurde abgebrochen.'
          })
    },

    fetchProject: function(id){
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