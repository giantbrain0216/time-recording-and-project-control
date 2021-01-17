<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="showDetailedView ? 6 : 12" vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Client List</h2>
            <div class="float-right mb-1">
              <vs-button @click="prompts.activePrompt = true" color="primary" icon="add" type="filled">Add New Client
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
              <tr v-for="client in clients" :key="client.clientID">
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
                <td>{{ client.email }}</td>
                <td>{{ client.address }}</td>
<!--                <td>{{ fetchProjectsByClient(client.clientID) }}</td>-->
                <td>
                  <div>
                    <vs-button @click="checkDeletePermission(client)" icon="delete" class="m-1" color="danger"
                               type="filled">
                      Delete
                    </vs-button>
                    <vs-button @click="updateEditID(client.clientID)" icon="edit" class="m-1" color="warning"
                               type="filled"> Edit
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
            <p><strong>Contact Person (ID): </strong>{{ currentClient.contactPersonID }}</p>
            <hr>
            <p><strong>Projects Names: </strong></p>
            <ul class="ml-3 mt-2">
              <li v-for="projectnamedetail in currentClient.projectsNames" :key="projectnamedetail">{{projectnamedetail}}</li>
            </ul>
            <hr>
            <p><strong>Address: </strong>{{ currentClient.address}}</p>


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
            :get-result-value="getEmployeeResultValue"
            @submit="handleEmployeeSubmitAdd"
            placeholder="Choose contact person"
            aria-label="Search for a employee"
            auto-select
        ></autocomplete>
        <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
          <div>Selected Employee:</div>
          <div class="ml-1" style="color:royalblue;">{{this.selectedEmployeeName }}</div>
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
            ref="textSearchOfEmployeeAdd"
            :search="filterEmployeeItemsAdd"
            :get-result-value="getEmployeeResultValue"
            @submit="handleEmployeeSubmitAdd"
            placeholder="Choose contact person"
            aria-label="Search for a employee"
            auto-select
        ></autocomplete>
        <div class="d-flex align-items-center dropdownbtn-alignment mb-3 mt-2">
          <div>Selected Employee:</div>
          <div class="ml-1" style="color:royalblue;">{{this.selectedEmployeeName }}</div>
        </div>
        <hr>
        <h5>Projects of the Client</h5>
        <ul class="ml-3 mt-2">
          <li v-for="projectname in editValues.projectsField" :key="projectname">{{projectname}}</li>
        </ul>
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
      employees: [],
      selectedEmployeeID: 0,
      selectedEmployeeName: "None",
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
        projectsField: []
      },
      prompts: {
        activePrompt: false,
        activeEditPromt: false,
        activeDeletePrompt: false,
      },
      map:null,
      mapCenter:{lat:0,lng:0},
    };
  },

  async created() {
    await this.fetchCustomers();
    await this.fetchEmployees()
  },

  computed: {
    /** Checks if input values for add client are valid*/
    validClient() {
      var re = /\S+@\S+\.\S+/;
      return (this.inputValues.nameField.length > 0
          && 50 > this.inputValues.emailField.length && this.inputValues.emailField.length > 4
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
          && this.editValues.addressField.length >0
          && re.test(this.editValues.emailField)
          && this.selectedEmployeeID != 0
      )
    }
  },

  methods: {
    /**Filters items for searchbar of employees on add form*/
    async filterEmployeeItemsAdd(input) {

      if (input.length < 1) { return [] }

      return this.employees.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()))
      })
    },
    /**Returns name of the employee objects*/
    getEmployeeResultValue(result){
      return result.name
    },
    /**Handle function when employee is selected by searchbar add form*/
    handleEmployeeSubmitAdd(result){
      this.selectedEmployeeName = result.name
      this.selectedEmployeeID = result.employeeID
      this.$refs.textSearchOfEmployeeAdd.value = ""

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


    },

    /**
     * Gets client from DB
     *
     * @param id of client
     * @return curreentClient with client data of DB
     */
    fetchCustomerAndUpdateCurrentClient: async function (id) {
      var arrayProjectNames = []
      await axios.get('http://localhost:8080/projectsByClient/' + id).then(async (response) => {
        var arr = response.data
        // eslint-disable-next-line no-console
        console.log(arr)
        // eslint-disable-next-line no-console
        for(var i=0;i<arr.length;i++){
          await axios.get('http://localhost:8080/projects/' + arr[i]).then((response) => {
            arrayProjectNames.push(response.data.projectName)
          })
        }

      })

      await axios.get(`http://localhost:8080/clients/${id}`)
          .then(async response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            this.currentClient = response.data
            this.currentClient.projectsNames = arrayProjectNames

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
        this.notify("Notification:", "Client was added.", "success")
      }).catch((error) => {
        if (error.response) {
          this.notify("Add Client Error", error.message, "danger")
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
    deleteClient: async function (id) {
      await axios.delete(`http://localhost:8080/clients/` + id).then(() => {
        this.notify("Confirmation", "Client has been successfully deleted.", "danger")

      }).catch((error) => {
        if (error.response) {
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
    async updateEditID(id) {
      await this.fetchCustomerAndUpdateCurrentClient(id);
      this.editValues.nameField = this.currentClient.name
      this.editValues.emailField = this.currentClient.email
      this.editValues.numberField = this.currentClient.telephoneNumber
      this.selectedEmployeeID = this.currentClient.contactPersonID
      for (var i = 0; i < this.employees.length; i++) {
        if (this.employees[i].employeeID == this.currentClient.contactPersonID) {
          this.selectedEmployeeName = this.employees[i].name
        }
      }
      this.editValues.addressField = this.currentClient.address
      this.editValues.projectsField = this.currentClient.projectsNames
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

      if(projects.length ==  0){
        return true
      }else{
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
      var valid = await this.validForDelete(client)
      if (valid) {
        this.currentClient = client
        this.prompts.activeDeletePrompt = true
      } else {
        this.notify("Warning", "Client cannot be deleted because there are still projects associated to this client", "danger")
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
      this.selectedEmployeeName = "None";
      this.selectedEmployeeID = 0;
      this.editValues.nameField = '';
      this.editValues.emailField = '';
      this.editValues.numberField = '';
      this.editValues.addressField = '';
      this.editValues.projectsField = '';
      this.currentClient = {};
    }

  }
}
</script>

<style>

</style>
