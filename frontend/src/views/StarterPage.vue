<template>
 <div>
  <States/>
   <News/>

</div>  
</template>

<script>

import States from './components/dashboard/States.vue';
import News from "@/views/components/NewsCards/News";


export default {
	name: 'StarterPage',
  components: {
    States,
    News
  }
}
</script>
