<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学生ID" suffix-icon="el-icon-search" class="ml-5" v-model="studentId"></el-input>
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class = "el-icon-circle-plus-outline"/></el-button>
      <el-button type="danger" @click="">批量删除<i class = "el-icon-remove-outline"/></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="studentId" label="学生ID" width="120"></el-table-column>
      <el-table-column prop="studentName" label="学生姓名" width="120"></el-table-column>
      <el-table-column prop="semester" label="学期" width="140"></el-table-column>
      <el-table-column prop="courseId" label="课号" width="120"></el-table-column>
      <el-table-column prop="courseName" label="课程名称" width="120"></el-table-column>
      <el-table-column prop="usualPerformance" label="平时成绩"></el-table-column>
      <el-table-column prop="testScore" label="考试成绩"></el-table-column>
      <el-table-column prop="totalScore" label="总评成绩"></el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template v-slot:default="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="成绩填入" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="平时成绩">
          <el-input
              v-model.number="form.usualPerformance"
              autocomplete="off"
              style="width: 200px"
              @input="validateScore('usualPerformance')"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试成绩">
          <el-input
              v-model.number="form.testScore"
              autocomplete="off"
              style="width: 200px"
              @input="validateScore('testScore')"
          ></el-input>
        </el-form-item>
        <el-form-item label="总评成绩">
          <el-input
              v-model.number="form.totalScore"
              autocomplete="off"
              style="width: 200px"
              @input="validateScore('totalScore')"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 成绩分布图区域 -->
    <div v-if="chartVisible" style="margin-top: 20px;">
      <el-row :gutter="20">
        <!-- 遍历分组后的课程数据 -->
        <el-col :span="8" v-for="course in groupedCourses" :key="course.courseId">
          <div :id="'chart-' + course.courseId" style="width: 400px; height: 400px;"></div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "StudentScore",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      studentId: "",
      semester: "",
      courseId: "",
      courseName: "",
      usualPerformance: null,
      testScore: null,
      totalScore: null,
      form: "",
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg: 'headerBg',
      chartVisible: false,
    }
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request.get("/courseSelection/studentResult", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          studentId: this.studentId,
          studentName: this.studentName,
          semester: this.semester,
          courseId: this.courseId,
          courseName: this.courseName,
          usualPerformance: this.usualPerformance,
          testScore: this.testScore,
          totalScore: this.totalScore,
          chartVisible: false,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
        this.chartVisible = true;
        // 使用 nextTick 确保 DOM 渲染后再初始化图表
        this.$nextTick(() => {
          // 绘制每门课程的成绩分布图
          this.drawScoreCharts(res.data);
        });
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

    validateScore(field) {
      let value = this.form[field];
      if (value < 0) {
        this.$message.warning("成绩不能小于0");
        this.form[field] = 0; // 自动修正为0
      } else if (value > 100) {
        this.$message.warning("成绩不能大于100");
        this.form[field] = 100; // 自动修正为100
      }
    },

// 绘制成绩分布图
    drawScoreCharts() {
      this.groupedCourses.forEach(course => {
        const chartDom = document.getElementById(`chart-${course.courseId}`);
        if (!chartDom) return;

        const chart = echarts.init(chartDom);

        // 定义分数段（可调整区间）
        const scoreRanges = [
          { min: 0, max: 59, label: '0-59' },
          { min: 60, max: 69, label: '60-69' },
          { min: 70, max: 79, label: '70-79' },
          { min: 80, max: 89, label: '80-89' },
          { min: 90, max: 100, label: '90-100' }
        ];

        // 统计各分数段人数，过滤掉null或undefined成绩
        const countStudents = (scores) => {
          return scoreRanges.map(range => {
            return scores.filter(s => s != null && s >= range.min && s <= range.max).length;
          });
        };

        // 获取三类成绩数据
        const usual = course.students.map(s => s.usualPerformance);
        const test = course.students.map(s => s.testScore);
        const total = course.students.map(s => s.totalScore);

        // ECharts配置
        const option = {
          title: {
            text: `${course.courseName} 成绩分布`,
            left: 'center'
          },
          tooltip: { trigger: 'axis' },
          legend: {
            data: ['平时成绩', '考试成绩', '总评成绩'],
            bottom: 0
          },
          xAxis: {
            type: 'category',
            data: scoreRanges.map(r => r.label),
            name: '分数区间'
          },
          yAxis: {
            type: 'value',
            name: '学生人数'
          },
          series: [
            {
              name: '平时成绩',
              type: 'line',
              smooth: true,
              data: countStudents(usual)
            },
            {
              name: '考试成绩',
              type: 'line',
              smooth: true,
              data: countStudents(test)
            },
            {
              name: '总评成绩',
              type: 'line',
              smooth: true,
              data: countStudents(total)
            }
          ]
        };

        chart.setOption(option);

        // 窗口resize时自适应
        window.addEventListener('resize', () => chart.resize());

      });
    },

  },
  computed: {
    // 按课程ID分组计算属性
    groupedCourses() {
      const groups = {};
      this.tableData.forEach(item => {
        const key = item.courseId;
        if (!groups[key]) {
          groups[key] = {
            courseId: key,
            courseName: item.courseName,
            students: [] // 存储该课程所有学生成绩
          };
        }
        groups[key].students.push(item);
      });
      return Object.values(groups);
    }
  }
}
</script>

<style scoped>
.headerBg {
  background-color: aliceblue !important;
}
</style>