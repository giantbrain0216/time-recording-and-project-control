<template>
 <div>
  <States/>
   <div class="mt-3 mt-4"></div>
   <SalesSummary/>
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
