<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="clientSelected ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Client List</h2>
            <div class="float-right mb-1"><vs-button @click="activePrompt = true" color="primary" icon="add" type="filled">Add New Client</vs-button></div>
          </div>
          <div class="table-responsive">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0" style="color: cornflowerblue">ID</th>
            <th class="border-top-0" style="color: cornflowerblue">Name</th>
            <th class="border-top-0" style="color: cornflowerblue">EMAIL</th>
            <th class="border-top-0" style="color: cornflowerblue">Project IDs</th>
            <th class="border-top-0" style="color: cornflowerblue">Actions</th>
            </tr>
          </thead>
          <tbody>
          <tr v-for="client in clients" :key="client.clientID">
            <td>{{client.clientID}}</td>
            <td>
              <div class="d-flex align-items-center">
                <a @click="updateDetailedClient(client.clientID)">
                  <div class="mr-2"><vs-avatar color="primary" :text="client.name"/></div></a>
                <div class="">
                  <a @click="updateDetailedClient(client.clientID)" class="m-b-0" style="cursor:pointer"> {{ client.name }}</a>
                </div>
              </div>
            </td>
            <td>{{client.email}}</td>
            <td>{{client.projectIDs}}</td>
            <td>
              <div>
                <vs-button @click="updateDeleteClient(client)" icon="delete" class="m-1" color="danger" type="filled">
                </vs-button>
                <vs-button @click="updateEditID(client.clientID)" icon="edit" class="m-1" color="warning" type="filled">
                </vs-button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="clientSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="clientSelected" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off" @click="clientSelected = false"></vs-button>
            <h1 >Details of {{currentClient.name}} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentClient.name}}</p>
            <hr>
            <p><strong>Client ID: </strong>{{currentClient.clientID}}</p>
            <hr>
            <p><strong>Tel: </strong>{{currentClient.telephoneNumber}}</p>
            <hr>
            <p><strong>Contact Person (ID): </strong>{{currentClient.contactPersonID}}</p>
            <hr>
            <p><strong>Projects (IDs): </strong>{{currentClient.projectIDs}}</p>
            <hr>
          </div>
        </vs-card>
      </vs-col>

      <vs-prompt
        title="Add Client"
        color="primary"
        @cancel="closeAdd"
        @accept="addClient"
        @close="closeAdd"
        :is-valid="validClient"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Please insert client data.
          <vs-input label-placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input label-placeholder="Email" class="mb-3" v-model="inputValues.emailField"/>
          <vs-input type="number" label-placeholder="Tel" class="mb-3" v-model="inputValues.numberField"/>
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3">
            <div>Contact Person:      </div>
            <vs-dropdown class="ml-1">
              <a class="a-icon" href="#">
                {{this.selectedEmployeeName}}
                <vs-icon class="" icon="expand_more"></vs-icon>
              </a>
              <vs-dropdown-menu>
                <vs-dropdown-item @click="updateSelectedEmployee(employee.employeeID,employee.name)" v-for="employee in employees" :key="employee.employeeID">
                  {{employee.name }}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>


          </div>
          <vs-alert
            :active="!validClient"
            color="danger"
            icon="new_releases"
          >
          Fields can't be empty
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Edit Clients"
          color="warning"
          @cancel="closeEdit"
          @accept="updateClient"
          @close="closeEdit"
          :is-valid="validClientEdit"
          :active.sync="activeEditPromt"
      >
        <div class="con-exemple-prompt">
          Please Modify Client Data

          <div>  </div>
          <div>Name</div>
          <vs-input :placeholder="editValues.nameField" class="mb-3" v-model="editValues.nameField" />
          Email
          <vs-input :placeholder="editValues.emailField" class="mb-3" v-model="editValues.emailField"/>
          Telephone Number
          <vs-input :placeholder="editValues.numberField" class="mb-3" v-model="editValues.numberField"/>
          <div class="d-flex align-items-center dropdownbtn-alignment mb-3">
            <div>Contact Person:      </div>
            <vs-dropdown class="ml-1">
              <a class="a-icon" href="#">
                {{this.selectedEmployeeName}}
                <vs-icon class="" icon="expand_more"></vs-icon>
              </a>
              <vs-dropdown-menu>
                <vs-dropdown-item @click="updateSelectedEmployee(employee.employeeID,employee.name)" v-for="employee in employees" :key="employee.employeeID">
                  {{employee.name }}
                </vs-dropdown-item>
              </vs-dropdown-menu>
            </vs-dropdown>


          </div>


          <div class="mb-3"><h5>{{"Projects ID's: " + editValues.projectsField}}</h5></div>
          <vs-alert
              :active="!validClientEdit"
              color="warning"
              icon="new_releases"
          >
            Fields can't be empty
          </vs-alert>
        </div>
      </vs-prompt>

    </vs-row>
    <vs-prompt
        title="Delete Project"
        color="danger"
        @cancel='closeDeletePrompt("Client has not been deleted")'
        @accept="deleteClient(currentClient.clientID)"
        @close='closeDeletePrompt("Client has not been deleted")'
        :is-valid="true"
        :active.sync="activeDeletePrompt"
    >
      <div class="con-exemple-prompt">
        <h4>Are you sure to delete the client :</h4>  <h5>{{ currentClient.name }}</h5>
      </div>
    </vs-prompt>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "clientList",
  data: () => {
    return {
      employees:[],
      selectedEmployee:0,
      selectedEmployeeName:"Contact Person",
      clients: [],
      currentClient:{},
      clientSelected:false,
      activePrompt:false,
      activeEditPromt:false,
      inputValues: {
        nameField: '',
        emailField: '',
        numberField: '',
      },
      editValues: {
        nameField: '',
        emailField: '',
        numberField: '',
        projectsField:"",

      },
      activeDeletePrompt:false,
    };
  },

  async created() {
    await this.fetchCustomers();
    await this.fetchEmployees()


  },

  computed:{
      validClient(){
        var re = /\S+@\S+\.\S+/;
        return (this.inputValues.nameField.length > 0
                && 50 > this.inputValues.emailField.length && this.inputValues.emailField.length > 4
                && 41 > this.inputValues.numberField.length && this.inputValues.numberField.length > 7
                && re.test(this.inputValues.emailField)
                && this.selectedEmployee !== 0 && this.inputValues.numberField > 0
                )
      },
    validClientEdit(){
      var re = /\S+@\S+\.\S+/;
      return (this.editValues.nameField.length > 0
          && 50 > this.editValues.emailField.length && this.editValues.emailField.length> 4
          && 41 > this.editValues.numberField.length && this.editValues.numberField.length> 7
          && re.test(this.editValues.emailField)
          && this.selectedEmployee != 0
      )
    }
  },

  methods: {

    /**
     * Sets the editValues to the data of the current client
     *
     * @return client list with updated current client
     */
    async updateClient(){
      await axios.put(`http://localhost:8080/clients/`,{
        'id':this.currentClient.clientID,
        'name': this.editValues.nameField,
        'email': this.editValues.emailField,
        'telephoneNumber': this.editValues.numberField,
        'contactPersonID': this.selectedEmployee,
        'projectIDs': this.editValues.projectsField
      })
      await this.fetchCustomers()


    },

    /**
     * Notifies that addition succeeded
     */
    acceptAlert(){
      this.$vs.notify({
        title:'Notification:',
        text:'Employee was added.'
      })
    },

    /**
     * Notifies that addition was closed and sets the inputValues to ''
     */
    closeAdd(){
      this.inputValues.nameField = '';
      this.inputValues.emailField = '';
      this.inputValues.numberField = '';
      this.selectedEmployeeName = "Contact Person"
      this.selectedEmployee = 0
      this.$vs.notify({
        title:'Closed',
        text:'Adding was cancelled.'
      })

    },

    /**
     * Notifies that editing was closed and sets the editValues to ''
     */
    closeEdit(){
      this.editValues.nameField = '';
          this.editValues.emailField = '';
          this.editValues.numberField = '';
          this.editValues.projectsField = '';
          this.selectedEmployeeName = "Contact Person"
          this.selectedEmployee = 0
          this.$vs.notify({
            title:'Closed',
            text:'Edit was cancelled.'
          })
    },

    /**
     * Gets client from DB
     *
     * @param id of client
     * @return curreentClient with client data of DB
     */
    fetchCustomer: async function(id){
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

    /**
     * Gets all employees from DB
     *
     * @return employees
     */
    fetchEmployees: async function(){
      await axios.get(`http://localhost:8080/employees/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.employees = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })

    },

    /**
     * Gets all clients from DB
     *
     * @return clients
     */
    fetchCustomers: async function (){
      await axios.get(`http://localhost:8080/clients/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.clients = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
    },

    /**
     * Adds an client to DB
     *
     * @return updated clients
     */
    addClient: async function() {
      await axios.post('http://localhost:8080/clients' , {
        'name': this.inputValues.nameField,
        'email': this.inputValues.emailField,
        'telephoneNumber': this.inputValues.numberField,
        'contactPersonID': this.selectedEmployee,
        'projectIDs': ""
      })
      this.acceptAlert()
      await this.fetchCustomers()
      this.selectedEmployeeName = "Contact Person"
      this.selectedEmployee = 0
      this.inputValues = {
        nameField: '',
        emailField: '',
        numberField: '',
      }

    },

    /**
     * Deletes selected client
     *
     * @param id of client
     * @return updated clients
     */
    deleteClient: async function(id){
      await axios.delete(`http://localhost:8080/clients/` + id).then(() => {
        this.deleteAlert()
        this.activeDeletePrompt = false
        this.currentClient = {}

      }).catch((error) => {
        if (error.response){
          this.closeDeletePrompt( error.message);
          this.currentClient = {}
        }
      })
      await this.fetchCustomers()
    },

    /**
     * Sets the editValues to values of current client and activates editing prompt
     *
     * @param id of client
     */
    async updateEditID(id){
      await this.fetchCustomer(id);
      this.editValues.nameField = this.currentClient.name
      this.editValues.emailField = this.currentClient.email
      this.editValues.numberField = this.currentClient.telephoneNumber
      this.selectedEmployee = this.currentClient.contactPersonID
      for(var i =0;i<this.employees.length;i++){
        if(this.employees[i].employeeID == this.currentClient.contactPersonID){
          this.selectedEmployeeName = this.employees[i].name
        }
      }
      this.editValues.projectsField = this.currentClient.projectIDs
      this.activeEditPromt = true;

    },

    /**
     * Sets clientSelected to true to show detailed view of client
     * @param id of client
     */
    updateDetailedClient(id){
      this.fetchCustomer(id)
      this.clientSelected = true
    },

    /**
     * Updated the contact person of client
     * @param id of client
     * @param name of employee
     */
    updateSelectedEmployee(id, name){
      this.selectedEmployee = id;
      this.selectedEmployeeName = name;
    },

    closeDeletePrompt: function (message) {
      this.activeDeletePrompt = false;
      this.closeDeleteAlert(message)

    },

    closeDeleteAlert(message) {
      this.$vs.notify({
        title: 'Cancelled:',
        color: "rgb(187, 138, 200)", type: "gradient",
        text: message
      })
    },

    /**
     * Notifies that deletion succeeded
     */
    deleteAlert() {
      this.$vs.notify({
        title: 'Confirmation:',
        text: 'Project has been successfully deleted.'
      })
    },

    validForDelete: async function(client){
      var projects = client.projectIDs.split("-")
      var bool = true
      for(var i = 0; i<projects.length;i++){
        await axios.get(`http://localhost:8080/projects/` + projects[i])
            .then(response => {
                  var today = new Date().getTime()
                  var date = new Date(response.data.plannedEnd)
                  date = date.getTime()
                  if(today < date){
                    bool = false

                  }
                })
            }
            return bool;
    },

    async updateDeleteClient(client){
      var valid = await this.validForDelete(client)
      if(valid){
        this.currentClient = client
        this.activeDeletePrompt = true
      }else{
        this.$vs.notify({
          title: 'Warning:',
          color: "rgb(219,9,29)", type: "gradient",
          text: 'Client cannot be deleted because it still has open projects'
        })
      }

    }

  }
}
</script>

<style>
</style>
