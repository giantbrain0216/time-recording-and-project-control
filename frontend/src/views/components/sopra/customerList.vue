<template>
  <div class="table-responsive">
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="customerSelected ? 6 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Name</th>
            <th class="border-top-0">ID</th>
            <th class="border-top-0">Location</th>
            <th class="border-top-0">-</th>
            </tr>
          </thead>
          <tbody>
          <tr v-for="customer in customers" :key="customer.id">
            <td>
              <div class="d-flex align-items-center">
                <a @click="fetchCustomer(customer.id)"> <div class="mr-2"><vs-avatar color="primary" :text="customer.name"/></div></a>
                <div class="">
                  <a @click="fetchCustomer(customer.id)" class="m-b-0" style="cursor:pointer"> {{ customer.name }}</a>
                </div>
              </div>
            </td>
            <td>{{customer.id}}</td>
            <td>{{customer.location }}</td>
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
      <vs-col v-if="customerSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="6" vs-xs="12">
        <vs-card v-show="customerSelected" class="cardx">
          <div slot="header">
            <h4>Details vom {{currentCustomer.name}}</h4>
          </div>
          <div>
            <p><strong>Name: </strong>{{currentCustomer.name}}</p>
            <p><strong>ID: </strong>{{currentCustomer.id}}</p>
            <h3>Location: </h3>
          </div>
        </vs-card>
      </vs-col>
      <vs-button @click="activePrompt = true" color="primary" type="filled">Add Customer</vs-button>
      <vs-prompt
        title="Kunde Hinzufügen"
        color="danger"
        @cancel="close"
        @accept="acceptAlert"
        @close="close"
        :is-valid="validName"
        :active.sync="activePrompt"
      >
        <div class="con-exemple-prompt">
          Bitte Kundendaten eingeben
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
  name: "customerList",
  data: () => {
    return {
      customers: [
        {id: 1, name: 'Clirim Salihi', location: 'Tangegartstr. 11'},
        {id: 2, name: 'Radu Manea', location: 'Kein plan wo' },
        {id: 3, name: 'Mohamed Ben Salha', location: 'i-wo :D'}
      ],
      currentCustomer:{},
      customerSelected:false,
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
          this.customers = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })


  },

  computed:{
      validCustomer(){
        return (this.inputValues.idField.length > 0
                && this.inputValues.nameField.length > 0
                && this.inputValues.locationField.length > 0
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
      axios.get(`http://localhost:8080/customers`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentCustomer = response.data[id-1]
          })
          .catch(e => {
            this.errors.push(e)
          })
      this.customerSelected = true
      //print("Working")
    }

  }
}
</script>
<style scoped>
</style>