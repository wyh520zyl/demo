package wuge.social.com.model

//任务列表
data class Task(
    val message: String,
    val result: TaskResult,
    val status: Int
)

data class TaskResult(
    val TaskList: List<TaskX>
)

data class TaskX(
    val exp: Int,
    val finish_num: String,
    val gold: Int,
    val status: String,
    val task_id: Int,
    val task_name: String,
    val total_num: Int
)