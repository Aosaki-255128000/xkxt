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

    <!--  选课表容器  -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="semester" label="学期"></el-table-column>
      <el-table-column prop="courseId" label="课号"></el-table-column>
      <el-table-column prop="course.name" label="课程名称"></el-table-column>
      <el-table-column prop="course.credit" label="学分"></el-table-column>
      <el-table-column prop="course.creditHour" label="学时"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号"></el-table-column>
      <el-table-column prop="teacher.name" label="教师姓名"></el-table-column>
      <el-table-column prop="classTime" label="开课时间"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template v-slot:default="scope">
          <el-button type="success" @click="enrollCourse(scope.row)">选课 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="取消选课？"
              :hide-after="0"
              @confirm="withdrawCourse(scope.row)"
          >
            <el-button type="danger" slot="reference">退课 <i class="el-icon-remove-outline"></i></el-button>
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
          layout="sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>

    <div style="margin-bottom: 20px">
      <el-select v-model="currentSemester" @change="handleSemesterChange">
        <el-option label="2025-第一学期" value="202501"></el-option>
        <el-option label="2025-第二学期" value="202502"></el-option>
      </el-select>
    </div>

    <!-- 总学分显示 -->
    <el-row style="margin-bottom: 20px">
      <el-col :span="24">
        <span>当前学期选课总学分: <strong>{{ totalCredits }}</strong></span>
      </el-col>
    </el-row>

    <!-- 课程表容器 -->
    <template>
      <el-table
          :data="gridData"
          border
          style="width: 100%;"
          :span-method="spanMethod"
          :cell-style="cellStyle"
      >
        <!-- 时间列 -->
        <el-table-column prop="time" label="时间" width="120" align="center" />

        <!-- 星期列 -->
        <el-table-column
            v-for="(day, index) in ['周一','周二','周三','周四','周五']"
            :key="index"
            :prop="`day${index + 1}`"
            :label="day"
            align="center"
        >
          <template v-slot="{ row, column }">
            <div class="course-content">
              <div class="course-name">{{ row[column.property]?.courseName }}</div>
              <div class="teacher-name">@{{row[column.property]?.teacherName }}</div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </template>


  </div>
</template>

<script>
export default {
  name: "SelectCourse.vue",
  data() {
      return {
        tableData: [],
        total: 0,
        pageNum: 1,
        pageSize: 10,
        totalCredits: 0,
        currentSemester: "202501",
        semester: "",
        courseId: "",
        jobNumber: "",
        teacherName: "",
        classTime: "",
        form: "",
        dialogFormVisible: false,
        multipleSelection: [],
        timetableData:[],
        gridData: [],
        headerBg: 'headerBg',
      }
  },
  created() {
    this.load();
    this.loadTimetable();
    this.getTotalCredits();
  },
  methods: {
    load() {
      this.request.get("/openCourse/studentPage", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          semester: this.semester,
          courseId: this.courseId,
          jobNumber: this.jobNumber,
          teacherName: this.teacherName,
          classTime: this.classTime
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      }).catch(
          err => {
            console.error("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    loadTimetable() {
      this.request.get("/courseSelection/timetable", {
        params: {
          semester: this.currentSemester
        }
      }).then(res => {
        console.log("后端返回的数据：", res);

        // 检查返回数据格式
        if (res.code !== 200 || !Array.isArray(res.data.data)) {
          this.$message.error("数据格式错误，请联系管理员！");
          return;
        }
        this.processTimetableData(res.data.data);
      }).catch(
          err => {
            console.error("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    // 获取总学分的方法
    getTotalCredits() {
      this.request.get("/courseSelection/totalCredits", {
        params: {
          semester: this.currentSemester
        }
      }).then(res => {
        console.log("后端返回的学分：", res);
        if (res.code === 200) {
          // 更新总学分
          this.totalCredits = res.data.data;
        } else {
          this.$message.error("获取总学分失败，请联系管理员");
        }
      }).catch(err => {
        console.error("请求失败", err);
        this.$message.error("获取总学分失败，请稍后再试");
      });
    },

    processTimetableData(rawData) {
      // 创建12节课的时间槽位（初始化所有单元格为空）
      const timeSlots = Array.from({length: 12}, (_, i) => ({
        time: `${i + 1} ${this.getTimeRange(i + 1)}`,
        day1: null, day2: null, day3: null, day4: null, day5: null
      }));

      // 颜色生成器（按课程ID生成固定颜色）
      const colorMap = new Map();
      const hueBase = Math.random() * 360;

      rawData.forEach(selection => {
        if (!selection.classTime || typeof selection.classTime !== 'string') return;

        // 使用更健壮的正则解析 "星期三5-8" 或 "星期三1-4节"
        const match = selection.classTime.match(/星期([一二三四五])(\d+)-(\d+)/);
        if (!match) {
          console.error('classTime 格式错误:', selection.classTime);
          return;
        }

        // 解析星期和节次
        const weekdayStr = `星期${match[1]}`; // 如 "星期三"
        const startSlot = parseInt(match[2], 10);
        const endSlot = parseInt(match[3], 10);

        // 映射星期到列（星期一=1，星期三=3...）
        const weekdayMap = {
          "星期一": 1, "星期二": 2, "星期三": 3, "星期四": 4, "星期五": 5
        };
        const weekday = weekdayMap[weekdayStr];
        if (!weekday) return;

        // 生成课程颜色
        if (!colorMap.has(selection.courseId)) {
          colorMap.set(selection.courseId,
              `hsl(${(hueBase + Math.random() * 50) % 360}, 70%, 60%)`);
        }

        // 只在第一个时间段填充数据
        const firstSlotIndex = startSlot - 1;
        timeSlots[firstSlotIndex][`day${weekday}`] = {
          courseName: selection.courseName,
          teacherName: selection.teacherName,
          color: colorMap.get(selection.courseId),
          span: endSlot - startSlot + 1 // 合并行数
        };
      });

      this.gridData = timeSlots;
    },

    spanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 0) return; // 时间列不合并

      const prop = column.property;
      const course = row[prop];

      if (course && course.span) {
        // 仅第一个单元格需要合并
        if (rowIndex === this.gridData.findIndex(r => r[prop] === course)) {
          return {rowspan: course.span, colspan: 1};
        } else {
          return {rowspan: 0, colspan: 0}; // 隐藏被合并的单元格
        }
      }
      return {rowspan: 1, colspan: 1};
    },

    getTimeRange(slot) {
      const timeMap = {
        1: '8:00-8:45',
        2: '8:55-9:40',
        3: '10:00-10:45',
        4: '10:55-11:40',
        5: '13:00-13:45',
        6: '13:55-14:40',
        7: '15:00-15:45',
        8: '15:55-16:40',
        9: '18:00-18:45',
        10: '18:55-19:40',
        11: '20:00-20:45',
        12: '20:55-21:40'
      };
      return timeMap[slot];
    },

    // 选课方法
    async enrollCourse(course) {
      try {
        console.log('Course Data:', course); // 打印course对象内容
        const token = localStorage.getItem('token');

        if(!token) {
          this.$message.error('未找到token， 请先登录');
          return;
        }

        console.log('Request Payload: ', {
          semester: course.semester,
          courseId: course.courseId,
          jobNumber: course.jobNumber,
        })

        const res = await this.$axios.post('/courseSelection/enroll',
            {
              semester: course.semester,
              courseId: course.courseId,
              jobNumber: course.jobNumber
              },
            {
              headers: {
                'token': token, // 传递 token
              }
            }
        );
        if (res.data.code === 200) {
          this.$message.success('选课成功');
          this.getTotalCredits();
          this.loadTimetable(); // 刷新列表
        } else {
          this.$message.error(res.data.message);
        }
      } catch (error) {
        this.$message.error('选课失败');
      }
    },

    // 退课方法
    async withdrawCourse(course) {
      try {
        const token = localStorage.getItem('token');

        if (!token) {
          this.$message.error('未找到token， 请先登录');
          return;
        }

        console.log('Request Payload: ', {
          semester: course.semester,
          courseId: course.courseId,
          jobNumber: course.jobNumber,
        });

        const res = await this.$axios.delete('/courseSelection/withdraw',
            {
              data: {
                semester: course.semester,
                courseId: course.courseId,
                jobNumber: course.jobNumber,
              },
              headers: {
                'token': token, // 传递 token
              },
            }
        );

        if (res.data.code === 200) {
          this.$message.success('退课成功');
          this.getTotalCredits();
          this.loadTimetable(); // 刷新列表
        } else {
          this.$message.error(res.data.message);
        }
      } catch (error) {
        this.$message.error('退课失败');
      }
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

    handleSemesterChange(newSemester) {
      this.currentSemester = newSemester;
      this.getTotalCredits(); // 每次切换学期时，重新获取总学分
      this.loadTimetable(); // 重新加载选课数据
    },

    reset() {
      this.semester = ""
      this.courseId = ""
      this.jobNumber = ""
      this.classTime = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },

    cellStyle({row, column}) {
      if (row[column.property]) {
        return {
          backgroundColor: row[column.property].color,
        };
      }
      return {};
    },
  }
}
</script>

<style scoped>
.course-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  font-weight: bold;
}
.course-name {
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.teacher-name {
  font-size: 16px;
  color: white;
}
</style>