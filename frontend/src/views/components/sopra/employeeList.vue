<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="employeeSelected ? 8 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Name</th>
            <th class="border-top-0">ID</th>
            <th class="border-top-0">Location</th>
            </tr>
          </thead>
          <tbody>
          <tr v-for="employee in employees" :key="employee.id">
            <td>
              <div class="d-flex align-items-center">
                <a @click="fetchEmployee(employee.id)"> <div class="mr-2"><vs-avatar color="primary" :text="employee.name"/></div></a>
                <div class="">
                  <a @click="fetchEmployee(employee.id)" class="m-b-0" style="cursor:pointer"> {{ employee.name }}</a>
                </div>
              </div>
            </td>
            <td>{{employee.id}}</td>
            <td>{{employee.location }}</td>
            <vs-button color="danger" type="filled" >
              Delete
            </vs-button>
          </tr>
          </tbody>
        </table>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card v-show="employeeSelected" class="cardx">
          <div slot="header">
            <h4>Details</h4>
          </div>
          <div>
            <h3>{{"Name:" + currentEmployee.name}}</h3>
            <h3>ID: </h3>
            <h3>Location: </h3>
          </div>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Employee</vs-button>
      <vs-prompt
        title="Mitarbeiter Hinzufügen"
        color="danger"
        @cancel="close"
        @accept="acceptAlert"
        @close="close"
        :is-valid="validName"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Mitarbeiter Daten eingeben
          <vs-input placeholder="ID" class="mb-3" v-model="inputValues.idField" />
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Location" class="mb-3" v-model="inputValues.locationField"/>
          <vs-alert
            :active="!validName"
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
      employees: [
        {id: 1, name: 'Clirim Salihi', location: 'Tangegartstr. 11'},
        {id: 2, name: 'Radu Manea', location: 'Kein plan wo' },
        {id: 3, name: 'Mohamed Ben Salha', location: 'i-wo :D'}
      ],
      currentEmployee:{},
      EmployeeSelected:false,
      activePrompt:false,
      inputValues: {
        idField:'',
        nameField:'',
        locationField:''
      }
    };
  },

  created() {
    axios.get(`http://localhost:8080/customers`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.employees = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })


  },

  computed:{
      validName(){
        return (this.inputValues.idField.length > 0 && this.inputValues.nameField.length > 0 && this.inputValues.locationField.length > 0)
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
      axios.get(`http://localhost:8080/employees`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentCustomer = response.data[id-1]
          })
          .catch(e => {
            this.errors.push(e)
          })
      this.employeeSelected = true
      //print("Working")
    }

  }
}
</script>
<style scoped>
</style>