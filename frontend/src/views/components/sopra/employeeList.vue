<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="employeeSelected ? 4 : 12" vs-sm="6" vs-xs="12" code-toggler>
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
                  <a @click="updateDetailedEmployee(employee.employeeID)"> <div class="mr-2"><vs-avatar color="primary" :text="employee.employeeID"/></div></a>
                  <div class="">
                    <a @click="updateDetailedEmployee(employee.employeeID)" class="m-b-0" style="cursor:pointer"> {{ employee.name }}</a>
                  </div>
                </div>
              </td>
              <td>{{employee.employeeID}}</td>
              <td>
                <div>
                  <vs-button @click="deleteEmployee(employee.employeeID)" class="m-1" color="danger" type="filled">
                    Delete
                  </vs-button>
                  <vs-button @click="updateEditID(employee.employeeID)" class="m-1" color="primary" type="filled">
                    Edit
                  </vs-button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card v-show="employeeSelected" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off" @click="employeeSelected = false"></vs-button>
            <h1 >Details vom {{currentEmployee.name}} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentEmployee.name}}</p>
            <hr>
            <p><strong>Domicile: </strong>{{currentEmployee.domicile}}</p>
            <hr>
            <p><strong>Competences: </strong>{{currentEmployee.competences}}</p>
            <hr>
            <p><strong>Projects (ID): </strong>{{currentEmployee.projectIDs}}</p>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card class="cardx">
          <EmployeeChart :employeeID="this.currentEmployee.employeeID"/>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Customer</vs-button>
      <vs-prompt
          title="Add Employee"
          color="danger"
          @cancel="closeAdd"
          @accept="addEmployee"
          @close="closeAdd"
          :is-valid="validEmployee"
          :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Mitarbeiterdaten eingeben
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Domicile" class="mb-3" v-model="inputValues.domicileField"/>
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
      <vs-prompt
          title="Edit Employee"
          color="warning"
          @cancel="closeEdit"
          @accept="updateEmployee"
          @close="closeEdit"
          :is-valid="validEmployeeEdit"
          :active.sync="activeEditPromt"
      >
        <div class="con-exemple-prompt">
          Please Modify Employee Data
          <vs-input :placeholder="editValues.nameField" class="mb-3" v-model="editValues.nameField" />
          <vs-input :placeholder="editValues.domicileField" class="mb-3" v-model="editValues.domicileField"/>
          <vs-input :placeholder="editValues.competencesField" class="mb-3" v-model="editValues.competencesField"/>
          <vs-input :placeholder="editValues.projectsField" class="mb-3" v-model="editValues.projectsField"/>
          <vs-alert
              :active="!validEmployeeEdit"
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
import EmployeeChart from "@/views/components/dashboard/EmployeeChart";

export default {
  name: "employeeList",
  data: () => {
    return {
      employees: [],
      currentEmployee:{},
      employeeSelected:false,
      activePrompt:false,
      activeEditPromt:false,
      inputValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        projectsField: ''
      },
      editValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        projectsField: ''
      },
      components: {EmployeeChart},
    };
  },

  created() {
    this.fetchEmployees();


  },

  computed:{
    validEmployee(){
      return (this.inputValues.nameField.length > 4 && this.inputValues.nameField.length < 26
              && this.inputValues.domicileField.length > 4 && this.inputValues.domicileField.length < 26
      )
    },
    validEmployeeEdit(){
      return (this.inputValues.nameField.length > 4 && this.inputValues.nameField.length < 26
              && this.inputValues.domicileField.length > 4 && this.inputValues.domicileField.length < 26
      )
    }
  },

  methods: {
    async updateEmployee(){
      await axios.put(`http://localhost:8080/employees/`,{
        'id':this.currentEmployee.employeeID,
        'name': this.editValues.nameField,
        'domicile': this.editValues.domicileField,
        'competences': this.editValues.competencesField,
        'projectIDs': this.editValues.projectsField
      })
      await this.fetchEmployees()


    },



    acceptAlert(){
      this.$vs.notify({
        title:'Benachrichtigung:',
        text:'Mitarbeiter wurde erfolgreich angelegt.'
      })
    },
    closeAdd(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.projectsField = ''
      this.$vs.notify({
        title:'Beendet',
        text:'Hinzufügen wurde abgebrochen.'
      })
    },

    closeEdit(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.projectsField = ''
      this.$vs.notify({
        title:'Closed',
        text:'Edit was cancelled.'
      })
    },

    fetchEmployee: async function(id){
      await axios.get(`http://localhost:8080/employees/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            console.log(response.data)
            this.currentEmployee = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    fetchEmployees: async function (){
      await axios.get(`http://localhost:8080/employees/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.employees = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },


    addEmployee: async function() {
      await axios.post('http://localhost:8080/employees' , {
        'name': this.inputValues.nameField,
        'domicile': this.inputValues.domicileField,
        'competences': this.inputValues.competencesField,
        'projectIDs': this.inputValues.projectsField
      })
      this.acceptAlert()
      await this.fetchEmployees()
    },

    deleteEmployee: async function(id){
      await axios.delete(`http://localhost:8080/employees/` + id)
      await this.fetchEmployees()
    },

    async updateEditID(id){
      await this.fetchEmployee(id);
      this.editValues.nameField = this.currentEmployee.name
      this.editValues.domicileField = this.currentEmployee.domicile
      this.editValues.competencesField = this.currentEmployee.competences
      this.editValues.projectsField = this.currentEmployee.projectIDs
      this.activeEditPromt = true;

    },

    updateDetailedEmployee(id){
      this.fetchEmployee(id)
      this.employeeSelected = true
    }
  }
}
</script>

<style scoped>
</style>
