<template lang="html">
  <div class="demo-alignment" style="display:flex; width: 100%">
    <div class="fill-row-loading" style="margin: 0 auto; width:100%">
      <div style="width:100%" :key="type" :class="{'activeLoading':activeLoading}" @click="openLoading(type)" v-for="type in types" :id="[`loading-${type}`]" class="vs-con-loading__container loading-example">
      </div>

    </div>
  </div>
</template>
<script>
export default {
  name:"LoadingAnimation",
  data:()=>({
    types:[
      'corners',

    ],
    activeLoading:false,
  }),
  mounted(){
    this.types.forEach((type)=>{
      this.$vs.loading({
        container: `#loading-${type}`,
        type,
        text:"News"
      })
    })
  },
  methods:{
    openLoading(type){
      this.activeLoading = true
      this.$vs.loading({
        type:type,
      })
      setTimeout( ()=> {
        this.activeLoading = false
        this.$vs.loading.close()
      }, 3000);
    },
  }
}
</script>
<style lang="scss">
.fill-row-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  .loading-example {
    width: 120px;
    float: left;
    height: 120px;
    box-shadow: 0px 5px 20px 0px rgba(0, 0, 0, 0.05);
    border-radius: 10px;
    margin: 8px;
    transition: all 0.3s ease;
    cursor: pointer;
    &:hover {
      box-shadow: 0px 0px 0px 0px rgba(0, 0, 0, 0.05);
      transform: translate(0, 4px);
    }
    h4 {
      z-index: 40000;
      position: relative;
      text-align: center;
      padding: 10px;
    }
    &.activeLoading {
      opacity: 0 !important;
      transform: scale(0.5);
    }
  }
}
</style>