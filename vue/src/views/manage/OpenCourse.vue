<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师工号" suffix-icon="el-icon-search" class="ml-5" v-model="jobNumber"></el-input>
      <el-input style="width: 200px" placeholder="请输入开课时间" suffix-icon="el-icon-search" class="ml-5" v-model="classTime"></el-input>
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
      <el-table-column prop="semester" label="学期" width="140"></el-table-column>
      <el-table-column prop="courseId" label="课号" width="120"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号" width="120"></el-table-column>
      <el-table-column prop="classTime" label="开课时间"></el-table-column>
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

    <el-dialog title="开课表信息" :visible.sync="dialogFormVisible">
      <el-form label-width="60px" size="small">
        <el-form-item label="学期">
          <el-select v-model="form.semester" placeholder="请选择学期">
            <el-option label="202501" value="202501" />
            <el-option label="202502" value="202502" />
          </el-select>
        </el-form-item>
        <el-form-item label="课号">
          <el-select v-model="form.courseId" placeholder="请选择课程号">
            <el-option
                v-for="course in courses"
                :key="course.courseId"
                :label="course.name"
            :value="course.courseId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="老师">
          <el-select v-model="form.jobNumber" placeholder="请选择教师工号">
            <el-option
                v-for="teacher in teachers"
                :key="teacher.jobNumber"
                :label="teacher.name"
                :value="teacher.jobNumber"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-row :gutter="10">
              <el-select v-model="form.day" placeholder="选择周几">
                <el-option v-for="day in days" :key="day" :label="day" :value="day" />
              </el-select>
              <el-select v-model="form.timeRange" placeholder="选择时间段">
                <el-option
                    v-for="range in timeRanges"
                    :key="range"
                    :label="range"
                    :value="range"
                />
              </el-select>
          </el-row>
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
  name: "OpenCourse.vue",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      semester: "",
      courseId: "",
      jobNumber: "",
      classTime: "",
      day: '',
      timeRange: '',
      form: "",
      courses: [],
      teachers: [],
      days: ['星期一', '星期二', '星期三', '星期四', '星期五'],
      timeRanges: this.generateTimeRanges(1, 12),
    }
  },
  created() {
    this.load();
    this.loadCourses();
    this.loadTeachers();
  },
  methods: {
    load() {
      this.request.get("/openCourse/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          semester: this.semester,
          courseId: this.courseId,
          jobNumber: this.jobNumber,
          classTime: this.classTime
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      })
          .catch(
              err => {
                console.error("请求失败", err);
                this.$message.error("数据加载失败，请稍后再试");
              })
    },
    loadCourses() {
      this.request
        .get("/course")
        .then((res) => {
          console.log(res);
          this.courses = res;
        })
        .catch((err) => {
          console.error("加载课程失败", err);
          this.$message.error("课程数据加载失败，请稍后再试");
        });
    },
    loadTeachers() {
      this.request
          .get("/teacher")
          .then((res) => {
            console.log(res);
            this.teachers = res;
          })
          .catch((err) => {
            console.error("加载课程失败", err);
            this.$message.error("课程数据加载失败，请稍后再试");
          });
    },
    save() {
      this.form.classTime = `${this.form.day}${this.form.timeRange}`;
      this.request.post("/openCourse", this.form).then(res => {
        if(res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    // 生成时间段范围，比如 1-2, 1-3...
    generateTimeRanges(start, end) {
      const ranges = [];
      for (let i = start; i <= end; i++) {
        for (let j = i + 1; j <= end; j++) {
          ranges.push(`${i}-${j}`);
        }
      }
      return ranges;
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
        if(res.status === 200) {
          this.$message.success("删除成功");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error(res.data || "删除失败");
        }
      }).catch(err => {
        this.$message.error("无法删除该开课，存在关联的选课记录");
      });
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.semester = ""
      this.courseId = ""
      this.jobNumber = ""
      this.classTime = ""
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