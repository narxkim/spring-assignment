const form = document.getElementById("chatForm");
const input = document.getElementById("messageInput");
const messages = document.getElementById("chatMessages");
const clearBtn = document.getElementById("clearBtn");

// 사용자는 보지 않지만, 브라우저 내부에 conversationId 저장
let conversationId = localStorage.getItem("travelConversationId");

if (!conversationId) {
    conversationId = crypto.randomUUID();
    localStorage.setItem("travelConversationId", conversationId);
}

// README 캡처용/사용자용: Persistent API 고정
const endpoint = "/api/travel-persistent";

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    await sendMessage();
});

input.addEventListener("keydown", async (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        await sendMessage();
    }
});

if (clearBtn) {
    clearBtn.addEventListener("click", () => {
        messages.innerHTML = `
      <div class="message bot">
        안녕하세요. 여행지, 기간, 예산, 이동수단을 알려주시면 여행 계획을 추천해드릴게요.
      </div>
    `;
    });
}

async function sendMessage() {
    const message = input.value.trim();

    if (!message) return;

    appendMessage(message, "user");
    input.value = "";

    const loading = appendMessage("답변을 생성하는 중입니다...", "bot loading");

    const params = new URLSearchParams();
    params.append("message", message);
    params.append("conversationId", conversationId);

    const url = `${endpoint}?${params.toString()}`;

    try {
        const response = await fetch(url);
        const answer = await response.text();

        loading.remove();

        if (!response.ok) {
            appendMessage("API 요청 중 오류가 발생했습니다.", "bot");
            return;
        }

        appendMessage(answer, "bot");

    } catch (error) {
        loading.remove();
        appendMessage("서버 연결에 실패했습니다. Spring Boot 서버가 실행 중인지 확인해주세요.", "bot");
        console.error(error);
    }
}

function appendMessage(text, type) {
    const div = document.createElement("div");
    div.className = `message ${type}`;
    div.textContent = text;

    messages.appendChild(div);
    messages.scrollTop = messages.scrollHeight;

    return div;
}