## 目录结构
**Main.java 主程序入口**  
### **model**  
   * Admin 管理员实体类  
   * Course 课程实体类
   * User 用户实体类
     - Student 学生实体类
     - Teacher 教师实体类
### manager
  * FilePersistence IO工具类 （文件读取写入）  
  * CourseManager 课程管理类  
  * UserManager 用户管理类

管理类用于与文件交互
### utils
  * ConsoleUtils 输入工具类
  * IDGenerator 唯一ID生成器
     
## 待解决问题
1.课程时间是String类型，应该改成Date，学生选课时要校验课程时间冲突。  （现在代码中只校验了容量和课程中是否已包含该学生）  
2.添加提示：当信息为空时，输出提示（比如学生角色执行“查看我的课程”，如果他的课程列表为空，输出提示）  
3.选课成功与否无提示  
4.学生“查看我的课程”有问题

