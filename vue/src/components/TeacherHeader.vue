<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">
    <div style="flex : 1; font-size : 20px">
      <span :class = "collapseBtnClass" style="cursor: pointer; margin-right: 200px" @click="onToggleCollapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>

    </div>
    <el-dropdown style="width: 70px; cursor : pointer">
      <span>欢迎</span><i class="el-icon-arrow" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
        <el-dropdown-item>重置</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "TeacherHeader.vue",
  props: {
    collapseBtnClass: String,
    onToggleCollapse: Function
  },
  computed: {
    currentPathName() {
      return this.$store?.state?.currentPathName || '默认路径'; // 使用可选链操作符防止报错
    }
  },
  methods: {
    // 退出登录
    logout() {
      this.$router.push('/login');
      console.log(1);
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data() {
    return {
      //
    }
  }
}
</script>

<style scoped>
</style>