<template>
  <div>
    <h1>Cats</h1>
    <div>
      <b-alert v-bind:show="cats.length === 0" variant="warning">
        No cat available on the server...
      </b-alert>

      <div class="catBox" v-for="cat in cats" v-bind:key="cat.id">
        <h3>{{ cat.name }}</h3>
        <p>Age: {{ cat.ageInYears }} years</p>
        <b-img
          v-bind:alt="cat.name"
          v-bind:src="cat.picUrl"
          class="catLogo"
          rounded="circle"
        />
        <div>
          <b-button
            size="sm"
            variant="outline-danger"
            v-on:click="deleteCat(cat.id)"
          >
            <b-icon-trash></b-icon-trash> Delete
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      cats: [],
      deleteMessage: ""
    };
  },
  methods: {
    // send GET request to API to fetch all cats
    fetchAllCats: function() {
      axios.get(`${config.apiBaseUrl}/cats`).then((response) => {
        // log response data to the browser console
        console.log(response.data);
        // assign response to data variable
        this.cats = response.data;
      });
    },
    deleteCat: function(id) {
      // send DELETE request to API to delete a specific cat by ID
      axios.delete(`${config.apiBaseUrl}/cats/${id}`).then(() => {
        // update view by removing deleted cat
        this.cats = this.cats.filter((cat) => cat.id !== id);
        // show success message using BootstrapVue toast component
        this.showToastMessage(
          `Successfully deleted cat with ID ${id}!`,
          "success"
        );
      });
    },
    showToastMessage(msg, variant) {
      this.$bvToast.toast(msg, {
        title: "Alert!",
        variant: variant,
        solid: true,
        toaster: "b-toaster-top-center",
        autoHideDelay: 4000,
        appendToast: true
      });
    }
  },
  // executed after the component has been started
  created: function() {
    this.fetchAllCats();
  }
};
</script>

<style>
.catBox {
  padding: 10px;
  margin: 3px;
  border: 1px solid #42b983;
  float: left;
  min-width: 250px;
  text-align: center;
}

.catLogo {
  height: 200px;
}

.catBox button {
  margin: 5px;
}
</style>
