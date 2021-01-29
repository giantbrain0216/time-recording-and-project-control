
<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="showDetailedView ? 6 : 12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Client List</h2>
            <div class="float-right mb-1">
              <vs-button @click="prompts.activePrompt = true" color="primary" icon="person_add" type="filled">Add New Client
              </vs-button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">EMAIL</th>
                <th class="border-top-0" style="color: cornflowerblue">ADDRESS</th>
                <!--                <th class="border-top-0" style="color: cornflowerblue">Project IDs</th>-->
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="client in pagination.viewableClients" :key="client.clientID">
                <td>{{ client.clientID }}</td>
                <td>
                  <div class="d-flex align-items-center">
                    <a @click="fetchCustomerAndUpdateCurrentClient(client.clientID);showDetailedView = true">
                      <div class="mr-2">
                        <vs-avatar color="primary" :text="client.name"/>
                      </div>
                    </a>
                    <div class="">
                      <a @click="fetchCustomerAndUpdateCurrentClient(client.clientID);showDetailedView = true"
                         class="m-b-0" style="cursor:pointer"> {{ client.name }}</a>
                    </div>
                  </div>
                </td>
                 <td>
                  <i><a :href="`mailto:${client.email}`">{{ client.email }}</a></i></td>
                <td>{{ client.address }}</td>
                <!--                <td>{{ fetchProjectsByClient(client.clientID) }}</td>-->
                <td>
                  <div>
                    <vs-button style="width:80px" @click="checkDeletePermission(client)" icon="delete" class="m-1" color="danger"
                               type="filled">
                      Delete
                    </vs-button>
                    <vs-button style="width:80px"  @click="updateEditID(client.clientID)" icon="edit" class="m-1" color="warning"
                               type="filled"> Edit
                    </vs-button>
                    <ExportInvoiceButton :client-i-d="client.clientID"/>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>

          </div>
          <div style="width: 20%;margin: auto;" id="pagination"><vs-pagination
              :total="pagination.maxPages" v-model="pagination.currentPage" prev-icon="arrow_back"
              next-icon="arrow_forward" style="justify-content: center;"></vs-pagination></div>
        </vs-card>
      </vs-col>
      <vs-col v-if="showDetailedView" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="showDetailedView" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off"
                       @click="showDetailedView = false"></vs-button>
            <h1>Details of {{ currentClient.name }} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{ currentClient.name }}</p>
            <hr>
            <p><strong>Client ID: </strong>{{ currentClient.clientID }}</p>
            <hr>
            <p><strong>Phone Number: </strong>{{ currentClient.telephoneNumber }}</p>
            <hr>
            <p><strong>Contact Person: </strong>{{ currentClient.contactPersonID + ". " +  getEmployeeName(currentClient.contactPersonID) }}</p>
            <hr>
            <vs-list class="mb-2" >
              <vs-list-header title="Projects of this Client" > </vs-list-header>
              <h6></h6>
              <vs-list-item class="ml-2" v-for="project in currentClient.projects" icon='arrow_right' :key="project.projectNumber"
                            :title="project.projectName"  >
              </vs-list-item>
            </vs-list>
            <hr>
            <p><strong>Address: </strong>{{ currentClient.address }}</p>
            <div><Map :address="currentClient.address" :name="currentClient.name"/></div>


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
        <vs-input label-placeholder="Name" class="mb-4" v-model="inputValues.nameField"/>
        <vs-input label-placeholder="Email" class="mb-4" v-model="inputValues.emailField"/>
        <vs-input label-placeholder="Address" class="mb-4" v-model="inputValues.addressField"/>
        <vs-input type="number" label-placeholder="Tel" class="mb-4" v-model="inputValues.numberField"/>
        <autocomplete
            ref="textSearchOfEmployeeAdd"
            :search="filterEmployeeItemsAdd"
            :get-result-value="getClientResultValue"
            @submit="handleClientSubmitAdd"
            placeholder="Search for a employee"
            aria-label="Search for a employee"
            v-if="!selectedEmployeeAdd"
            class="mb-3"
            style="width: 88%"
            auto-select
        ></autocomplete>
        <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2" v-if="selectedEmployeeAdd">
          <div>Selected contact person:</div>
          <div class="ml-1" style="color:royalblue;">{{ selectedEmployeeName }}</div>
          <vs-button class="ml-2" @click='selectedEmployeeAdd=false;selectedEmployeeName="";selectedEmployeeID=0' radius color="danger" type="border" icon="close" style="width:10px !important;height:10px !important;"></vs-button>
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
        @cancel='resetAllValues();notify("Closed","Edit was cancelled","warning")'
        @accept="updateClient"
        @close='resetAllValues();notify("Closed","Edit was cancelled","warning")'
        :is-valid="validClientEdit"
        :active.sync="prompts.activeEditPromt"
    >
      <div class="con-exemple-prompt">

        <div></div>
        <div>Name</div>
        <vs-input :placeholder="editValues.nameField" class="mb-3" v-model="editValues.nameField"/>
        Email
        <vs-input :placeholder="editValues.emailField" class="mb-3" v-model="editValues.emailField"/>
        Address
        <vs-input :placeholder="editValues.addressField" class="mb-3" v-model="editValues.addressField"/>
        Telephone Number
        <vs-input :placeholder="editValues.numberField" class="mb-3" v-model="editValues.numberField"/>
        <autocomplete
            ref="textSearchOfEmployeeEdit"
            :search="filterEmployeeItemsAdd"
            :get-result-value="getClientResultValue"
            @submit="handleClientSubmitEdit"
            placeholder="Search for a employee"
            aria-label="Search for a employee"
            auto-select
            v-if="!selectedEmployeeEdit"
            class="mb-3 mt-2"
            style="width: 88%"
        ></autocomplete>
        <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2" v-if="selectedEmployeeEdit">
          <div>Selected contact person:</div>
          <div class="ml-1" style="color:royalblue;">{{ selectedEmployeeName }}</div>
          <vs-button class="ml-2" @click='selectedEmployeeEdit=false;selectedEmployeeName="";selectedEmployeeID=0' radius color="danger" type="border" icon="close" style="width:10px !important;height:10px !important;"></vs-button>
        </div>


        <vs-list class="mb-2" >
          <vs-list-header color="warning" title="Projects of this Client" > </vs-list-header>
          <h6></h6>
          <vs-list-item class="ml-2" v-for="project in currentClient.projects" icon='arrow_right' :key="project.projectNumber"
                        :title="project.projectName"  >
          </vs-list-item>
        </vs-list>
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
        title="Delete Client"
        color="danger"
        @cancel='prompts.activeDeletePrompt = false; projectsCurrentClient=[];notify("Closed","Client has not been deleted","warning")'
        @accept="deleteClient(currentClient.clientID)"
        @close='prompts.activeDeletePrompt = false; projectsCurrentClient=[];notify("Closed","Client has not been deleted","warning")'
        :is-valid="projectsCurrentClient.length===0"
        :active.sync="prompts.activeDeletePrompt"
    >
      <div class="con-exemple-prompt">
        <div v-show="projectsCurrentClient.length===0">
        <h4>Are you sure to delete the client :</h4>  <h5>{{ currentClient.name }}</h5> </div>
        <div  v-show="projectsCurrentClient.length!==0" class="mt-2">
          <h6 style="color: red">You cannot delete {{currentClient.name }}, because he has running projects.</h6>

          <div>
          <vs-list class="mb-2" >
          <vs-list-header title="Project of this Client" > </vs-list-header>
          <h6></h6>
          <vs-list-item class="ml-2" v-for="project in projectsCurrentClient" :key="project.projectNumber"
                         :title="project.projectName"  >
          </vs-list-item>
        </vs-list>
            <vs-button v-show="projectsCurrentClient.length!==0"  class="mt-4" style="width: 100%;"
                       @click="projectsCurrentClient=[]"  color="danger">Delete All Running Projects</vs-button>
          </div>
        </div>

      </div>
    </vs-prompt>
  </div>
</template>

<script>
import axios from 'axios';
import ExportInvoiceButton from "@/views/components/ExportInvoice/ExportInvoiceButton";
import Map from "@/views/components/LeafletMap/Map";

export default {
  name: "clientList",
  components: {ExportInvoiceButton, Map},
  data: () => {
    return {
      employees: [],
      projectsCurrentClient: [],
      selectedEmployeeID: 0,
      selectedEmployeeName: "Contact Person",
      selectedEmployeeAdd: false,
      selectedEmployeeEdit: false,

      clients: [],
      currentClient: {},
      showDetailedView: false,
      inputValues: {
        nameField: '',
        addressField: '',
        emailField: '',
        numberField: '',
      },
      editValues: {
        nameField: '',
        emailField: '',
        numberField: '',
        addressField: '',
        projectsField: ''
      },
      prompts: {
        activePrompt: false,
        activeEditPromt: false,
        activeDeletePrompt: false,
      },
      map: null,
      mapCenter: {lat: 0, lng: 0},
      pagination: {maxPages:0,currentPage:1,viewableClients:[]}
    };
  },

  watch: {
    returnCurrentPage(){
      // eslint-disable-next-line no-console
      console.log("Page changed")
      var currentPage = this.pagination.currentPage
      if(7+(currentPage-1)*7 < this.clients.length){
        this.pagination.viewableClients = this.clients.slice(0+(currentPage-1)*7,7+(currentPage-1)*7)
      }else{
        this.pagination.viewableClients = this.clients.slice(0+(currentPage-1)*7,this.clients.length)
      }

    }
  },

  async created() {
    await this.fetchCustomers();
    await this.fetchEmployees()
    this.pagination.maxPages = Math.ceil(this.clients.length / 7)
    if(this.clients.length < 7){
      this.pagination.viewableClients = this.clients.slice(0,this.clients.length)
    }else{
      this.pagination.viewableClients =this.clients.slice(0,7)
    }
  },

  computed: {
    /** Checks if input values for add client are valid*/
    validClient() {
      var re = /\S+@\S+\.\S+/;
      return (this.inputValues.nameField.length > 0
          && 100 > this.inputValues.emailField.length && this.inputValues.emailField.length > 1
          && 41 > this.inputValues.numberField.length && this.inputValues.numberField.length > 7 && this.inputValues.addressField.length > 0
          && re.test(this.inputValues.emailField)
          && this.selectedEmployeeID !== 0 && this.inputValues.numberField > 0
      )
    },
    /** Checks if input values for edit client are valid*/
    validClientEdit() {
      var re = /\S+@\S+\.\S+/;
      return (this.editValues.nameField.length > 0
          && 50 > this.editValues.emailField.length && this.editValues.emailField.length > 4
          && 41 > this.editValues.numberField.length && this.editValues.numberField.length > 7
          && this.editValues.addressField.length > 0
          && re.test(this.editValues.emailField)
          && this.selectedEmployeeID !== 0)
    },

    returnCurrentPage(){
      return this.pagination.currentPage
    }
  },

  methods: {

    /**Filters items for searchbar of clients on add form*/
    async filterEmployeeItemsAdd(input) {

      if (input.length < 1) { return [] }

      else if(input.length < 2){return this.employees.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()))
      })}
      else{
        return this.employees.filter(competence => {
          // eslint-disable-next-line no-console
          return (competence.name.toLowerCase()
              .includes(input.toLowerCase()))
        })
      }
    },
    /**Returns name of the client objects*/
    getClientResultValue(result) {
      return result.name
    },
    /**Handle function when client is selected by searchbar edit form*/
    handleClientSubmitAdd(result) {
      this.selectedEmployeeID = result.employeeID
      this.selectedEmployeeName = result.name
      this.$refs.textSearchOfEmployeeAdd.value = ""
      this.selectedEmployeeAdd = true;

    },

    handleClientSubmitEdit(result) {
      this.selectedEmployeeID = result.employeeID
      this.selectedEmployeeName = result.name
      this.$refs.textSearchOfEmployeeEdit.value = ""
      this.selectedEmployeeEdit = true;

    },

    updatePagesAfterAddOrDelete(){
      var maxPages = Math.ceil(this.clients.length / 7)
      if(maxPages < this.pagination.maxPages){
        this.pagination.maxPages = maxPages
        this.pagination.currentPage = maxPages
      }else if(maxPages > this.pagination.maxPages){
        this.pagination.maxPages = maxPages
        this.pagination.currentPage = maxPages
      }
      var currentPage = this.pagination.currentPage
      if(7+(currentPage-1)*7 < this.clients.length){
        this.pagination.viewableClients = this.clients.slice(0+(currentPage-1)*7,7+(currentPage-1)*7)
      }else{
        this.pagination.viewableClients = this.clients.slice(0+(currentPage-1)*7,this.clients.length)
      }


    },



    /**
     * Sets the editValues to the data of the current client
     *
     * @return client list with updated current client
     */
    async updateClient() {
      await axios.put(`http://localhost:8080/clients/`, {
        'clientID': this.currentClient.clientID,
        'name': this.editValues.nameField,
        'email': this.editValues.emailField,
        'address': this.editValues.addressField,
        'telephoneNumber': this.editValues.numberField,
        'contactPersonID': this.selectedEmployeeID,
      }).then(() => {
        this.notify("Success", "Client was successfully updated", "success")
      }).catch((error) => {
        if (error.response) {
          this.notify("Client Edit Error", error.message, "danger")
        }
      })
      await this.fetchCustomers()
      this.updatePagesAfterAddOrDelete()
    },

    /**
     * Gets client from DB
     *
     * @param id of client
     * @return curreentClient with client data of DB
     */
    fetchCustomerAndUpdateCurrentClient: async function (id) {
      var projects = []
      await axios.get('http://localhost:8080/projectsByClient/' + id).then(async (response) => {

        for(var i = 0;i< response.data.length;i++){
          await axios.get('http://localhost:8080/projects/' + response.data[i]).then((response1) => {
            projects.push(response1.data)
          })
        }


      })

      await axios.get(`http://localhost:8080/clients/${id}`)
          .then(async response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            this.currentClient = response.data
            this.currentClient.projects = projects

          }).catch((error) => {
            if (error.response) {
              this.notify("Client Database Error", error.message, "danger")
            }
          })


    },

    /**
     * Gets all employees from DB
     *
     * @return employees
     */
    fetchEmployees: async function () {
      await axios.get(`http://localhost:8080/employees/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.employees = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Employees Database Error", error.message, "danger")
            } else {
              this.notify("Employees Database Error", "Connection to Database Error", "danger")
            }
          })

    },

    /**
     * Gets all clients from DB
     *
     * @return clients
     */
    fetchCustomers: async function () {
      await axios.get(`http://localhost:8080/clients/`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.clients = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Clients Database Error", error.message, "danger")
            } else {
              this.notify("Clients Database Error", "Connection to Database Error", "danger")
            }
          })
    },

    /**
     * Adds an client to DB
     *
     * @return updated clients
     */
    addClient: async function () {
      await axios.post('http://localhost:8080/clients', {
        'name': this.inputValues.nameField,
        'email': this.inputValues.emailField,
        'telephoneNumber': this.inputValues.numberField,
        'contactPersonID': this.selectedEmployeeID,
        'address': this.inputValues.addressField
      }).then(() => {
        this.notify("Notification:", "Employee was added.", "success")
      }).catch((error) => {
        if (error.response) {
          this.notify("Add Client Error", error.message, "danger")
        }
      })

      await this.fetchCustomers()
      this.resetAllValues()
      this.updatePagesAfterAddOrDelete()

    },

    /**
     * Deletes selected client
     *
     * @param id of client
     * @return updated clients
     */
    deleteClient: async function (id) {
      await axios.delete(`http://localhost:8080/clients/` + id).then(async () => {
        await axios.delete(`http://localhost:8080/allAssignedProjectsClient/${id}`).then(async (response1) => {
          for (let i = 0; i < response1.data.length; i++) {
            await axios.delete(`http://localhost:8080/projects/` + response1.data[i])
            await axios.delete(`http://localhost:8080/assignmentsbyproject/${response1.data[i]}`);
            await axios.delete(`http://localhost:8080/allAssignedCompetencesProject/${response1.data[i]}`);
          }
        });

        this.notify("Confirmation", "Client has been successfully deleted.", "danger")

      }).catch((error) => {
        if (error.response) {
          this.notify("Error", error.message, "danger");

        }
      })
      this.prompts.activeDeletePrompt = false
      await this.fetchCustomers()
      this.currentClient = {}
      this.updatePagesAfterAddOrDelete()
    },

    /**
     * Sets the editValues to values of current client and activates editing prompt
     *
     * @param id of client
     */
    async updateEditID(id) {
      await this.fetchCustomerAndUpdateCurrentClient(id);
      this.editValues.nameField = this.currentClient.name
      this.editValues.emailField = this.currentClient.email
      this.editValues.numberField = this.currentClient.telephoneNumber
      this.selectedEmployeeID = this.currentClient.contactPersonID
      for (let i = 0; i < this.employees.length; i++) {
        if (this.employees[i].employeeID === this.currentClient.contactPersonID) {
          this.selectedEmployeeName = this.employees[i].name
        }
      }
      this.editValues.addressField = this.currentClient.address
      this.editValues.projectsField = this.currentClient.projects
      this.prompts.activeEditPromt = true;

    },


    /** Checks if a client has any opened projects before delete */
    validForDelete: async function (client) {

      //var projects = client.projectIDs.split(" ")
      var projects = []

      await axios.get(`http://localhost:8080/projectsByClient/` + client.clientID).then((response) => {
        projects = response.data

      }).catch((error) => {
        if (error.response) {
          this.notify("Error", error.message, "danger");

        }
      })

      // eslint-disable-next-line no-console
      console.log(projects)

      if (projects.length == 0) {
        return true
      } else {
        return false
      }

      // var bool = true
      // for (var i = 0; i < projects.length; i++) {
      //   await axios.get(`http://localhost:8080/projects/` + projects[i])
      //       .then(response => {
      //         var today = new Date().getTime()
      //         var date = new Date(response.data.plannedEnd)
      //         date = date.getTime()
      //         if (today < date) {
      //           bool = false
      //
      //         }
      //       })
      // }
      // return bool;
    },

    /** When delete button is pressed, checks if client is valid for deletion. If yes, shows delete prompt*/
    async checkDeletePermission(client) {
   /*   var valid = await this.validForDelete(client)
      if (valid) {*/
        this.currentClient = client
        this.prompts.activeDeletePrompt = true
      await axios.get('http://localhost:8080/projectsByClient/' + client.clientID).then( async response => {
        for (let i=0;i<response.data.length;i++){
          await axios.get('http://localhost:8080/projects/' + response.data[i]).then(response1 => {
            this.projectsCurrentClient.push(response1.data)
          })
        }
      }).catch((error) => {
        if(error.response){
          this.notify("Assignment Database Error", error.message,"danger")
        }
      })
  /*   } else {
        this.notify("Warning", "Client cannot be deleted because there are still projects associated to this client", "danger")
      }*/

    },

    getEmployeeName(employeeID){
      for(var i=0;i<this.employees.length;i++){
        if(this.employees[i].employeeID == employeeID){
          return this.employees[i].name
        }
      }
    },

    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },

    /** Resets all values of input and edit fields. Also resets the values for the employee dropdown*/
    resetAllValues: function () {
      this.inputValues.nameField = '';
      this.inputValues.emailField = '';
      this.inputValues.numberField = '';
      this.inputValues.addressField = '';
      this.selectedEmployeeName = "Contact Person"
      this.selectedEmployeeID = 0;
      this.editValues.nameField = '';
      this.editValues.emailField = '';
      this.editValues.numberField = '';
      this.editValues.addressField = '';
      this.editValues.projectsField = '';
      this.currentClient = {};
      this.selectedEmployeeAdd= false;
      this.selectedEmployeeEdit= false;
    }



  }
}
</script>

<style>

</style>
