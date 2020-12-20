<template>
  <BarChart :employeeID="employeeID"/>
</template>

<script>
import BarChart from './BarChart.vue'
export default {
  name:"EmployeeChart",
  props: {employeeID: Number},
  components: {
    BarChart
  },

}
</script>