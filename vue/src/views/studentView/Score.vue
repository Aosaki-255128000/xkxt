<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师工号" suffix-icon="el-icon-search" class="ml-5" v-model="jobNumber"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <!-- 总绩点显示 -->
    <el-row style="margin-bottom: 20px">
      <el-col :span="24">
        <span>总绩点: <strong>{{ totalGpa }}</strong></span>
      </el-col>
    </el-row>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="semester" label="学期" width="140"></el-table-column>
      <el-table-column prop="courseId" label="课号" width="120"></el-table-column>
      <el-table-column prop="courseName" label="课程名称" width="120"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号" width="120"></el-table-column>
      <el-table-column prop="usualPerformance" label="平时成绩"></el-table-column>
      <el-table-column prop="testScore" label="考试成绩"></el-table-column>
      <el-table-column prop="totalScore" label="总评成绩"></el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "Score",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      jobNumber: "",
      semester: "",
      courseId: "",
      courseName: "",
      usualPerformance: null,
      testScore: null,
      totalScore: null,
      totalGpa: 0,
      form: "",
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg: 'headerBg',
    }
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request.get("/courseSelection/studentScore", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          jobNumber: this.jobNumber,
          teacherName: this.teacherName,
          semester: this.semester,
          courseId: this.courseId,
          courseName: this.courseName,
          usualPerformance: this.usualPerformance,
          testScore: this.testScore,
          totalScore: this.totalScore
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total

        // 计算总绩点
        let totalGpa = 0;
        let totalCourses = 0;

        this.tableData.forEach(item => {
          if (item.totalScore == null || item.totalScore === '') {
            return;
          }
          totalGpa += item.gpa;
          totalCourses++;
        });

        if(totalCourses > 0) {
          this.totalGpa = (totalGpa / totalCourses).toFixed(2);
        } else {
          this.totalGpa = 0;
        }
      }).catch(
          err => {
            console.log("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },

    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.courseId = ""
      this.semester = ""
      this.jobNumber = ""
    },

    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },

    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<style scoped>
.headerBg {
  background-color: aliceblue !important;
}
</style>