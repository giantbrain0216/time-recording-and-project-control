<template>
  <BarChart :employeeID="employeeID"/>
</template>

<script>
import BarChart from './BarChart.vue'
export default {
  name:"ClientChart",
  props: {employeeID: Number},
  components: {
    BarChart
  },

}
</script>