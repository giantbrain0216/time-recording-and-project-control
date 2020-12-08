<template>
  <div class="table-responsive">
<!--    <table class="table v-middle border">
      <thead>
      <tr class="">
        <th class="border-top-0">Name</th>
        <th class="border-top-0">ID</th>
        <th class="border-top-0">Location</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="customer in customers" :key="customer.id">
        <td>
          <div class="d-flex align-items-center">
            <div class="mr-2"><vs-avatar color="primary" :text="customer.name" /></div>
            <div class="">
              <h5 class="m-b-0">{{ customer.name }}</h5>
            </div>
          </div>
        </td>
        <td>{{customer.id}}</td>
        <td>{{customer.location }}</td>
      </tr>
      </tbody>
    </table>-->

    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="customerSelected ? 8 : 12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
        <table class="table v-middle border">
          <thead>
          <tr class="">
            <th class="border-top-0">Name</th>
            <th class="border-top-0">ID</th>
            <th class="border-top-0">Location</th>
            <!--<th class="border-top-0">City</th>-->
            <!--<th class="border-top-0">Tickets</th>
            <th class="border-top-0">Sales</th>
            <th class="border-top-0">Earnings</th>-->
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

          </tr>
          </tbody>
        </table>
        </vs-card>
      </vs-col>
      <vs-col v-if="customerSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="6" vs-lg="4" vs-xs="12">
        <vs-card v-show="customerSelected" class="cardx">
          <div slot="header">
            <h4>
              Details
            </h4>
          </div>
          <div>
            <h3>{{"Name:" + currentCustomer.name}}</h3>
            <h3>ID: </h3>
            <h3>Location: </h3>

          </div>
        </vs-card>

      </vs-col>
    </vs-row>
    <vs-button class="mb-2" radius color="danger" type="gradient" icon="person_add"></vs-button>

  </div>


</template>

<script>
import axios from 'axios';

export default {
  name: "customerList",
  data: () => {
    return {
      customers: [],
      currentCustomer:{},
      customerSelected:false
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
  methods: {
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