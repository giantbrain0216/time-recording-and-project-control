<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="employeeSelected ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
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
          <tr v-for="employee in employees" :key="employee.employeeID">
            <td>
              <div class="d-flex align-items-center">
                <a @click="fetchEmployee(employee.employeeID)"> <div class="mr-2"><vs-avatar color="primary" :text="employee.name"/></div></a>
                <div class="">
                  <a @click="fetchEmployee(employee.employeeID)" class="m-b-0" style="cursor:pointer"> {{ employee.name }}</a>
                </div>
              </div>
            </td>
            <td>{{employee.employeeID}}</td>
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
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="employeeSelected" class="cardx">
          <div slot="header">
            <h4>Details vom {{currentEmployee.name}}</h4>
          </div>
          <div>
            <p><strong>ID: </strong>{{currentEmployee.employeeID}}</p>
            <hr>
            <p><strong>Email: </strong>{{currentEmployee.name}}</p>
            <hr>
            <p><strong>Domicile: </strong>{{currentEmployee.domicile}}</p>
            <hr>
            <p><strong>Contact Person (ID): </strong>{{currentEmployee.competences}}</p>
            <hr>
            <p><strong>Projects (IDs): </strong>{{currentEmployee.projectIDs}}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Employee</vs-button>
      <vs-prompt
        title="Add Employee"
        color="danger"
        @cancel="close"
        @accept="close"
        @close="close"
        :is-valid="validEmployee"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Kundendaten eingeben
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Email" class="mb-3" v-model="inputValues.emailField" />
          <vs-input placeholder="Demicile" class="mb-3" v-model="inputValues.domicileField"/>
          <vs-input placeholder="Competences" class="mb-3" v-model="inputValues.competencesField"/>
          <vs-input placeholder="Projects (IDs)" class="mb-3" v-model="inputValues.projectsField"/>
          <vs-alert
            :active="!validEmployee"
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
  name: "employeeList",
  data: () => {
    return {
      employees: [],
      currentEmployee:{},
      employeeSelected:false,
      activePrompt:false,
      inputValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        projectsField: ''
      }
    };
  },

  created() {
    axios.get(`http://localhost:8080/employees`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.employees = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })


  },

  computed:{
      validEmployee(){
        return (41 > this.inputValues.nameField.length > 0
                && 41 > this.inputValues.domicileField.length > 0
                )
      }
  },

  methods: {
    acceptAlert(){
      this.$vs.notify({
        title:'Benachrichtigung:',
        text:'Mitarbeiter wurde erfolgreich angelegt.'
      })
    },
      close(){
        this.inputValues.idField = '',
        this.inputValues.nameField = '',
        this.inputValues.locationField = '',
        this.$vs.notify({
          title:'Beendet',
          text:'Hinzufügen wurde abgebrochen.'
        })
      },

    fetchEmployee: function(id){
      axios.get(`http://localhost:8080/employees/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentEmployee = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      this.employeeSelected = true
    },
  }
}
</script>

<style scoped>
</style>