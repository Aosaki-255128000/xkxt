<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学生ID" suffix-icon="el-icon-search" class="ml-5" v-model="studentId"></el-input>
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师工号" suffix-icon="el-icon-search" class="ml-5" v-model="jobNumber"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class = "el-icon-circle-plus-outline"/></el-button>
      <el-button type="danger" @click="">批量删除<i class = "el-icon-remove-outline"/></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="studentId" label="学生ID" width="120"></el-table-column>
      <el-table-column prop="semester" label="学期" width="140"></el-table-column>
      <el-table-column prop="courseId" label="课号" width="120"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号" width="120"></el-table-column>
      <el-table-column prop="usualPerformance" label="平时成绩"></el-table-column>
      <el-table-column prop="testScore" label="考试成绩"></el-table-column>
      <el-table-column prop="totalScore" label="总评成绩"></el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template v-slot:default="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
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

    <el-dialog title="选课表信息" :visible.sync="dialogFormVisible">
      <el-form label-width="60px" size="small">
        <el-form-item label="学生ID">
          <el-input v-model="form.studentId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="form.semester" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课号">
          <el-input v-model="form.courseId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="老师">
          <el-input v-model="form.jobNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="平时成绩">
          <el-input v-model="form.usualPerformance" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试成绩">
          <el-input v-model="form.testScore" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总评成绩">
          <el-input v-model="form.totalScore" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "CourseSelection.vue",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      studentId: "",
      semester: "",
      courseId: "",
      jobNumber: "",
      usualPerformance: null,
      testScore: null,
      totalScore: null,
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
      this.request.get("/courseSelection/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          studentId: this.studentId,
          semester: this.semester,
          courseId: this.courseId,
          jobNumber: this.jobNumber,
          usualPerformance: this.usualPerformance,
          testScore: this.testScore,
          totalScore: this.totalScore
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      }).catch(
          err => {
            console.log("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    save() {
      this.request.post("/courseSelection", this.form).then(res => {
        if(res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
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

    del(id) {
      this.request.delete(`/openCourse/${id}`).then(res => {
        if(res) {
          this.$message.success("删除成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.studentId = ""
      this.semester = ""
      this.courseId = ""
      this.jobNumber = ""
      this.usualPerformance = null
      this.testScore = null
      this.totalScore = null
      this.load()
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