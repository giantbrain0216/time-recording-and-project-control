<template>
  <BarChart :clientID='clientID'/>
</template>

<script>
import BarChart from './BarChart.vue'
export default {
  name:"ClientChart",
  props: {clientID: Number},
  components: {
    BarChart
  }
}
</script>