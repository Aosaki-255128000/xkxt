<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#fff"
           :collapse-transition="false"
           :collapse="isCollapsed"
           router
           @select="handleSelect"
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; margin-right: 5px">
      <b style="color: white">选课管理系统</b>
    </div>

    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-s-home"></i>
        <span slot="title">主页</span>
      </template>
    </el-menu-item>

    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/admin">
        <i class="el-icon-user"></i>
        <span slot="title">管理员信息</span>
      </el-menu-item>
      <el-menu-item index="/teacher">
        <i class="el-icon-user"></i>
        <span slot="title">教师信息</span>
      </el-menu-item>
      <el-menu-item index="/student">
        <i class="el-icon-user"></i>
        <span slot="title">学生信息</span>
      </el-menu-item>
    </el-submenu>

    <el-submenu index="3">
      <template slot="title">
        <i class="el-icon-s-promotion"></i>
        <span slot="title">信息管理</span>
      </template>
      <el-menu-item index="/department">
        <i class="el-icon-office-building"></i>
        <span slot="title">学院信息</span>
      </el-menu-item>
      <el-menu-item index="/course">
        <i class="el-icon-school"></i>
        <span slot="title">课程信息</span>
      </el-menu-item>
      <el-menu-item index="/openCourse">
        <i class="el-icon-school"></i>
        <span slot="title">开课信息</span>
      </el-menu-item>
      <el-menu-item index="/courseSelection">
        <i class="el-icon-school"></i>
        <span slot="title">选课信息</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
   isCollapsed: Boolean,
   logoTextShow:  Boolean
  },
  methods: {
    handleSelect(index) {
      console.log(index)
      console.log(this.$router.options.routes)
    }
  }
}
</script>

<style scoped>

</style>