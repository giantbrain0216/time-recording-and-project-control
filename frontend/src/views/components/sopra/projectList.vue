<template>
  <div class="table-responsive">
    <vs-row justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="6" vs-xs="12" code-toggler>
        <vs-card class="cardx">
          <table class="table v-middle border">
            <thead>
              <tr>
                <th class="border-top-0"> - </th>
                <th class="border-top-0"> - </th>
                <th class="border-top-0"> - </th>
                <th class="border-top-0"> - </th>
              </tr>
            </thead>
            <tbody>
            <tr>
            </tr>
              <tr v-for="project in projects" :key="project.id">
                <td>
                  <div class="d-flex align-items-center">
                    <a @click="fetchProject(project.id)">
                      <div class="mr-2">
                        <vs-avatar color="primary" :text="project.name"/>
                      </div>
                    </a>
                    <div class="">
                      <a @click="fetchProject(project.id)" class="m-b-0" style="cursor:pointer">
                        {{ project.name }}
                      </a>
                    </div>

                  </div>
                </td>
                <td>{{project.id}}</td>
                <td>{{project.location }}</td>
                <td>
                  <vs-button color="danger" type="filled" >
                    Delete
                  </vs-button>
                </td>
                <!--till Here-->
                <div>
                  <vs-progress :height="12" :percent="40" color="success">success</vs-progress>
                </div>
              </tr>
            </tbody>
          </table>
        </vs-card>
      </vs-col>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "projectList",
  data: () => {
    return {
      projects: [
        {id: 1, name: 'Itestra', location: 'Tangegartstr. 11'},
        {id: 2, name: 'AEB', location: 'Kein plan wo' },
        {id: 3, name: 'IC Consult', location: 'i-wo :D'}
      ],
      progress_value:'',
      currentProject:{},
      projectSelected:false,
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

    fetchProject: function(id){
      axios.get(`http://localhost:8080/projects`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.currentCustomer = response.data[id-1]
          })
          .catch(e => {
            this.errors.push(e)
          })
      this.projectSelected = true
      //print("Working")
    }

  }
}
</script>
<style scoped>
</style>