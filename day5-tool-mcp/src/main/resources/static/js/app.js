document.addEventListener("DOMContentLoaded", () => {

    const questionInput = document.getElementById("question");
    const sendBtn = document.getElementById("sendBtn");
    const newChatBtn = document.getElementById("newChatBtn");
    const chatHistory = document.getElementById("chatHistory");

    let chatCount = 1;
    let chatId = `chat-${chatCount}`;

    sendBtn.addEventListener("click", sendQuestion);

    newChatBtn.addEventListener("click", () => {
        chatCount++;
        chatId = `chat-${chatCount}`;

        chatHistory.innerHTML = `
        <div class="message ai">
            <div class="profile">🤖</div>
            <div class="bubble">
                새로운 대화를 시작했습니다.<br>
                여행지를 입력해주세요.
            </div>
        </div>
        `;

        questionInput.value = "";
        questionInput.focus();
    });

    questionInput.addEventListener("keydown", e => {
        if (e.ctrlKey && e.key === "Enter") {
            sendQuestion();
        }
    });

    async function sendQuestion() {
        const question = questionInput.value.trim();

        if (!question) {
            alert("여행 요청을 입력하세요.");
            return;
        }

        appendUser(question);
        questionInput.value = "";

        sendBtn.disabled = true;
        sendBtn.textContent = "생성 중...";

        const loadingBubble = appendLoading();

        try {
            const params = new URLSearchParams({
                chatId,
                question
            });

            const response = await fetch(`/api/travel/chat?${params}`);

            if (!response.ok) {
                throw new Error(`요청 실패: ${response.status}`);
            }

            const answer = await response.text();

            loadingBubble.classList.remove("loading");
            loadingBubble.classList.add("markdown-body");
            loadingBubble.innerHTML = marked.parse(answer);

            scrollBottom();

        } catch (e) {
            loadingBubble.classList.remove("loading");
            loadingBubble.classList.add("markdown-body");
            loadingBubble.innerHTML = marked.parse("오류가 발생했습니다.\n\n" + e.message);

            scrollBottom();

        } finally {
            sendBtn.disabled = false;
            sendBtn.textContent = "여행 일정 생성";
        }
    }

    function appendUser(text) {
        const safeText = escapeHtml(text);

        chatHistory.insertAdjacentHTML("beforeend", `
        <div class="message user">
            <div class="bubble">${safeText}</div>
            <div class="profile">👤</div>
        </div>
        `);

        scrollBottom();
    }

    function appendAI(text) {
        const html = marked.parse(text);

        chatHistory.insertAdjacentHTML("beforeend", `
        <div class="message ai">
            <div class="profile">🤖</div>
            <div class="bubble markdown-body">${html}</div>
        </div>
        `);

        scrollBottom();
    }

    function appendLoading() {
        chatHistory.insertAdjacentHTML("beforeend", `
        <div class="message ai">
            <div class="profile">🤖</div>
            <div class="bubble loading">
                ✈️ 여행 일정을 생성하고 있습니다...
            </div>
        </div>
        `);

        scrollBottom();

        const loadingBubbles = chatHistory.querySelectorAll(".bubble.loading");
        return loadingBubbles[loadingBubbles.length - 1];
    }

    function scrollBottom() {
        chatHistory.scrollTop = chatHistory.scrollHeight;
    }

    function escapeHtml(text) {
        return text
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }
});