<template>
  <div>
  <vs-row v-if="finishedFetching">
    <vs-col vs-lg="3" vs-sm="6" vs-xs="12">
      <vs-card actionable >
        <div slot="header">
          <h3>
            World Article
          </h3>
        </div>
        <div slot="media">
          <img :src="worldArticle.img">
        </div>
        <div class="mb-2">
          <h6 class="mb-2">{{worldArticle.title}}</h6>
          {{worldArticle.abstract}}</div>
        <vs-button :href="worldArticle.link" color="warning" type="filled"  target="_blank">Read More</vs-button>
      </vs-card>
    </vs-col>
    <vs-col vs-lg="3" vs-sm="6" vs-xs="12">
      <vs-card actionable >
        <div slot="header">
          <h3>
            Business Article
          </h3>
        </div>
        <div slot="media">
          <img :src="businessArticle.img">
        </div>
        <div class="mb-2">
          <h6 class="mb-2">{{businessArticle.title}}</h6>
          {{businessArticle.abstract}}</div>
        <vs-button :href="businessArticle.link" color="danger" type="filled" target="_blank">Read More</vs-button>
      </vs-card>
    </vs-col>
    <vs-col vs-lg="3" vs-sm="6" vs-xs="12">
      <vs-card actionable >
        <div slot="header">
          <h3>
            Technology Article
          </h3>
        </div>
        <div slot="media">
          <img :src="technologyArticle.img">
        </div>
        <div class="mb-2">
          <h6 class="mb-2">{{technologyArticle.title}}</h6>
          {{technologyArticle.abstract}}</div>
        <vs-button :href="technologyArticle.link"  color="primary" type="filled" target="_blank">Read More</vs-button>
      </vs-card>
    </vs-col>
    <vs-col vs-lg="3" vs-sm="6" vs-xs="12">
      <vs-card actionable >
        <div slot="header">
          <h3>
            Health Article
          </h3>
        </div>
        <div slot="media">
          <img :src="healthArticle.img">
        </div>
        <div class="mb-2">
          <h6 class="mb-2">{{healthArticle.title}}</h6>
          {{healthArticle.abstract}}</div>
        <vs-button :href="healthArticle.link" color="success" type="filled" target="_blank">Read More</vs-button>
      </vs-card>
    </vs-col>
  </vs-row>
  <vs-row>
    <LoadingAnimation v-if="!finishedFetching"/>
  </vs-row>
  </div>
</template>

<script>
import config from "@/config";
import axios from 'axios';
import LoadingAnimation from "@/views/components/NewsCards/LoadingAnimation";

export default {
name: "News",
  components: {LoadingAnimation},
  data: () => {
  return {
    worldArticle : {},
    businessArticle : {},
    technologyArticle : {},
    healthArticle: {},
    finishedFetching: false,
  }
},

 async beforeRouteEnter(){
   await this.fetchWorldArticle()
 },

async created() {
      await this.fetchWorldArticle()
      this.finishedFetching = true
  },

 methods: {
  async fetchWorldArticle(){
    await axios.get(` https://api.nytimes.com/svc/topstories/v2/world.json?api-key=${config.apiKey}`).then((response) => {

      var title = ''
      var abstract = ''
      var img = ''
      var link = ''

      title =response.data.results[0].title;
      abstract = response.data.results[0].abstract;
      img = response.data.results[0].multimedia[3].url;
      link = response.data.results[0].url

      this.$set(this.worldArticle,"title",title)
      this.$set(this.worldArticle,"abstract",abstract)
      this.$set(this.worldArticle,"img",img)
      this.$set(this.worldArticle,"link",link)



    })
    await axios.get(` https://api.nytimes.com/svc/topstories/v2/business.json?api-key=${config.apiKey}`).then((response) => {

      var title = ''
      var abstract = ''
      var img = ''
      var link = ''

      title =response.data.results[0].title;
      abstract = response.data.results[0].abstract;
      img = response.data.results[0].multimedia[3].url;
      link = response.data.results[0].url

      this.$set(this.businessArticle,"title",title)
      this.$set(this.businessArticle,"abstract",abstract)
      this.$set(this.businessArticle,"img",img)
      this.$set(this.businessArticle,"link",link)





    })
    await axios.get(` https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=${config.apiKey}`).then((response) => {

      var title = ''
      var abstract = ''
      var img = ''
      var link = ''

      title =response.data.results[1].title;
      abstract = response.data.results[1].abstract;
      img = response.data.results[1].multimedia[3].url;
      link = response.data.results[1].url

      this.$set(this.technologyArticle,"title",title)
      this.$set(this.technologyArticle,"abstract",abstract)
      this.$set(this.technologyArticle,"img",img)
      this.$set(this.technologyArticle,"link",link)





    })
    await axios.get(` https://api.nytimes.com/svc/topstories/v2/health.json?api-key=${config.apiKey}`).then((response) => {

      var title = ''
      var abstract = ''
      var img = ''
      var link = ''

      title =response.data.results[0].title;
      abstract = response.data.results[0].abstract;
      img = response.data.results[0].multimedia[3].url;
      link = response.data.results[0].url

      this.$set(this.healthArticle,"title",title)
      this.$set(this.healthArticle,"abstract",abstract)
      this.$set(this.healthArticle,"img",img)
      this.$set(this.healthArticle,"link",link)





    })
  }

 }

}



</script>

<style scoped>

</style>