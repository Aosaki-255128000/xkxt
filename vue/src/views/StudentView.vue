<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgba(0, 21, 41, 0.5);">
      <Aside :isCollapsed="isCollapsed" :logoTextShow="logoTextShow"/>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; border-bottom: 1px solid #ccc; ">
        <Header
            :collapseBtnClass="collapseBtnClass"
            :onToggleCollapse="collapse"/>
      </el-header>

      <el-main>
        <!--      表示当前页面的子路由会在 router-view 中展示-->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/StudentAside";
import Header from "@/components/StudentHeader";

export default {
  name: 'StudentView.vue',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapsed: false,
      sideWidth: 200,
      logoTextShow: true
    }
  },

  components: {
    Aside,
    Header
  },

  methods: {
    collapse: function () {
      this.isCollapsed = !this.isCollapsed
      if (this.isCollapsed) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
      this.collapseBtnClass = this.collapseBtnClass === 'el-icon-s-fold' ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    }
  }

};
</script>

<style scoped>
/* 可以添加一些简单样式 */
</style>
