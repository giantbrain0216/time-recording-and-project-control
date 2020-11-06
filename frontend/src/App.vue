<template>
  <div id="app">
    <!-- this navigation will appear on every route -->

    <b-navbar toggleable="lg" type="dark" variant="primary">
      <b-navbar-brand to="/">
        <img alt="Vue logo" src="src/assets/logo.png" class="logo" />
        Home
      </b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/about">About</b-nav-item>
          <b-nav-item to="/cats">Cats</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown text="Dropdown Test" right>
            <b-dropdown-item href="#">Option 1</b-dropdown-item>
            <b-dropdown-item href="#">Option 2</b-dropdown-item>
            <b-dropdown-item href="#">Option 3</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>

    <!-- the different view templates will appear in this HTML element based on the route -->
    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}

.logo {
  height: 35px;
  margin: 0 3px;
}

.main {
  padding: 10px 20px;
}
</style>
