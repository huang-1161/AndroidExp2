package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 深红色（标题 / 强调色） */
private val Crimson = Color(0xFFB71C1C)

/** 根布局浅色背景 */
private val PageBackground = Color(0xFFF7F7F9)

/** 进度文字灰色 */
private val HintGray = Color(0xFF757575)

/** 一条学习任务 */
private data class StudyTask(val title: String, val done: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ComponentActivity + setContent（替代 setContentView）
        setContent {
            MaterialTheme {
                CourseTaskScreen()
            }
        }
    }
}

@Composable
private fun CourseTaskScreen() {
    // 初始 3 条任务，第 1 条已完成
    val tasks = remember {
        mutableStateListOf(
            StudyTask("学习 Column 和 Row", done = true),
            StudyTask("学习状态管理", done = false),
            StudyTask("完成 Compose 实验", done = false)
        )
    }
    var input by remember { mutableStateOf("") }

    val doneCount = tasks.count { it.done }

    Scaffold(
        containerColor = PageBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PageBackground)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 标题
            Text(
                text = "课程学习任务",
                color = Crimson,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 输入框 + 添加按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "请输入学习任务") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        val typed = input.trim()
                        // 输入框为空时也要能添加，保证截图状态可复现
                        val title = if (typed.isEmpty()) "复习 LazyColumn" else typed
                        tasks.add(StudyTask(title, done = false))
                        input = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Crimson)
                ) {
                    Text(text = "添加", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 进度：注意全角冒号与斜杠两侧空格
            Text(
                text = "已完成：$doneCount / ${tasks.size}",
                color = HintGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 任务列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(tasks) { index, task ->
                    TaskRow(
                        title = task.title,
                        done = task.done,
                        onToggle = {
                            // 用新对象替换，保证 SnapshotStateList 触发重组
                            tasks[index] = task.copy(done = !task.done)
                        },
                        onDelete = { tasks.removeAt(index) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    title: String,
    done: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = done,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Crimson)
            )
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = if (done) HintGray else Color(0xFF212121),
                // 已完成任务显示删除线
                textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
            )
            TextButton(onClick = onDelete) {
                Text(text = "删除", color = Crimson)
            }
        }
    }
}
