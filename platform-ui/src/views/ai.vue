<template>
  <div
    class="chat-container"
    :style="{ top: position.y + 'px', left: position.x + 'px' }"

  >
  <!--
   @mousedown.stop="startDrag"
   @mouseup.stop="stopDrag"
   @mousemove.stop="onDrag"-->
    <!-- 消息列表 -->
    <div class="message-list" ref="messageList">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message', message.type]"
      >
        <div class="bubble">
          <span v-if="message.type === 'ai' && message.isTyping" class="typing-indicator">
            <span></span><span></span><span></span>
          </span>
          <span v-else-if="message.type === 'file'">
            <a :href="message.fileUrl" target="_blank">{{ message.fileName }}</a>
          </span>
          <span v-else>{{ message.text }}</span>
        </div>
      </div>
    </div>

    <!-- 输入框 -->
    <div class="input-box">
      <input
        v-model="userInput"
        @keyup.enter="sendMessage"
        placeholder="输入消息..."
      />
      <button @click="sendMessage">发送</button>
      <input type="file" style="padding: 0;margin: 10px auto;" @change="uploadFile" />
      <button @click="openHistoryModal">查看历史</button>
    </div>

    <!-- 历史消息模态框 -->
    <div v-if="isHistoryModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeHistoryModal">&times;</span>
        <h2>历史消息</h2>
        <div class="message-list">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.type]"
          >
            <div class="bubble">
              <span v-if="message.type === 'file'">
                <a :href="message.fileUrl" target="_blank">{{ message.fileName }}</a>
              </span>
              <span v-else>{{ message.text }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userInput: "", // 用户输入
      messages: [], // 消息列表
      position: { x: 100, y: 100 }, // 对话框位置
      isDragging: false, // 是否正在拖拽
      dragStart: { x: 0, y: 0 }, // 拖拽起始位置
      isHistoryModalOpen: false, // 历史消息模态框是否打开
    };
  },
  methods: {
    // 发送消息
    sendMessage() {
      if (!this.userInput.trim()) return;

      // 添加用户消息
      this.messages.push({
        type: "user",
        text: this.userInput,
      });

      // 清空输入框
      this.userInput = "";

      // 滚动到底部
      this.scrollToBottom();

      // 模拟 AI 回复
      this.simulateAIResponse();
    },

    // 模拟 AI 回复
    simulateAIResponse() {
      // 添加 AI 正在输入的状态
      this.messages.push({
        type: "ai",
        text: "",
        isTyping: true,
      });

      // 滚动到底部
      this.scrollToBottom();

      // 延迟回复
      setTimeout(() => {
        // 移除正在输入状态
        this.messages.pop();

        // 模拟 AI 回复内容
        const aiResponse = "这是 AI 的回复，模拟打字效果。";
        this.typeMessage(aiResponse);
      }, 1500); // 延迟 1.5 秒
    },

    // 打字效果
    typeMessage(text) {
      let index = 0;
      const message = {
        type: "ai",
        text: "",
        isTyping: false,
      };

      // 添加 AI 消息
      this.messages.push(message);

      // 逐字显示
      const interval = setInterval(() => {
        if (index < text.length) {
          message.text += text[index];
          index++;
          // 每次更新内容后滚动到底部
          this.scrollToBottom();
        } else {
          clearInterval(interval);
        }
      }, 100); // 每个字符间隔 100ms
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const messageList = this.$refs.messageList;
        messageList.scrollTop = messageList.scrollHeight;
      });
    },

    // 开始拖拽
    startDrag(event) {
      this.isDragging = true;
      this.dragStart = {
        x: event.clientX - this.position.x,
        y: event.clientY - this.position.y,
      };
    },

    // 停止拖拽
    stopDrag() {
      this.isDragging = false;
    },

    // 拖拽中
    onDrag(event) {
      if (this.isDragging) {
        this.position = {
          x: event.clientX - this.dragStart.x,
          y: event.clientY - this.dragStart.y,
        };
      }
    },

    // 上传文件
    uploadFile(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          const fileUrl = e.target.result;
          this.messages.push({
            type: "file",
            fileName: file.name,
            fileUrl: fileUrl,
          });
          this.scrollToBottom();
          this.simulateAIResponse();
        };
        reader.readAsDataURL(file);
      }
    },

    // 打开历史消息模态框
    openHistoryModal() {
      this.isHistoryModalOpen = true;
    },

    // 关闭历史消息模态框
    closeHistoryModal() {
      this.isHistoryModalOpen = false;
    },
  },
};
</script>

<style scoped>
.chat-container {
  position: absolute;
  max-width: 400px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
  /* cursor: move; */
   /* 拖拽时显示移动光标 */
  /* user-select: none; */
  /* 防止拖拽时选中文本 */
}

.message-list {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.message.user {
  justify-content: flex-end;
}

.message.ai {
  justify-content: flex-start;
}

.bubble {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 15px;
  position: relative;
}

.message.user .bubble {
  background-color: #007bff;
  color: white;
}

.message.ai .bubble {
  background-color: #e9ecef;
  color: black;
}

.typing-indicator {
  display: inline-flex;
  align-items: center;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  margin: 0 2px;
  background-color: #999;
  border-radius: 50%;
  animation: blink 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%,
  100% {
    opacity: 0.2;
  }
  50% {
    opacity: 1;
  }
}

.input-box {
}

.input-box input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 10px;
}

.input-box button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;
}

.input-box button:hover {
  background-color: #0056b3;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 400px;
  width: 100%;
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  cursor: pointer;
}
</style>
