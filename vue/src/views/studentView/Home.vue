<template>
  <div>
    <h1>欢迎您，{{ studentName }}同学！这里是主页</h1>
  </div>
</template>

<script>
import { jwtDecode } from 'jwt-decode';

export default {
  name: "Home",
  data() {
    return {
      studentName: ''
    };
  },
  created() {
    const token = localStorage.getItem('token');

    if(token) {
      try {
        const decoded = jwtDecode(token);
        console.log("Decoded JWT: ", decoded);
        this.studentName = decoded.name || "未知";
      } catch (error) {
        console.error('解析Token失败:', error);
        this.$message.error('登录信息已失效，请重新登录');
        this.$router.push('/login');
      }
    } else {
        this.$router.push('/login');
      }
    }
  }

</script>

<style scoped>
h1 {
  color: #2c3e50;
  margin: 20px 0;
}
</style>