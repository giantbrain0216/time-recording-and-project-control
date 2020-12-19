<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="employeeSelected ? 4 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0">Name</th>
              <!--<th class="border-top-0">ID</th>-->
              <th class="border-top-0">Competences</th>
              <th class="border-top-0">Remaining Working Hours Per Week</th>
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
              <!--<td>{{employee.employeeID}}</td>-->
              <td>{{employee.competences}}</td>
              <td>{{employee.remainingWorkingHoursPerWeek}}</td>
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
            <h1 >Details of {{currentEmployee.name}} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentEmployee.name}}</p>
            <hr>
            <p><strong>Domicile: </strong>{{currentEmployee.domicile}}</p>
            <hr>
            <p><strong>Competences: </strong>{{currentEmployee.competences}}</p>
            <hr>
            <p><strong>Working Hours Per Week: </strong>{{currentEmployee.workingHoursPerWeek}}</p>
            <hr>
            <p><strong>Remaining Working Hours : </strong>{{currentEmployee.remainingWorkingHoursPerWeek}}</p>

          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card class="cardx">
          <EmployeeChart :employeeID="this.currentEmployee.employeeID"/>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add New Employee</vs-button>
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
          <vs-input label-placeholder="Name" class="mb-4" v-model="inputValues.nameField" />
          <vs-input label-placeholder="Domicile" class="mb-4" v-model="inputValues.domicileField"/>
          <vs-input label-placeholder="Competences" class="mb-4" v-model="inputValues.competencesField"/>
          <vs-input type="number"  label-placeholder="Working Hours Per Week" class="mb-4" v-model="inputValues.workingHoursField"/>
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
          <h5>Please Modify Employee Data of <strong class="edit-employeee">{{editValues.nameField}}</strong></h5>
          <h5>ID of the Employee: <strong class="edit-employeee">{{currentEmployee.employeeID}}</strong></h5>
<hr>
          <br>
          <vs-input label="Domicile" :placeholder="currentEmployee.domicile" class="mb-4" v-model="editValues.domicileField"/>
          <vs-input label="Competences" :placeholder="currentEmployee.competences" class="mb-4" v-model="editValues.competencesField"/>
          <vs-input label="Working Hours Peer Week" type="number" :placeholder="currentEmployee.workingHoursPerWeek" class="mb-4" v-model="editValues.workingHoursField"/>
          <vs-alert
              :active="!validEmployeeEdit"
              color="warning"
              icon="new_releases"
          >
            Fields can't be empty.
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
  components: {EmployeeChart},
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
        workingHoursField: '',
      },
      editValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        workingHoursField: ''
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
        'employeeID':this.currentEmployee.employeeID,
        'name': this.editValues.nameField,
        'domicile': this.editValues.domicileField,
        'competences': this.editValues.competencesField,//.toUpperCase(),
        'workingHoursPerWeek': this.editValues.workingHoursField,
        'remainingWorkingHoursPerWeek': this.currentEmployee.remainingWorkingHoursPerWeek + this.editValues.workingHoursField,
      })
      await this.fetchEmployees()


    },



    acceptAlert(){
      this.$vs.notify({
        title:'Successfully:',
        text:'added Employee.'
      })
    },
    closeAdd(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.workingHoursField = ''
      this.$vs.notify({
        title:'Closed',
        text:'Adding was cancelled.'
      })
    },

    closeEdit(){
      this.inputValues.nameField = ''
      this.inputValues.domicileField = ''
      this.inputValues.competencesField = ''
      this.inputValues.workingHoursField = ''
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
        'competences': this.inputValues.competencesField,//.toUpperCase(),
        'workingHoursPerWeek': this.inputValues.workingHoursField,
        'remainingWorkingHoursPerWeek':this.inputValues.workingHoursField,
      })
      this.acceptAlert()
      await this.fetchEmployees()
    },

    deleteEmployee: async function(id){
      await this.fetchEmployee(id);
      if (confirm(`Are you sure you want to delete ${this.currentEmployee.name}`)) {
        await axios.delete(`http://localhost:8080/employees/` + id)
        await this.fetchEmployees()
      }
    },

    async updateEditID(id){
      await this.fetchEmployee(id);
      this.editValues.nameField = this.currentEmployee.name
      this.editValues.domicileField = this.currentEmployee.domicile
      this.editValues.competencesField = this.currentEmployee.competences
      this.editValues.workingHoursField = this.currentEmployee.workingHoursPerWeek
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
