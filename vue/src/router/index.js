import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login', // 启动项目时默认跳转到登录页面
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue'),
  },
  {
    path: '/manage',
    component: () => import('../views/Manage.vue'),
    redirect: '/home', // 管理界面默认重定向到首页
    children: [
      { path: '/home', name: 'Home', component: () => import('../views/manage/Home.vue') },
      { path: '/user', name: 'User', component: () => import('../views/manage/User.vue') },
      { path: '/password', name: 'Password', component: () => import('../views/manage/Password.vue') },
      { path: '/teacher', name: 'Teacher', component: () => import('../views/manage/Teacher.vue') },
      { path: '/student', name: 'Student', component: () => import('../views/manage/Student.vue') },
      { path: '/admin', name: 'Admin', component: () => import('../views/manage/Admin.vue') },
      { path: '/department', name: 'Department', component: () => import('../views/manage/Department.vue') },
      { path: '/course', name: 'Course', component: () => import('../views/manage/Course.vue') },
      { path: '/openCourse', name: 'OpenCourse', component: () => import('../views/manage/OpenCourse.vue') },
      { path: '/courseSelection', name: 'CourseSelection', component: () => import('../views/manage/CourseSelection.vue') },
    ],
  },
  {
    path: '/studentView',
    name: 'StudentView',
    component: () => import('../views/StudentView.vue'),
    redirect: '/studentHome',
    children: [
      { path: '/studentHome', name: 'Home', component: () => import('../views/studentView/StudentHome.vue')},
      { path: '/studentScore', name: 'Score', component: () => import('../views/studentView/Score.vue')},
      { path: '/studentSelectCourse', name: 'Select', component: () => import('../views/studentView/SelectCourse.vue')},
    ]
  },
  {
    path: '/teacherView',
    component: () => import('../views/TeacherView.vue'),
    redirect: '/teacherHome',
    children: [
      { path: '/teacherHome', name: 'Home', component: () => import('../views/teacherview/TeacherHome.vue') },
      { path: '/teacherDepartment', name: 'Department', component: () => import('../views/teacherview/TeacherDepartment.vue') },
      { path: '/teacherCourse', name: 'OpenCourse', component: () => import('../views/teacherview/TeacherOpenCourse.vue') },
      { path: '/teacherScore', name: 'Score', component: () => import('../views/teacherview/StudentScore.vue') },
    ],
  },
  {
     path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue'),
  },
];


const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
