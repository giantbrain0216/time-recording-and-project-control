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
                  <vs-button @click="deletionPrompt(employee.employeeID)" class="m-1" color="danger" type="filled">
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
            <h1 >Details of {{currentEmployee.name}} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentEmployee.name}}</p>
            <hr>
            <p><strong>Domicile: </strong>{{currentEmployee.domicile}}</p>
            <hr>
            <p><strong>Competences: </strong>{{currentEmployee.competences}}</p>
            <hr>
            <p><strong>Projects (ID): </strong>{{currentEmployee.projectIDs}}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card class="cardx">
          <EmployeeChart :employeeID="this.currentEmployee.employeeID"/>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Employee</vs-button>
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
          Please insert employee data
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Domicile" class="mb-3" v-model="inputValues.domicileField"/>
          <vs-input placeholder="Competences" class="mb-3" v-model="inputValues.competencesField"/>
          <vs-input placeholder="Projects (IDs)" class="mb-3" v-model="inputValues.projectsField"/>
          <vs-alert
              :active="!validEmployee"
              color="danger"
              icon="new_releases"
          >
           Fields can't be empty.
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
          <strong>Please Modify Employee Data of</strong>
          <h6 class="edit-employeee">{{editValues.nameField}}</h6>
          <br>
          <label>Name: </label>
          <vs-input :placeholder="editValues.nameField" class="mb-3" v-model="editValues.nameField" />
          <label>Domicile: </label>
          <vs-input :placeholder="editValues.domicileField" class="mb-3" v-model="editValues.domicileField"/>
          <label>Competences: </label>
          <vs-input :placeholder="editValues.competencesField" class="mb-3" v-model="editValues.competencesField"/>
          <label>Projects (IDs): </label>
          <vs-input :placeholder="editValues.projectsField" class="mb-3" v-model="editValues.projectsField"/>
          <vs-alert
              :active="!validEmployeeEdit"
              color="warning"
              icon="new_releases"
          >
            Fields can't be empty.
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close="closeDeletio"
          @cancel="closeDeletio"
          @accept="deleteEmployee"
          :is-valid="true"
          :active.sync="activeDeletionPrompt"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete <br>
          <h6>{{currentEmployee.name}}</h6>
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
  components: {EmployeeChart},
  data: () => {
    return {
      employees: [],
      currentEmployee:{},
      employeeSelected:false,
      activePrompt:false,
      activeEditPromt:false,
      activeDeletionPrompt: false,
      inputValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        projectsField: '',
      },
      editValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        projectsField: ''
      },

    };
  },

  created() {
    this.fetchEmployees();
  },

  computed:{
    validEmployee(){
      return (this.inputValues.nameField.length > 0 && this.inputValues.nameField.length < 26
              && this.inputValues.domicileField.length > 4 && this.inputValues.domicileField.length < 26
      )
    },
    validEmployeeEdit(){
      return (this.editValues.nameField.length > 0 && this.editValues.nameField.length < 26
              && this.editValues.domicileField.length > 4 && this.editValues.domicileField.length < 26
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
      this.acceptEditAlert()
    },

    deletionPrompt: function (id) {
      this.fetchEmployee(id)
      this.activeDeletionPrompt = true
    },

    acceptEditAlert() {
      this.$vs.notify({
        title:'Successfully:',
        text:'modified Employee.',
        color:'green'
      })
    },

    acceptAlert(){
      this.$vs.notify({
        title:'Successfully:',
        text:'added Employee.',
        color:'green'
      })
    },

    acceptDeletionAlert() {
      this.$vs.notify({
        title:'Deletion',
        text:'was successful.',
        color:'green',
      })
    },

    closeAdd(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.projectsField = ''
      this.$vs.notify({
        title:'Closed',
        text:'Adding was cancelled.',
        color:'red'
      })
    },

    closeDeletio() {
      this.$vs.notify({
        title:'Closed',
        text:'Deletion was cancelled.',
        color:'red'
      })
    },

    closeEdit(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.projectsField = ''
      this.$vs.notify({
        title:'Closed',
        text:'Edit was cancelled.',
        color:'red'
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

    deleteEmployee: async function(){
      this.activeDeletePrompt = false;
      await axios.delete(`http://localhost:8080/employees/${this.currentEmployee.employeeID}`)
      await this.fetchEmployees()
      this.acceptDeletionAlert()
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
.edit-employeee{
  color: red;
}
</style>
