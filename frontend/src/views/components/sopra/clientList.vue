<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="showDetailedView ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Client List</h2>
            <div class="float-right mb-1"><vs-button @click="prompts.activePrompt = true" color="primary" icon="add" type="filled">Add New Client</vs-button></div>
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
                <a @click="fetchCustomerAndUpdateCurrentClient(client.clientID);showDetailedView = true">
                  <div class="mr-2"><vs-avatar color="primary" :text="client.name"/></div></a>
                <div class="">
                  <a @click="fetchCustomerAndUpdateCurrentClient(client.clientID);showDetailedView = true" class="m-b-0" style="cursor:pointer"> {{ client.name }}</a>
                </div>
              </div>
            </td>
            <td>{{client.email}}</td>
            <td>{{client.projectIDs}}</td>
            <td>
              <div>
                <vs-button @click="checkDeletePermission(client)" icon="delete" class="m-1" color="danger" type="filled">
                </vs-button>
                <vs-button @click="updateEditID(client.clientID)" icon="edit" class="m-1" color="warning" type="filled">
                </vs-button>
                <ExportInvoiceButton :client-i-d="client.clientID"/>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="showDetailedView" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="showDetailedView" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off" @click="showDetailedView = false"></vs-button>
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
    </vs-row>

    <vs-prompt
        title="Add Client"
        color="primary"
        @cancel='resetAllValues();notify("Closed","Adding was cancelled", "warning")'
        @accept="addClient"
        @close='resetAllValues();notify("Closed","Adding was cancelled", "warning")'
        :is-valid="validClient"
        :active.sync="prompts.activePrompt"
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
              <vs-dropdown-item @click="selectedEmployeeID=employee.employeeID;selectedEmployeeName=employee.name" v-for="employee in employees" :key="employee.employeeID">
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
        @cancel='resetAllValues;notify("Closed","Edit was cancelled","warning")'
        @accept="updateClient"
        @close='resetAllValues;notify("Closed","Edit was cancelled","warning")'
        :is-valid="validClientEdit"
        :active.sync="prompts.activeEditPromt"
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
              <vs-dropdown-item @click="selectedEmployeeID=employee.employeeID;selectedEmployeeName=employee.name" v-for="employee in employees" :key="employee.employeeID">
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
    <vs-prompt
        title="Delete Project"
        color="danger"
        @cancel='prompts.activeDeletePrompt = false;notify("Closed","Client has not been deleted","warning")'
        @accept="deleteClient(currentClient.clientID)"
        @close='prompts.activeDeletePrompt = false;notify("Closed","Client has not been deleted","warning")'
        :is-valid="true"
        :active.sync="prompts.activeDeletePrompt"
    >
      <div class="con-exemple-prompt">
        <h4>Are you sure to delete the client :</h4>  <h5>{{ currentClient.name }}</h5>
      </div>
    </vs-prompt>
  </div>
</template>

<script>
import axios from 'axios';
import ExportInvoiceButton from "@/views/components/ExportInvoice/ExportInvoiceButton";

export default {
  name: "clientList",
  components: {ExportInvoiceButton},
  data: () => {
    return {
      employees:[],
      selectedEmployeeID:0,
      selectedEmployeeName:"Contact Person",
      clients: [],
      currentClient:{},
      showDetailedView:false,
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
      prompts:{
        activePrompt:false,
        activeEditPromt:false,
        activeDeletePrompt:false,}
    };
  },

  async created() {
    await this.fetchCustomers();
    await this.fetchEmployees()
  },

  computed:{
    /** Checks if input values for add client are valid*/
      validClient(){
        var re = /\S+@\S+\.\S+/;
        return (this.inputValues.nameField.length > 0
                && 50 > this.inputValues.emailField.length && this.inputValues.emailField.length > 4
                && 41 > this.inputValues.numberField.length && this.inputValues.numberField.length > 7
                && re.test(this.inputValues.emailField)
                && this.selectedEmployeeID !== 0 && this.inputValues.numberField > 0
                )
      },
    /** Checks if input values for edit client are valid*/
    validClientEdit(){
      var re = /\S+@\S+\.\S+/;
      return (this.editValues.nameField.length > 0
          && 50 > this.editValues.emailField.length && this.editValues.emailField.length> 4
          && 41 > this.editValues.numberField.length && this.editValues.numberField.length> 7
          && re.test(this.editValues.emailField)
          && this.selectedEmployeeID != 0
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
        'contactPersonID': this.selectedEmployeeID,
        'projectIDs': this.editValues.projectsField
      }).then(() =>{
        this.notify("Success","Client was successfully updated", "success")
      }).catch((error) => {
        if(error.response){
          this.notify("Client Edit Error", error.message,"danger")
        }
      })
      await this.fetchCustomers()


    },

    /**
     * Gets client from DB
     *
     * @param id of client
     * @return curreentClient with client data of DB
     */
    fetchCustomerAndUpdateCurrentClient: async function(id){
      await axios.get(`http://localhost:8080/clients/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            this.currentClient = response.data
          }).catch((error) => {
            if(error.response){
              this.notify("Client Database Error", error.message,"danger")
            }
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
          .catch((error) => {
            if(error.response){
              this.notify("Employees Database Error", error.message,"danger")
            }else{
              this.notify("Employees Database Error", "Connection to Database Error","danger")
            }
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
          .catch((error) => {
            if(error.response){
              this.notify("Clients Database Error", error.message,"danger")
            }else{
              this.notify("Clients Database Error", "Connection to Database Error","danger")
            }
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
        'contactPersonID': this.selectedEmployeeID,
        'projectIDs': ""
      }).then(() => {
        this.notify("Notification:","Employee was added.","success")
      }).catch((error) => {
        if(error.response){
          this.notify("Add Client Error", error.message,"danger")
        }
      })

      await this.fetchCustomers()
      this.resetAllValues()

    },

    /**
     * Deletes selected client
     *
     * @param id of client
     * @return updated clients
     */
    deleteClient: async function(id){
      await axios.delete(`http://localhost:8080/clients/` + id).then(() => {
        this.notify("Confirmation","Client has been successfully deleted.","danger")

      }).catch((error) => {
        if (error.response){
          this.notify("Error", error.message, "danger");

        }
      })
      this.prompts.activeDeletePrompt = false
      await this.fetchCustomers()
      this.currentClient = {}
    },

    /**
     * Sets the editValues to values of current client and activates editing prompt
     *
     * @param id of client
     */
    async updateEditID(id){
      await this.fetchCustomerAndUpdateCurrentClient(id);
      this.editValues.nameField = this.currentClient.name
      this.editValues.emailField = this.currentClient.email
      this.editValues.numberField = this.currentClient.telephoneNumber
      this.selectedEmployeeID = this.currentClient.contactPersonID
      for(var i =0;i<this.employees.length;i++){
        if(this.employees[i].employeeID == this.currentClient.contactPersonID){
          this.selectedEmployeeName = this.employees[i].name
        }
      }
      this.editValues.projectsField = this.currentClient.projectIDs
      this.prompts.activeEditPromt = true;

    },

    /** Checks if a client has any opened projects before delete */
    validForDelete: async function(client){
      var projects = client.projectIDs.split(" ")
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

    /** When delete button is pressed, checks if client is valid for deletion. If yes, shows delete prompt*/
    async checkDeletePermission(client){
      var valid = await this.validForDelete(client)
      if(valid){
        this.currentClient = client
        this.prompts.activeDeletePrompt = true
      }else{
        this.notify("Warning","Client cannot be deleted because it still has open projects", "danger")
      }

    },

    /** Shows prompt with title, message and selected color*/
    notify: function(title, message,color){
      this.$vs.notify({
        title: title,
        text: message,
        color:color, type: "gradient",
      })
    },

    /** Resets all values of input and edit fields. Also resets the values for the employee dropdown*/
    resetAllValues: function(){
      this.inputValues.nameField = '';
      this.inputValues.emailField = '';
      this.inputValues.numberField = '';
      this.selectedEmployeeName = "Contact Person"
      this.selectedEmployeeID = 0;
      this.editValues.nameField = '';
      this.editValues.emailField = '';
      this.editValues.numberField = '';
      this.editValues.projectsField = '';
    }

  }
}
</script>

<style>
</style>
