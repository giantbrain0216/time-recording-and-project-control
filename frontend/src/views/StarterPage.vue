<template>
  <div>
    <States/>
    <vs-card class="cardx">
      <div slot="header">
        <h3 class="float-left" >Earnings Summary </h3></div>
      <SalesSummary/> </vs-card>
    <News/>
  </div>
</template>

<script>

import States from './components/dashboard/States.vue';
import News from "@/views/components/NewsCards/News";
import SalesSummary from './components/dashboard/SalesSummary.vue';


export default {
  name: 'StarterPage',
  components: {
    States,
    SalesSummary,
    News,
  }
}
</script>
