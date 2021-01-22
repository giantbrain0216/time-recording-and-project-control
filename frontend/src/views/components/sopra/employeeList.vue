<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="employeeSelected ? 4 : 12" vs-sm="12" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2  class="float-left" style="color: cornflowerblue">Employee List</h2>
            <div class="float-right mb-1">
            <vs-button @click="prompts.activeAddPrompt = true" color="primary" icon="add" type="filled">Add New Employee</vs-button>
            </div>
          </div>
          <div class="table-responsive">
          <table class="table v-middle border">
            <thead>
            <tr class="">
              <th class="border-top-0" style="color: cornflowerblue">ID</th>
              <th class="border-top-0" style="color: cornflowerblue">Name</th>
<!--              <th class="border-top-0" style="color: cornflowerblue">Competences</th>-->
              <th class="border-top-0" style="color: cornflowerblue">Remaining Working Hours Per Week</th>
              <th class="border-top-0" style="color: cornflowerblue">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="employee in employees" :key="employee.employeeID">
              <td>{{employee.employeeID}}</td>
              <td>
                <div class="d-flex align-items-center">
                  <a @click="updateDetailedEmployee(employee.employeeID)">
                    <div class="mr-2">
                      <vs-avatar color="primary" :text="employee.name"/>
                    </div>
                  </a>
                  <div class="">
                    <a @click="updateDetailedEmployee(employee.employeeID)" class="m-b-0" style="cursor:pointer">
                      {{ employee.name }}</a>
                  </div>
                </div>
              </td>
              <!--<td>{{employee.employeeID}}</td>-->
<!--              <td>{{ employee.competences }}</td>-->
              <td>{{ employee.remainingWorkingHoursPerWeek }}</td>
              <td>
                <div>
                  <vs-button style="width:80px" @click="fetchEmployeeUpdateCurrentEmployee(employee.employeeID);prompts.activeDeletePrompt = true"  icon="delete" class="m-1" color="danger" type="filled">
                  Delete</vs-button>
                  <vs-button style="width:80px" @click="updateEditID(employee.employeeID)" icon="edit" class="m-1" color="warning" type="filled">
                Edit  </vs-button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="12" vs-lg="4" vs-xs="12">
        <vs-card v-show="employeeSelected" class="cardx">
          <div slot="header">
            <vs-button class="float-right" radius color="danger" type="gradient" icon="highlight_off"
                       @click="employeeSelected = false"></vs-button>
            <h2 style="text-align: center">
              Details of <strong style="color: red;">{{ currentEmployee.name }} </strong>
            </h2>
          </div>
          <div>
            <p><strong>Name: </strong>{{ currentEmployee.name }}</p>
            <hr>
            <p><strong>Domicile: </strong>{{ currentEmployee.domicile }}</p>
            <hr>
            <p><strong>Competences: </strong>{{ currentEmployee.competences }}</p>
            <hr>
            <p><strong>Working Hours Per Week: </strong>{{ currentEmployee.workingHoursPerWeek }}</p>
            <hr>
            <p><strong>Remaining Working Hours : </strong>{{ currentEmployee.remainingWorkingHoursPerWeek }}</p>
            <hr>
            <EmployeeChart :employeeID="this.currentEmployee.employeeID"/>

          </div>
        </vs-card>
      </vs-col>
      <vs-col v-if="employeeSelected" type="flex" vs-justify="center" vs-align="center" vs-sm="12" vs-lg="4" vs-xs="12">
        <vs-card class="cardx">
          <div slot="header">
            <h2 style="text-align: center">
              Assignments of <strong style="color: red;">{{ currentEmployee.name }} </strong>
            </h2>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
               <!-- <th class="border-top-0">Assignment ID</th> -->
                <!--<th class="border-top-0">ID</th>-->
                <th class="border-top-0">Project</th>
                <th class="border-top-0">Planned Hours</th>
                <th class="border-top-0">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="assignment in assignmentsCurrentEmployee" :key="assignment.id">
               <!-- <td>{{ assignment.id }}</td> -->
                <!--<td>{{employee.employeeID}}</td>-->
                <td>{{ currentProjectName(assignment.projectID) }}</td>
                <td>{{ assignment.plannedWorkingHours }}</td>
                <td>
                  <vs-button icon="delete" @click="currentAssignment=assignment;prompts.activeDeleteAssignmentPrompt=true" class="m-1" color="danger"
                             type="filled">
                  </vs-button>
                </td>
                <!-- <td>
                   <div>
                     <vs-button @click="deletionPrompt(employee.employeeID)" class="m-1" color="danger" type="filled">
                       Delete
                     </vs-button>
                     <vs-button @click="updateEditID(employee.employeeID)" class="m-1" color="primary" type="filled">
                       Edit
                     </vs-button>
                   </div>
                 </td>-->
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>

      <vs-prompt
          title="Add Employee"
          color="primary"
          @cancel='resetAllValues();notify("Closed","Adding was cancelled.","warning")'
          @accept="addEmployee"
          @close='resetAllValues();notify("Closed","Adding was cancelled.","warning")'
          :is-valid="validEmployee"
          :active.sync="prompts.activeAddPrompt"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" class="mb-4" v-model="inputValues.nameField"/>
          <vs-input label-placeholder="Domicile" class="mb-4" v-model="inputValues.domicileField"/>
          <vs-input type="number" label-placeholder="Working Hours Per Week" class="mb-4"
                    v-model="inputValues.workingHoursField"/>

          <autocomplete
              ref="textSearchOfCompetencesAdd"
              :search="filterCompetenceItemsAdd"
              :get-result-value="getCompetenceResultValue"
              @submit="handleCompetenceSubmitAdd"
              placeholder="Search for a competence"
              aria-label="Search for a competence"
              auto-select
          ></autocomplete>
          <div class="mt-3 mb-3">
            <vs-checkbox v-for="competence in inputValues.selectedCompetences" :key="competence.id" class="justify-content-start mt-2" v-model="inputValues.tickBoxesForCompetences[competence.id]">{{competence.name}}</vs-checkbox>
          </div>

          <vs-alert
              :active="!validEmployee"
              color="danger"
              icon="new_releases"
          >
            Fields can't be empty.
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Edit Employee"
          color="warning"
          @cancel='resetAllValues();notify("Edit Closed","Edit was cancelled.","warning")'
          @accept="updateEmployee"
          @close='resetAllValues();notify("Edit Closed","Edit was cancelled.","warning")'
          :is-valid="validEmployeeEdit"
          :active.sync="prompts.activeEditPrompt"
      >
        <div class="con-exemple-prompt">
          <h5>Please Modify Employee Data of <strong class="edit-employeee">{{ editValues.nameField }}</strong></h5>
          <h5>ID of the Employee: <strong class="edit-employeee">{{ currentEmployee.employeeID }}</strong></h5>
          <hr>
          <vs-input label="Domicile" :placeholder="currentEmployee.domicile" class="mb-3"
                    v-model="editValues.domicileField"/>
          <vs-input label="Working Hours Peer Week" type="number" :placeholder="currentEmployee.workingHoursPerWeek"
                    class="mb-3" v-model="editValues.workingHoursField"/>

          <autocomplete
              ref="textSearchOfCompetencesEdit"
              :search="filterCompetenceItemsEdit"
              :get-result-value="getCompetenceResultValue"
              @submit="handleCompetenceSubmitEdit"
              placeholder="Search for a competence"
              aria-label="Search for a competence"
              auto-select
          ></autocomplete>
          <div class="mt-3 mb-3">
            <vs-checkbox v-for="competence in editValues.selectedCompetences" :key="competence.id" class="justify-content-start mt-2" v-model="editValues.tickBoxesForCompetences[competence.id]">{{competence.name}}</vs-checkbox>
          </div>

          <vs-alert
              :active="!validEmployeeEdit"
              color="warning"
              icon="new_releases"
          >
            Fields can't be empty.
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteEmployee"
          :is-valid="true"
          :active.sync="prompts.activeDeletePrompt"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete <br>
          <h6>{{ currentEmployee.name }}</h6>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteAssignment"
          :is-valid="true"
          :active.sync="prompts.activeDeleteAssignmentPrompt"
      >
        <div class="con-exemple-prompt">
          <h6>Are you sure you want to delete the next Assignment <br>
           Employee: {{this.currentEmployee.name}},  Project ID :{{ this.currentAssignment.projectID }}, Planned Working Hours : {{ this.currentAssignment.plannedWorkingHours  }}</h6>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';
import EmployeeChart from "@/views/components/dashboard/EmployeeChart";

export default {
  name: "employeeList",
  components: {EmployeeChart},
  data: () => {
    return {
      competences:[],
      employees: [],
      assignments: [],
      projects:[],
      assignmentsCurrentEmployee: [],
      currentEmployee: {},
      employeeSelected: false,
      currentAssignment: {},
      inputValues: {
        nameField: '',
        domicileField: '',
        workingHoursField: '',
        selectedCompetences: [],
        tickBoxesForCompetences: {}
      },
      editValues: {
        nameField: '',
        domicileField: '',
        competencesField: '',
        workingHoursField: '',
        selectedCompetences: [],
        tickBoxesForCompetences: {}
      },
      prompts:{activeAddPrompt:false,activeDeletePrompt:false,activeEditPrompt:false, activeDeleteAssignmentPrompt:false}
    }
  },

  created() {
    this.fetchEmployees();
    this.fetchAllAssignments();
    this.fetchAllProjects();
    this.fetchAllCompetences();
  },

  computed: {
    /**Checks if the input is valid for the add prompt */
    validEmployee() {
      if(!this.inputValues.workingHoursField){
        return false;
      }else {
        return (this.inputValues.nameField.length > 0 && this.inputValues.nameField.length < 100
            && this.inputValues.domicileField.length > 1 && this.inputValues.domicileField.length < 100
            && this.inputValues.workingHoursField > 0 && this.inputValues.workingHoursField < 41
        )
      }
    },
    /**Checks if the input is valid for the edit prompt */
    validEmployeeEdit() {
      return (this.editValues.nameField.length > 0 && this.editValues.nameField.length < 100
          && this.editValues.domicileField.length > 4 && this.editValues.domicileField.length < 100
      )
    }
  },

  methods: {

    /**Filters items for searchbar of competences on add form*/
    async filterCompetenceItemsAdd(input) {

      if (input.length < 1) { return [] }

      return this.competences.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()) && ! Object.keys(this.inputValues.tickBoxesForCompetences).includes(competence.id.toString()))
      })
    },
    /**Returns name of the competence objects*/
    getCompetenceResultValue(result){
      return result.name
    },
    /**Handle function when competence is selected by searchbar add form*/
    handleCompetenceSubmitAdd(result){
      this.inputValues.selectedCompetences.push(result)
      this.inputValues.tickBoxesForCompetences[result.id] = true
      this.$refs.textSearchOfCompetencesAdd.value = ""

    },
    /**Filters items for searchbar of competences on edit form*/
    async filterCompetenceItemsEdit(input) {

      if (input.length < 1) { return [] }

      return this.competences.filter(competence => {
        // eslint-disable-next-line no-console
        return (competence.name.toLowerCase()
            .startsWith(input.toLowerCase()) && ! Object.keys(this.editValues.tickBoxesForCompetences).includes(competence.id.toString()))
      })
    },
    /**Handle function when competence is selected by searchbar edit form*/
    handleCompetenceSubmitEdit(result){
      this.editValues.selectedCompetences.push(result)
      this.editValues.tickBoxesForCompetences[result.id] = true
      this.$refs.textSearchOfCompetencesEdit.value = ""

    },

    /**Returns name of project with given id*/
    currentProjectName(id){
      for (let i = 0; i < this.projects.length; i++) {
        if (this.projects[i].projectNumber === id)
          return this.projects[i].projectName
      }
    },

    /**
     * Deletes assignment from the DB
     */
    async deleteAssignment() {
      await axios.delete(`http://localhost:8080/assignments/` + this.currentAssignment.id).then(async () => {
        this.fetchAssignmentsByEmployee(this.currentEmployee.employeeID)
        await axios.put('http://localhost:8080/employees', {
          "employeeID": this.currentEmployee.employeeID,
          "name": this.currentEmployee.name,
          "domicile": this.currentEmployee.domicile,
          "competences": this.currentEmployee.competences,//.
          "workingHoursPerWeek": this.currentEmployee.workingHoursPerWeek,
          "remainingWorkingHoursPerWeek": (this.currentEmployee.remainingWorkingHoursPerWeek +
              this.currentAssignment.plannedWorkingHours)
        }).then(() => {
          this.notify("Success","Assignment was successfully deleted.","success")
        }).catch((error) => {
          if (error.response){
            this.notify("Delete Assignment Error",error.message,"danger")
          }
        })


      }).catch((error) => {
        if (error.response){
          this.notify("Delete Error",error.message,"danger")
        }
      })

      await this.fetchEmployees()
      await this.fetchEmployeeUpdateCurrentEmployee(this.currentEmployee.employeeID)
      await this.fetchAllAssignments()

      //this.fetchAssignment(this.currentEmployee.employeeID)

    },

    /**
     * Updated the Edit Prompt with the data of the employee to edit.
     * Sets currentEmployee to the gotten employee
     *
     * @param id of employee to be edited
     * @returns editInputFields with data of the employee with the given id
     */
    async updateEditID(id) {
      await this.fetchEmployeeUpdateCurrentEmployee(id);
      this.editValues.nameField = this.currentEmployee.name
      this.editValues.domicileField = this.currentEmployee.domicile
      this.editValues.workingHoursField = this.currentEmployee.workingHoursPerWeek
      this.prompts.activeEditPrompt = true;

      await axios.get(`http://localhost:8080/competencesByEmployee/${id}`)
          .then(async response => {
            for(var i=0; i<response.data.length;i++){

              await axios.get(`http://localhost:8080/competences/` + response.data[i]).then((competence) => {
                this.editValues.selectedCompetences.push(competence.data)
                this.editValues.tickBoxesForCompetences[competence.data.id] = true
              })

            }
          })
          .catch((error) => {
            if(error.response){
              this.notify("Assignments Employee Competence Database Error", error.message,"danger")
            }
          })


    },

    /**
     * Updated currentEmployee in the DB
     */
    async updateEmployee() {
      await axios.put(`http://localhost:8080/employees/`, {
        'employeeID': this.currentEmployee.employeeID,
        'name': this.editValues.nameField,
        'domicile': this.editValues.domicileField,
        'workingHoursPerWeek': this.editValues.workingHoursField,
        'remainingWorkingHoursPerWeek': parseInt(this.currentEmployee.remainingWorkingHoursPerWeek)
            + parseInt(this.editValues.workingHoursField) - parseInt(this.currentEmployee.workingHoursPerWeek),
      }).then(() => {
        this.notify("Success","Successfully modified employee.","success")
      }).catch((error) => {
        if (error.response)
          this.notify("Editing error",error.message,"danger")
      })

      var currentCompetences = []
      await axios.get(`http://localhost:8080/competencesByEmployee/` + this.currentEmployee.employeeID)
          .then(response => {
            // JSON responses are automatically parsed.
            currentCompetences = response.data
          })
          .catch((error) => {
            if(error.response){
              this.notify("Competences for Employees Database Error", error.message,"danger")
            }
          })

      var arr = Object.keys(this.editValues.tickBoxesForCompetences)


      for(var i=0; i < arr.length;i++){
        if(this.editValues.tickBoxesForCompetences[parseInt(arr[i])]){
          if(!currentCompetences.includes(parseInt(arr[i]))){

            await axios.post(`http://localhost:8080/assignedCompetencesEmployee`, {
              'employeeID': this.currentEmployee.employeeID,
              'competenceID': parseInt(arr[i]),
            }).catch((error) => {
              if (error.response) {
                this.notify("Error", error.message, "danger");
              }
            })

          }
        }
        else {
          if(currentCompetences.includes(parseInt(arr[i]))){
            await axios.get(`http://localhost:8080/assignedCompetencesEmployee`).then(async response => {
              for(var j=0; j<response.data.length;j++){
                if(response.data[j].employeeID == this.currentEmployee.employeeID && response.data[j].competenceID == parseInt( arr[i])){
                  await axios.delete(`http://localhost:8080/assignedCompetencesEmployee/` + response.data[j].id).catch((error) => {
                    if (error.response) {
                      this.notify("Error", error.message, "danger");
                    }
                  })
                }
              }
            })



          }
        }
      }

      await this.fetchEmployeeUpdateCurrentEmployee(this.currentEmployee.employeeID)
      await this.fetchEmployees()
      this.resetAllValues()
    },

    /**
     * Gets assignment of given employee
     *
     * @param employeeID id of employee
     */
    async fetchAssignmentsByEmployee(employeeID) {
      this.assignmentsCurrentEmployee = []
        await axios.get('http://localhost:8080/assignmentsbyemployee/' + employeeID).then(response => {
        this.assignmentsCurrentEmployee = response.data
      }).catch((error) => {
          if (error.response){
            this.notify("Assignments Database Error",error.message,"danger")
          }
        })
    },

    /**
     * Gets employee with given id from the DB
     *
     * @param id of employee to get from DB
     */
    fetchEmployeeUpdateCurrentEmployee: async function (id) {

      var competencesString = ""
      await axios.get(`http://localhost:8080/competencesByEmployee/` + id).then(async response => {
        // JSON responses are automatically parsed.
        for(var i = 0; i<response.data.length;i++){
          await axios.get(`http://localhost:8080/competences/` + response.data[i]).then(response1 => {

            competencesString = competencesString.concat(", " + response1.data.name)
          })
        }

      })

      await axios.get(`http://localhost:8080/employees/${id}`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            this.currentEmployee = response.data
            this.currentEmployee.competences = competencesString.substr(1);
          }).catch((error) => {
            if (error.response){
              this.notify("Employees Database Error",error.message,"danger")
            }
          })
    },

    /**
     * Gets the employees from the DB
     */
    fetchEmployees: async function () {
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
     * Gets the assignments from the DB
     *
     */
    fetchAllAssignments: async function () {
      await axios.get(`http://localhost:8080/assignments`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.assignments = response.data
          })
          .catch((error) => {
            if(error.response){
              this.notify("Assignments Database Error", error.message,"danger")
            }else{
              this.notify("Assignments Database Error", "Connection to Database Error","danger")
            }
          })
    },

    /**
     * Gets all competences from DB
     */
    fetchAllCompetences: async function () {
      await axios.get(`http://localhost:8080/competences/`)
          .then(response => {
            // JSON responses are automatically parsed.
            // eslint-disable-next-line no-console
            console.log(response.data)
            this.competences = response.data
          })
          .catch((error) => {
            if(error.response){
              this.notify("Competences Database Error", error.message,"danger")
            }else{
              this.notify("Employees Database Error", "Connection to Database Error","danger")
            }
          })
    },

    /** Fetches all projects*/
    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/projects`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
          })
          .catch((error) => {
            if(error.response){
              this.notify("Projects Database Error", error.message,"danger")
            }else{
              this.notify("Projects Database Error", "Connection to Database Error","danger")
            }
          })
    },

    /**
     * Adds an employee to the DB
     *
     * @return updated employeeList
     */
    addEmployee: async function () {
      await axios.post('http://localhost:8080/employees', {
        'name': this.inputValues.nameField,
        'domicile': this.inputValues.domicileField,
        'workingHoursPerWeek': this.inputValues.workingHoursField,
        'remainingWorkingHoursPerWeek': this.inputValues.workingHoursField,
      }).then(async (result) => {

        var arr = Object.keys(this.inputValues.tickBoxesForCompetences)

        for(var i=0; i < arr.length;i++){
          if(this.inputValues.tickBoxesForCompetences[arr[i]]){
            // eslint-disable-next-line no-console
            await axios.post(`http://localhost:8080/assignedCompetencesEmployee`, {
              'employeeID': result.data,
              'competenceID': arr[i],
            }).catch((error) => {
              if (error.response) {
                this.notify("Error", error.message, "danger");
              }
            })
          }
        }

          this.notify("Success", "Successfully added Employee.","success")

          }).catch(error => {
            if (error.response) {
              this.notify('Add Employee Error',error.message,"danger")

            }
      })
      await this.fetchEmployees()
      this.resetAllValues()
    },

    /**
     * Required:    Initialized Employee in Employee table in the DB.
     *              Initialized Employee table in DB.
     * Ensures:     DB doesn't contain Employee.
     *              employees doesn't contain Employee.
     *
     * Deletes an Employee from DB and updates projects in the frontend.
     *
     * @returns     projects without deleted employee.
     */
    deleteEmployee: async function () {
      this.activeDeletePrompt = false;
      await axios.delete(`http://localhost:8080/employees/${this.currentEmployee.employeeID}`).then(
         async () => {
           await axios.delete(`http://localhost:8080/timeregistrationsEmployee/${this.currentEmployee.employeeID}`);
           await axios.delete(`http://localhost:8080/assignmentsbyemployee/${this.currentEmployee.employeeID}`);
           await axios.delete(`http://localhost:8080/allAssignedCompetencesEmployee/${this.currentEmployee.employeeID}`);

            this.notify('Success','Deletion was successful.',"success")

          }
      ).catch((error) => {
        if (error.response) {
          this.notify("Delete Error",error.message,"danger")
        }
      })
      await this.fetchEmployees()
    },

    /**
     * Adds assignment to employee with given id
     * @param id of employee
     */
    updateDetailedEmployee(id) {
      this.assignmentsCurrentEmployee = []
      this.fetchEmployeeUpdateCurrentEmployee(id)
      for (var i = 0; i < this.assignments.length; i++) {
        if (this.assignments[i].employeeID === id) {
          this.assignmentsCurrentEmployee.push(this.assignments[i])
        }
      }
      this.employeeSelected = true
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
      this.inputValues.domicileField = ''
      this.inputValues.workingHoursField = ''
      this.inputValues.selectedCompetences = []
      this.inputValues.tickBoxesForCompetences = {}
      this.editValues.nameField = ''
      this.editValues.domicileField = ''
      this.editValues.workingHoursField = ''
      this.editValues.selectedCompetences = []
      this.editValues.tickBoxesForCompetences = {}
      this.currentEmployee = {}

    }
  }
}
</script>

<style scoped>
.edit-employeee {
  color: red;
}
</style>
