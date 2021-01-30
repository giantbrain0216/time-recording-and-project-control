<template lang="html">
  <vs-card class="cardx">
    <div slot="header">
      <h4 style="color:#007bff;">
        Event Log </h4>
    </div>

    <div class="">
      <vs-tabs>
        <vs-tab label="Employees">
          <div class="con-tab-ejemplo">
             <vs-list v-show="eventLogEmployees.length!==0">
              <vs-list-item class="mb-1" v-for="eventLog in eventLogEmployees" :key="eventLog.index "
              :title="eventLog" ></vs-list-item>
              </vs-list>
            <h6 v-show="eventLogEmployees.length===0" style="color:red;">There are no event logs to display</h6>
          </div>
        </vs-tab>
        <vs-tab label="Projects">
          <div class="con-tab-ejemplo">
            <vs-list v-show="eventLogProjects.length!==0" >
              <vs-list-item class="mb-1" v-for="eventLog in eventLogProjects" :key="eventLog.index "
                            :title="eventLog" ></vs-list-item>
            </vs-list>
            <h6 v-show="eventLogProjects.length===0" style="color:red;">There are no event logs to display</h6>
          </div>
        </vs-tab>
         <vs-tab label="Clients">
           <div class="con-tab-ejemplo">
             <vs-list v-show="eventLogClients.length!==0">
               <vs-list-item class="mb-1" v-for="eventLog in eventLogClients" :key="eventLog.index "
                             :title="eventLog" ></vs-list-item>
             </vs-list>
             <h6 v-show="eventLogClients.length===0" style="color:red;">There are no event logs to display</h6>
           </div>
         </vs-tab>
      </vs-tabs>
    </div>
  </vs-card>

</template>
<script>
export default {
  name: "EventLog",
  data: () => {
    return {
      eventLogEmployees: [],
      eventLogClients: [],
      eventLogProjects: [],

    }
  },
  mounted() {
    if (localStorage.getItem('eventLogEmployee')) this.eventLogEmployees = JSON.parse(localStorage.getItem('eventLogEmployee'));
    if (localStorage.getItem('eventLogClient')) this.eventLogClients = JSON.parse(localStorage.getItem('eventLogClient'));
    if (localStorage.getItem('eventLogProject')) this.eventLogProjects = JSON.parse(localStorage.getItem('eventLogProject'));
  },
}
</script>