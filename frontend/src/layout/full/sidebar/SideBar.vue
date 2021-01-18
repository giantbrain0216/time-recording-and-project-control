<template lang="html">

   <div id="parentx">
    <vs-sidebar default-index="1" :parent="parent" :hiddenBackground="doNotClose" color="primary" class="sidebarx" spacer v-model="isSidebarActive" :click-not-close="doNotClose" >
        <template v-for="(sidebarLink, index) in sidebarLinks" >
            <vs-sidebar-item :icon="sidebarLink.icon" :to="sidebarLink.url" :key="`sidebarLink-${index}`" :index="index">
              <span class="hide-in-minisidebar">{{ sidebarLink.name }}</span>
            </vs-sidebar-item>
        </template>  
        
        <div class="footer-sidebar" slot="footer">
            <vs-button icon="reply" color="primary" type="flat" href="https://itestra.com/">Go to Itestra Website</vs-button>
            <p style="font-size:9px">Copyright © 2021 Mohamed, Radu <br> V1.1.3</p>
        </div>

    </vs-sidebar>
  </div>


</template>

<script>

export default {
  name: "SideBar",
  props: {
    parent: {
        type: String
    },
    sidebarLinks: {
      type: Array,
      required: true,
    },
    index: {
        default: null,
        type: [String, Number]
    }
  },
  data:()=>({
    doNotClose: false,
    windowWidth: window.innerWidth,
  }),
  computed: {
      //This is for mobile trigger
      isSidebarActive: {
        get() {
          return this.$store.state.isSidebarActive
        },
        set(val) {
          this.$store.commit('IS_SIDEBAR_ACTIVE', val)
        }
      }
   },
   watch: {
      
   },   
   methods : {
      handleWindowResize(event) {
            this.windowWidth = event.currentTarget.innerWidth;
            this.setSidebar();
        },
      setSidebar() {
        if (this.windowWidth < 1170) {
          this.$store.commit('IS_SIDEBAR_ACTIVE', false);
          this.doNotClose= false
        } 
        else {
          this.$store.commit('IS_SIDEBAR_ACTIVE', true);
          this.doNotClose= true 
        }
      }
  },
  mounted() {
      this.$nextTick(() => {
            window.addEventListener('resize', this.handleWindowResize);
        });
      this.setSidebar();
    },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
    this.setSidebar();
  }
}
</script>