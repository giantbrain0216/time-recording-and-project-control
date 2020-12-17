import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
	routes: [
    {
    // ======================
    // Full Layout
    // ======================
			path: '',
            component: () => import('./layout/full/MainContainer.vue'),
            // ======================
            // Theme routes / pages
            // ======================

            children: [
                {
                    path: '/',
                    redirect: '/starterkit'
                },

                {
                    path: '/starterkit',
                    name: 'Starterkit',
                    index: 1,
                    component: () => import('./views/StarterPage.vue')
                },{
                    path: '/clientList',
                    name: 'Client List',
                    index: 2,
                    component: () => import('./views/components/sopra/clientList.vue')
                },

                {
                    path:'/projectList',
                    name: 'Project list',
                    index: 3,
                    component: () => import('./views/components/sopra/projectList')
                },

                {
                    path: '/employeeList',
                    name: 'Employee list',
                    index: 4,
                    component: () => import('./views/components/sopra/employeeList')
                },
                {
                    path: '/timeregistration',
                    name: 'Time Registration',
                    index: 5,
                    component: () => import('./views/components/sopra/timeRegistration')
                },

            ]
		},
    // Redirect to starterkit page, if no match found
        {
            path: '*',
            redirect: '/starterkit'
        }
	]
})