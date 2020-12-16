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
                <a @click="fetchCustomer(client.clientID)"> <div class="mr-2"><vs-avatar color="primary" :text="client.name"/></div></a>
                <div class="">
                  <a @click="fetchCustomer(client.clientID)" class="m-b-0" style="cursor:pointer"> {{ client.name }}</a>
                </div>
              </div>
            </td>
            <td>{{client.clientID}}</td>
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
      <vs-col v-if="clientSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="clientSelected" class="cardx">
          <div slot="header">
            <h4>Details vom {{currentClient.name}}</h4>
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
        title="Kunde Hinzufügen"
        color="danger"
        @cancel="close"
        @accept="addClient"
        @close="close"
        :is-valid="validClient"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Kundendaten eingeben
          <vs-input placeholder="Name" class="mb-3" v-model="inputValues.nameField" />
          <vs-input placeholder="Email" class="mb-3" v-model="inputValues.emailField"/>
          <vs-input placeholder="Tel" class="mb-3" v-model="inputValues.numberField"/>
          <vs-input placeholder="Contact person" class="mb-3" v-model="inputValues.cPersonField"/>
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
      inputValues: {
        nameField: '',
        emailField: '',
        numberField: '',
        cPersonField: '',
        projectsField: ''
      }
    };
  },

  created() {
    axios.get(`http://localhost:8080/clients/`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.clients = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })


  },

  computed:{
      validClient(){
        return (this.inputValues.nameField.length > 0
                && 26 > this.inputValues.emailField.length > 4
                && 41 > this.inputValues.numberField.length > 7
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

    fetchCustomer: function(id){
      axios.get(`http://localhost:8080/clients/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentClient = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      this.clientSelected = true
    },

    deleteClient: function(id) {
      axios.delete(`http://localhost:8080/clients/${id}`)
          .then((response) => {
            this.inputValues.nameField = ''
            this.inputValues.emailField = ''
            this.inputValues.numberField = ''
            this.inputValues.cPersonField = ''
            this.inputValues.projectsField = ''
            this.clients = response.data
          })
    },
    
    addClient: function() {
      axios.post('http://localhost:8080/clients' + {
        'name': this.inputValues.nameField,
        'email': this.inputValues.emailField,
        'telephoneNumber': this.inputValues.numberField,
        'contactPersonID': parseInt(this.inputValues.cPersonField),
        'projectIDs': this.inputValues.projectsField
      }).then((response) => this.clients = response.data)
    }
  }
}
</script>

<style scoped>
</style>