<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="clientSelected ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
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
          <tr v-for="client in clients" :key="client.clientID">
            <td>
              <div class="d-flex align-items-center">
                <a @click="updateDetailedClient(client.clientID)"> <div class="mr-2"><vs-avatar color="primary" :text="client.name"/></div></a>
                <div class="">
                  <a @click="updateDetailedClient(client.clientID)" class="m-b-0" style="cursor:pointer"> {{ client.name }}</a>
                </div>
              </div>
            </td>
            <td>{{client.clientID}}</td>
            <td>
              <div>
                <vs-button @click="deleteClient(client.clientID)" class="m-1" color="danger" type="filled">
                  Delete
                </vs-button>
                <vs-button @click="updateEditID(client.clientID)" class="m-1" color="primary" type="filled">
                  Edit
                </vs-button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
        </vs-card>
      </vs-col>
      <vs-col v-if="clientSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="clientSelected" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off" @click="clientSelected = false"></vs-button>
            <h1 >Details vom {{currentClient.name}} </h1>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentClient.name}}</p>
            <hr>
            <p><strong>Email: </strong>{{currentClient.clientID}}</p>
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
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Customer</vs-button>
      <vs-prompt
        title="Add Client"
        color="danger"
        @cancel="closeAdd"
        @accept="addClient"
        @close="closeAdd"
        :is-valid="validClient"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Kundendaten eingeben
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Email" class="mb-3" v-model="inputValues.emailField"/>
          <vs-input placeholder="Tel" class="mb-3" v-model="inputValues.numberField"/>
          <vs-input placeholder="Contact person" type="integer" class="mb-3" v-model="inputValues.cPersonField"/>
          <vs-input placeholder="Projects (IDs)" class="mb-3" v-model="inputValues.projectsField"/>
          <vs-alert
            :active="!validClient"
            color="danger"
            icon="new_releases"
          >
          Die Felder müssen gefüllt werden.
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
          <vs-input :placeholder="editValues.nameField" class="mb-3" v-model="editValues.nameField" />
          <vs-input :placeholder="editValues.emailField" class="mb-3" v-model="editValues.emailField"/>
          <vs-input :placeholder="editValues.numberField" class="mb-3" v-model="editValues.numberField"/>
          <vs-input :placeholder="editValues.cPersonField" class="mb-3" v-model="editValues.cPersonField"/>
          <vs-input :placeholder="editValues.projectsField" class="mb-3" v-model="editValues.projectsField"/>
          <vs-alert
              :active="!validClientEdit"
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

export default {
  name: "clientList",
  data: () => {
    return {
      clients: [],
      currentClient:{},
      clientSelected:false,
      activePrompt:false,
      activeEditPromt:false,
      inputValues: {
        nameField: '',
        emailField: '',
        numberField: '',
        cPersonField: '',
        projectsField: ''
      },
      editValues: {
        nameField: '',
        emailField: '',
        numberField: '',
        cPersonField: '',
        projectsField: ''
      },
    };
  },

  created() {
    this.fetchCustomers();


  },

  computed:{
      validClient(){
        return (this.inputValues.nameField.length > 0
                && 26 > this.inputValues.emailField.length && this.inputValues.emailField.length > 4
                && 41 > this.inputValues.numberField.length && this.inputValues.numberField.length > 7
                && this.inputValues.emailField.includes('@')
                && this.inputValues.cPersonField.isInteger
                )
      },
    validClientEdit(){
      return (this.editValues.nameField.length > 0
          && 26 > this.editValues.emailField.length && this.editValues.emailField.length> 4
          && 41 > this.editValues.numberField.length && this.editValues.numberField.length> 7
          && this.inputValues.emailField.includes('@')
          && this.inputValues.cPersonField.isInteger
      )
    }
  },

  methods: {
    async updateClient(){
      await axios.put(`http://localhost:8080/clients/`,{
        'id':this.currentClient.clientID,
        'name': this.editValues.nameField,
        'email': this.editValues.emailField,
        'telephoneNumber': this.editValues.numberField,
        'contactPersonID': parseInt(this.editValues.cPersonField),
        'projectIDs': this.editValues.projectsField
      })
      this.fetchCustomers()


    },



    acceptAlert(){
      this.$vs.notify({
        title:'Benachrichtigung:',
        text:'Mitarbeiter wurde erfolgreich angelegt.'
      })
    },
    closeAdd(){
      this.inputValues.nameField = '',
      this.inputValues.emailField = '',
      this.inputValues.numberField = '',
      this.inputValues.cPersonField = '',
      this.inputValues.projectsField = '',
      this.$vs.notify({
        title:'Beendet',
        text:'Hinzufügen wurde abgebrochen.'
      })
    },

    closeEdit(){
      this.editValues.nameField = '',
          this.editValues.emailField = '',
          this.editValues.numberField = '',
          this.editValues.cPersonField = '',
          this.editValues.projectsField = '',
          this.$vs.notify({
            title:'Closed',
            text:'Edit was cancelled.'
          })
    },

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

    
    addClient: async function() {
      await axios.post('http://localhost:8080/clients' , {
        'name': this.inputValues.nameField,
        'email': this.inputValues.emailField,
        'telephoneNumber': this.inputValues.numberField,
        'contactPersonID': parseInt(this.inputValues.cPersonField),
        'projectIDs': this.inputValues.projectsField
      })
      this.acceptAlert()
      await this.fetchCustomers()
    },

    deleteClient: async function(id){
      await axios.delete(`http://localhost:8080/clients/` + id)
      await this.fetchCustomers()
    },

    async updateEditID(id){
      await this.fetchCustomer(id);
      this.editValues.nameField = this.currentClient.name
      this.editValues.emailField = this.currentClient.email
      this.editValues.numberField = this.currentClient.telephoneNumber
      this.editValues.cPersonField = this.currentClient.contactPersonID
      this.editValues.projectsField = this.currentClient.projectIDs
      this.activeEditPromt = true;

    },

    updateDetailedClient(id){
      this.fetchCustomer(id)
      this.clientSelected = true
    }

  }
}
</script>

<style scoped>
</style>
