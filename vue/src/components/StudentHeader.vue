<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">
    <div style="flex : 1; font-size : 20px">
      <span :class="collapseBtnClass" style="cursor: pointer; margin-right: 200px" @click="onToggleCollapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>

    </div>
    <el-dropdown style="width: 70px; cursor : pointer">
      <span>欢迎</span><i class="el-icon-arrow" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click="logout">退出</el-dropdown-item>
        <el-dropdown-item>重置</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "StudentHeader",
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
      // 清除本地存储中的令牌
      localStorage.removeItem('token');  // 假设你把 token 存储在 localStorage

      // 清除 Vuex 或其他存储中的用户信息
      this.$store.dispatch('logout'); // 如果你使用 Vuex 管理用户状态

      // 重定向到登录页面
      this.$router.push('/login'); // 使用 Vue Router 进行页面跳转
    }
  },
  watch: {
    currentPathName(newVal, oldVal) {
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
