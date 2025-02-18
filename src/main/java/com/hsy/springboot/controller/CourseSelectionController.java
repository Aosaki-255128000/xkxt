package com.hsy.springboot.controller;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hsy.springboot.common.Result;
import com.hsy.springboot.entity.Course;
import com.hsy.springboot.entity.CourseSelection;
import com.hsy.springboot.entity.EnrollRequest;
import com.hsy.springboot.entity.OpenCourse;
import com.hsy.springboot.mapper.CourseMapper;
import com.hsy.springboot.mapper.CourseSelectionMapper;
import com.hsy.springboot.mapper.OpenCourseMapper;
import com.hsy.springboot.service.CourseSelectionService;
import com.hsy.springboot.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseSelection")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Autowired
    private CourseSelectionService courseSelectionService;

    @Autowired
    private OpenCourseMapper openCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @PostMapping
    public Integer save(@RequestBody CourseSelection courseSelection) { return courseSelectionService.save(courseSelection); }

    @GetMapping
    public List<CourseSelection> findAll() {
        List<CourseSelection> all = courseSelectionMapper.findAll();
        return all;
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String studentId,
                                        @RequestParam(defaultValue = "") String semester,
                                        @RequestParam(defaultValue = "") String courseId,
                                        @RequestParam(defaultValue = "") String jobNumber,
                                        @RequestParam(required = false) Integer usualPerformance,
                                        @RequestParam(required = false) Integer testScore,
                                        @RequestParam(required = false) Integer totalScore) {
        pageNum = (pageNum - 1) * pageSize;

        semester = "%" + semester + "%";
        courseId = "%" + courseId + "%";
        jobNumber = "%" + jobNumber + "%";

        List<CourseSelection> data = courseSelectionMapper.selectPage(pageNum, pageSize, studentId, semester, courseId, jobNumber, usualPerformance, testScore, totalScore);
        Integer total = courseSelectionMapper.selectTotal(studentId, semester, courseId, jobNumber, usualPerformance, testScore, totalScore);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return courseSelectionMapper.deleteById(id); }

    @GetMapping("/studentResult")
    public Map<String, Object> findStudentResult(
            @RequestHeader(value = "token") String token,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String studentId,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(required = false) Integer usualPerformance,
            @RequestParam(required = false) Integer testScore,
            @RequestParam(required = false) Integer totalScore){

        pageNum = (pageNum - 1) * pageSize;

        // 解析 token 获取教师工号
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String jobNumber = decodedJWT.getClaim("jobNumber").asString();

        // 查询该教师的选课信息
        Map<String, Object> result = new HashMap<>();
        List<CourseSelection> courseSelections = courseSelectionService.findSelectionsByJobNumber(jobNumber, pageNum, pageSize, studentId, semester, courseId, usualPerformance, testScore, totalScore);
        int total = courseSelectionService.countSelectionsByJobNumber(jobNumber, studentId, semester, courseId, usualPerformance, testScore, totalScore);

        result.put("data", courseSelections);
        result.put("total", total);
        return result;
    }

    @GetMapping("/studentScore")
    public Map<String, Object> findStudentScore(
            @RequestHeader(value = "token") String token,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String jobNumber,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(required = false) Integer usualPerformance,
            @RequestParam(required = false) Integer testScore,
            @RequestParam(required = false) Integer totalScore){

        pageNum = (pageNum - 1) * pageSize;

        // 解析 token 获取学号
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String studentId = decodedJWT.getClaim("code").asString();

        // 查询该学生的选课信息
        Map<String, Object> result = new HashMap<>();
        List<CourseSelection> courseSelections = courseSelectionService.findSelectionsByStudentId(studentId, pageNum, pageSize, jobNumber, semester, courseId, usualPerformance, testScore, totalScore);
        int total = courseSelectionService.countSelectionsByStudentId(studentId, jobNumber, semester, courseId, usualPerformance, testScore, totalScore);

        result.put("data", courseSelections);
        result.put("total", total);
        return result;
    }

    @GetMapping("/timetable")
    public Result getTimetable(
            @RequestHeader(value = "token") String token,
            @RequestParam String semester) {

        //接续学生ID
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String studentId = decodedJWT.getClaim("code").asString();

        //查询选课数据
        List<CourseSelection> selections = courseSelectionService.getStudentTimetable(studentId, semester);

        return Result.successWithData(selections);

    }

    @GetMapping("/totalCredits")
    public Result getTotalCredits(
            @RequestHeader(value = "token") String token,
            @RequestParam String semester) {

        // 获取学生ID
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String studentId = decodedJWT.getClaim("code").asString();

        // 查询选课数据（包括课程学分）
        List<CourseSelection> selections = courseSelectionService.getStudentTimetable(studentId, semester);

        // 计算总学分
        int totalCredits = 0;
        for (CourseSelection selection : selections) {
            // 你可以通过关联查询或者直接使用 CourseSelection 中的学分字段来获取学分
            String courseId = selection.getCourseId();
            Course course = courseMapper.getCourseById(courseId); // 你需要在 courseMapper 中增加这个方法
            if (course != null) {
                totalCredits += course.getCredit(); // 假设 Course 类中有 credit 字段
            }
        }
        // 返回总学分
        return Result.successWithData(totalCredits);
    }


    // 选课
    @PostMapping("/enroll")
    public Result enrollCourse(HttpServletRequest request, @RequestBody EnrollRequest enrollRequest) {
        System.out.println("Received EnrollRequest: " + enrollRequest);
        String token = request.getHeader("token");

        if (token == null || token.isEmpty()) {
            return Result.errorPrint("token 丢失");
        }
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String studentId = decodedJWT.getClaim("code").asString();

        // 学期校验
        if (!"202502".equals(enrollRequest.getSemester())) {
            return Result.errorPrint("当前选课未开放，请选择2025学年第2学期的课程！");
        }

        // 获取课程时间
        OpenCourse courseTime = openCourseMapper.getCourseTime(
                enrollRequest.getSemester(),
                enrollRequest.getCourseId(),
                enrollRequest.getJobNumber()
        );

        // 检查课程时间格式是否有效
        if (courseTime == null || courseTime.getClassTime() == null || courseTime.getClassTime().isEmpty()) {
            return Result.errorPrint("课程时间格式不正确");
        }

        // 检查是否已选过该课程
        int count = courseSelectionMapper.countByStudentCourseAndTeacher(studentId, enrollRequest.getCourseId(), enrollRequest.getJobNumber());
        if (count > 0) {
            return Result.errorPrint("无法重复选课！");
        }

        // 冲突检测
        List<CourseSelection> selectedCourses = courseSelectionMapper.selectTimetableByStudent(
                studentId,
                enrollRequest.getSemester()
        );

        // 检查已选课程的时间格式
        for (CourseSelection sc : selectedCourses) {
            if (sc.getClassTime() == null || sc.getClassTime().isEmpty()) {
                return Result.errorPrint("已选课程时间格式不正确");
            }
        }

        if (hasTimeConflict(courseTime.getClassTime(), selectedCourses)) {
            return Result.errorPrint("课程时间冲突");
        }

        // 保存选课
        CourseSelection selection = new CourseSelection();
        selection.setStudentId(studentId);
        selection.setSemester(enrollRequest.getSemester());
        selection.setCourseId(enrollRequest.getCourseId());
        selection.setJobNumber(enrollRequest.getJobNumber());
        courseSelectionMapper.insert(selection);

        return Result.success();
    }


    // 时间冲突检查
    private boolean hasTimeConflict(String newTime, List<CourseSelection> selectedCourses) {

        if (selectedCourses.isEmpty()) {
            return false;  // 如果没有已选课程，说明没有时间冲突
        }

        // 解析新的课程时间
        String[] newParts = newTime.split("星期|\\D+");  // 确保正确解析时间部分
        if (newParts.length < 3) {
            System.out.println("Invalid new time format: " + newTime);  // 打印调试信息
            return false;  // 如果格式不正确，直接返回没有冲突
        }

        // 调试：打印新课程时间的解析结果
        String newDay = newParts[1];
        String[] timeParts = newParts[2].split("-");
        if (timeParts.length < 2) {
            System.out.println("Invalid time range format: " + newParts[2]);
            return false;  // 返回没有时间冲突
        }

        int newStart = Integer.parseInt(timeParts[0]);
        int newEnd = Integer.parseInt(timeParts[1]);

        System.out.println("New Time Parsed: Day = " + newDay + ", Start = " + newStart + ", End = " + newEnd);

        // 遍历已选课程，检查是否有冲突
        for (CourseSelection sc : selectedCourses) {
            // 解析已选课程的时间
            String[] existParts = sc.getClassTime().split("星期|\\D+");
            if (existParts.length < 3) {
                System.out.println("Invalid existing course time format: " + sc.getClassTime());  // 打印调试信息
                continue;  // 如果已选课程的时间格式不对，跳过
            }

            // 调试：打印已选课程的时间解析结果
            String existDay = existParts[1];
            int existStart = Integer.parseInt(existParts[2].split("-")[0]);
            int existEnd = Integer.parseInt(existParts[2].split("-")[1]);
            System.out.println("Existing Course Parsed: Day = " + existDay + ", Start = " + existStart + ", End = " + existEnd);

            // 判断是否是同一天
            if (newDay.equals(existDay)) {
                // 判断时间是否重叠
                if (newStart <= existEnd && newEnd >= existStart) {
                    System.out.println("Time Conflict Detected: New Time (" + newStart + "-" + newEnd + ") conflicts with Existing Time (" + existStart + "-" + existEnd + ")");
                    return true;  // 有时间冲突
                }
            }
        }

        return false;  // 没有时间冲突
    }


    // 退课
    @DeleteMapping("/withdraw")
    public Result withdrawCourse(HttpServletRequest request, @RequestBody EnrollRequest withdrawRequest) {
        String token = request.getHeader("token");

        if (token == null || token.isEmpty()) {
            return Result.errorPrint("token 丢失");
        }

        // 获取学生ID
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String studentId = decodedJWT.getClaim("code").asString();

        // 校验学期
        if (!withdrawRequest.getSemester().equals("202502")) {
            return Result.errorPrint("当前退课未开放，请选择2025学年第2学期的课程！");
        }

        // 查找选课记录
        CourseSelection selection = courseSelectionMapper.selectByCourseIdAndStudentId(
                withdrawRequest.getCourseId(),
                studentId
        );

        if (selection == null) {
            return Result.errorPrint("未找到选课记录");
        }

        // 删除选课记录
        courseSelectionMapper.deleteById(selection.getId());
        return Result.success();
    }


}
