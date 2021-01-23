<template>
	<header class="gridx">
		<vs-navbar
      v-model="indexActive"
      :color="topbarColor"
      class="topnavbar"
      style="z-index: 1000"
    >

      <!---
      Template logo
      --> 
      <div slot="title" class="themelogo">
        <img :src="logo" v-if="logo" alt="Dashboard"/>
        <span class="logo-text pr-2 pl-2 ml-1 mr-1" v-if="title">{{ title }}</span>
      </div>
      <!---
      Mobile toggle
      -->
      <vs-input style="color:black" icon="search" placeholder="Search" v-model="search" @keyup.enter='$router.push({name:"Search Results", params: {searchKey: search}});search=""'/>

      <div slot="title">
        <div class="hiddenDesktop cursor-pointer" @click.stop="activeSidebar"><vs-icon icon="menu"></vs-icon></div>
      </div>









      <vs-spacer></vs-spacer>
       

    </vs-navbar>
     
	</header>

</template>

<script>
export default {
	name : 'Navbar',
  props: {
      topbarColor: {
          type: String,
          default: "#2962ff",
      },
      title: {
            type: String,
      },
      logo: {
          type: String
      },
  },
  data:()=>({
    indexActive: 0,
    showToggle: false,
    search: ""
    
  }),

  methods: {
      //This is for sidebar trigger in mobile
      activeSidebar() {
          this.$store.commit('IS_SIDEBAR_ACTIVE', true);
      }
      
  }
}
</script>
